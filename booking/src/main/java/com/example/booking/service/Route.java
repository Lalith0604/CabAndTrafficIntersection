package com.example.booking.service;

import com.example.booking.model.Cab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class Route {

    private static final String MAPBOX_TOKEN = "pk.eyJ1IjoibGFsaXRoMDYwNCIsImEiOiJjbWhobHc2a3gwbDUxMm9xdm0zOHl0cG0wIn0.9jkPLS8iFQA83HVXbIxMCw";
    private static final String DIRECTIONS_URL = "https://api.mapbox.com/directions/v5/mapbox/driving/";

    private final List<double[]> routePoints = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isMoving = false;

    Cab cab=new Cab();

    public int fetchRouteFromMapbox(double startLon, double startLat, double endLon, double endLat) {
        try {
            System.out.println("Fetching route from Mapbox...");

            String url = DIRECTIONS_URL + startLon + "," + startLat + ";" + endLon + "," + endLat +
                    "?geometries=geojson&access_token=" + MAPBOX_TOKEN;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("Error: Invalid response from Mapbox API");
                return 0;
            }

            cab.setDist_lon(endLon);
            cab.setDist_lat(endLat);

            Map<?, ?> body = response.getBody();
            List<?> routes = (List<?>) body.get("routes");
            if (routes == null || routes.isEmpty()) {
                System.out.println("No route found!");
                return 0;
            }

            Map<?, ?> route = (Map<?, ?>) routes.get(0);
            Map<?, ?> geometry = (Map<?, ?>) route.get("geometry");
            List<?> coordinates = (List<?>) geometry.get("coordinates");

            routePoints.clear();
            for (Object c : coordinates) {
                List<?> pair = (List<?>) c;
                double lon = ((Number) pair.get(0)).doubleValue();
                double lat = ((Number) pair.get(1)).doubleValue();
                routePoints.add(new double[]{lat, lon});
            }

            System.out.println("✅ Route loaded with " + routePoints.size() + " points.");
            return routePoints.size();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void startMoving() {
        if (routePoints.isEmpty()) {
            System.out.println("⚠️ No route loaded.");
            return;
        }
        isMoving = true;
        currentIndex = 0;
        System.out.println("🚗 Movement started!");
    }

    public void stopMoving() {
        isMoving = false;
        System.out.println("🛑 Movement stopped.");
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public Map<String, Object> getCurrentLocation() {
        Map<String, Object> res = new HashMap<>();
        if (routePoints.isEmpty()) {
            res.put("message", "⚠️ No route loaded");
            return res;
        }
        double[] current = routePoints.get(Math.min(currentIndex, routePoints.size() - 1));
        res.put("latitude", current[0]);
        res.put("longitude", current[1]);
        res.put("index", currentIndex);
        res.put("moving", isMoving);
        return res;
    }

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Object> fetchNearestSignal(double lat, double lon) {
        try {
            String url = String.format(
                    "http://localhost:8080/api/signal-status?lat=%f&lon=%f",
                    lat, lon
            );
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            System.out.println("⚠️ Error contacting Traffic Signal API: " + e.getMessage());
            return null;
        }
    }


    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

    @Scheduled(fixedRate = 20000)
    public void moveAlongRoute() {
        if (!isMoving || routePoints.isEmpty()) return;

        if (currentIndex >= routePoints.size() - 1) {
            System.out.println("🏁 Reached destination!");
            isMoving = false;
            return;
        }

        double[] prev = routePoints.get(Math.max(0, currentIndex - 1));
        double[] curr = routePoints.get(currentIndex);
        double[] next = routePoints.get(Math.min(currentIndex + 1, routePoints.size() - 1));

        cab.setCurr_lat(curr[0]);
        cab.setCurr_lon(curr[1]);

        // ✅ Fetch nearest signal info
        Map<String, Object> signalInfo = fetchNearestSignal(curr[0], curr[1]);

        if (signalInfo != null) {
            String signalName = (String) signalInfo.get("signal_name");
            String status = (String) signalInfo.get("status");
            Object remaining = signalInfo.getOrDefault("remaining_time_sec", "N/A");

            // Distance between cab and signal
            double signalLat = Double.parseDouble(signalInfo.getOrDefault("latitude", curr[0]).toString());
            double signalLon = Double.parseDouble(signalInfo.getOrDefault("longitude", curr[1]).toString());
            double distKm = distanceInKm(curr[0], curr[1], signalLat, signalLon);

            System.out.printf(
                    "🚦 Signal: %s | Status: %s | Distance: %.4fm | Remaining: %s sec%n",
                    signalName, status, distKm * 1000, remaining
            );

            // 🛑 Stop only if signal is within 100m and is RED
            if (distKm <= 1 && "RED".equalsIgnoreCase(status)) {
                System.out.printf("🛑 Waiting at %s (RED, within %.2f m)...%n", signalName, distKm * 1000);

                // Recheck every 5s until GREEN
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    Map<String, Object> updatedSignal = fetchNearestSignal(curr[0], curr[1]);
                    if (updatedSignal == null) break;

                    String newStatus = (String) updatedSignal.get("status");
                    if ("GREEN".equalsIgnoreCase(newStatus)) {
                        System.out.printf("✅ %s turned GREEN. Resuming movement.%n", signalName);
                        break;
                    } else {
                        Object remain = updatedSignal.getOrDefault("remaining_time_sec", "N/A");
                        System.out.printf("⏳ Still RED at %s (%s sec left)%n", signalName, remain);
                    }
                }
            }
        }

        // 🚗 Proceed to next point
        String direction = getTurnDirection(prev, curr, next);
        System.out.printf("📍 Point %d/%d → (%.6f, %.6f) | %s%n",
                currentIndex + 1, routePoints.size(), curr[0], curr[1], direction);

        currentIndex++;
    }



    private String getTurnDirection(double[] prev, double[] curr, double[] next) {
        if (prev == null) return "Starting";
        double angle = getBearing(curr, next) - getBearing(prev, curr);
        if (angle > 30) return "Turn Right";
        if (angle < -30) return "Turn Left";
        return "Straight";
    }

    private double getBearing(double[] a, double[] b) {
        double lat1 = Math.toRadians(a[0]);
        double lon1 = Math.toRadians(a[1]);
        double lat2 = Math.toRadians(b[0]);
        double lon2 = Math.toRadians(b[1]);
        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2)
                - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        return Math.toDegrees(Math.atan2(y, x));
    }
}
