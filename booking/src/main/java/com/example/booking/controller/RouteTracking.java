package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.Route;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/route")
public class RouteTracking {

    private final Route routeService;

    User users=new User();

    public RouteTracking(Route routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/set-location")
    public Map<String, Object> setUserLocation(@RequestBody Map<String, Double> body) {
        double lat = body.getOrDefault("latitude", 0.0);
        double lon = body.getOrDefault("longitude", 0.0);

        users.setCurr_lat(lat);
        users.setCurr_lon(lon);

        Map<String, Object> res = new HashMap<>();
        res.put("message", "📍 User location received successfully");
        res.put("latitude", lat);
        res.put("longitude", lon);
        return res;
    }


    @GetMapping("/path")
    public int path() {
        System.out.println("path");
        return routeService.fetchRouteFromMapbox(78.5278, 17.3853,users.getCurr_lon(), users.getCurr_lat());
    }

    @PostMapping("/start")
    public Map<String, Object> startRoute() {
        int size = routeService.fetchRouteFromMapbox(78.5278, 17.3853,users.getCurr_lon(), users.getCurr_lat());
        routeService.startMoving();


        Map<String, Object> res = new HashMap<>();
        res.put("message", "✅ Started following the real route");
        res.put("total_points", size);
        return res;
    }

    @PostMapping("/stop")
    public Map<String, Object> stopRoute() {
        routeService.stopMoving();

        Map<String, Object> res = new HashMap<>();
        res.put("message", "🛑 Route simulation stopped");
        res.put("current_index", routeService.getCurrentIndex());
        return res;
    }

    @GetMapping("/current")
    public Map<String, Object> getCurrentLocation() {
        return routeService.getCurrentLocation();
    }
}
