// File: service/SignalService.java
package com.example.traffic.service;

import com.example.traffic.model.TrafficSignal;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SignalService {

    private List<TrafficSignal> signals = new ArrayList<>();

    public SignalService() {
        signals.add(new TrafficSignal("Panjagutta Junction", 17.4239, 78.4521, "RED"));
        signals.add(new TrafficSignal("Ameerpet Signal", 17.4389, 78.4489, "GREEN"));
        signals.add(new TrafficSignal("Kukatpally Cross", 17.4946, 78.3992, "RED"));
        signals.add(new TrafficSignal("Secunderabad Clock Tower", 17.4399, 78.4983, "GREEN"));
        signals.add(new TrafficSignal("Paradise Junction", 17.4390, 78.4866, "RED"));
        signals.add(new TrafficSignal("Banjara Hills Road No. 2", 17.4149, 78.4349, "RED"));
        signals.add(new TrafficSignal("Jubilee Hills Check Post", 17.4325, 78.4077, "GREEN"));
        signals.add(new TrafficSignal("Madhapur Signal", 17.4485, 78.3919, "GREEN"));
        signals.add(new TrafficSignal("Hitech City Junction", 17.4487, 78.3820, "RED"));
        signals.add(new TrafficSignal("Durgam Cheruvu Cable Bridge", 17.4356, 78.3933, "GREEN"));
        signals.add(new TrafficSignal("Gachibowli Circle", 17.4401, 78.3489, "RED"));
        signals.add(new TrafficSignal("Kothaguda Junction", 17.4705, 78.3738, "GREEN"));
        signals.add(new TrafficSignal("Miyapur Metro Signal", 17.4986, 78.3664, "RED"));
        signals.add(new TrafficSignal("Lingampally Station", 17.4923, 78.3054, "GREEN"));
        signals.add(new TrafficSignal("Chandanagar Signal", 17.4898, 78.3342, "RED"));
        signals.add(new TrafficSignal("Madhapur Police Station", 17.4500, 78.3927, "RED"));
        signals.add(new TrafficSignal("Inorbit Mall Junction", 17.4340, 78.3844, "GREEN"));
        signals.add(new TrafficSignal("Raidurg Metro Signal", 17.4375, 78.3815, "GREEN"));
        signals.add(new TrafficSignal("Mehdipatnam Junction", 17.3917, 78.4340, "RED"));
        signals.add(new TrafficSignal("Tolichowki Circle", 17.3984, 78.4032, "GREEN"));
        signals.add(new TrafficSignal("Shaikpet Signal", 17.4108, 78.3935, "RED"));
        signals.add(new TrafficSignal("Lakdi-ka-pul Junction", 17.4063, 78.4622, "RED"));
        signals.add(new TrafficSignal("Nampally Station Road", 17.3940, 78.4716, "GREEN"));
        signals.add(new TrafficSignal("Abids Circle", 17.3955, 78.4737, "RED"));
        signals.add(new TrafficSignal("Koti Signal", 17.3889, 78.4843, "GREEN"));
        signals.add(new TrafficSignal("RTC Cross Roads", 17.4055, 78.5015, "RED"));
        signals.add(new TrafficSignal("Narayanaguda Signal", 17.3988, 78.4869, "GREEN"));
        signals.add(new TrafficSignal("Musheerabad Junction", 17.4121, 78.5013, "RED"));
        signals.add(new TrafficSignal("Chikkadpally Signal", 17.4022, 78.4966, "GREEN"));
        signals.add(new TrafficSignal("Amberpet Cross", 17.3987, 78.5093, "RED"));
        signals.add(new TrafficSignal("Dilsukhnagar Metro", 17.3693, 78.5251, "GREEN"));
        signals.add(new TrafficSignal("LB Nagar Junction", 17.3422, 78.5453, "RED"));
        signals.add(new TrafficSignal("Nagole Signal", 17.3732, 78.5628, "GREEN"));
        signals.add(new TrafficSignal("Uppal X Roads", 17.4059, 78.5590, "RED"));
        signals.add(new TrafficSignal("Tarnaka Cross", 17.4220, 78.5331, "GREEN"));
        signals.add(new TrafficSignal("Mettuguda Junction", 17.4422, 78.5202, "RED"));
        signals.add(new TrafficSignal("Habsiguda Metro", 17.4102, 78.5401, "GREEN"));
        signals.add(new TrafficSignal("ECIL X Roads", 17.4799, 78.5652, "RED"));
        signals.add(new TrafficSignal("Safilguda Signal", 17.4750, 78.5299, "GREEN"));
        signals.add(new TrafficSignal("Neredmet X Road", 17.4848, 78.5398, "RED"));
        signals.add(new TrafficSignal("Alwal Signal", 17.4981, 78.5180, "GREEN"));
        signals.add(new TrafficSignal("Suchitra Junction", 17.5054, 78.4789, "RED"));
        signals.add(new TrafficSignal("Bowenpally Check Post", 17.4745, 78.4765, "GREEN"));
        signals.add(new TrafficSignal("Balanagar X Road", 17.4692, 78.4401, "RED"));
        signals.add(new TrafficSignal("Sanath Nagar Signal", 17.4528, 78.4445, "GREEN"));
        signals.add(new TrafficSignal("Erragadda Signal", 17.4471, 78.4384, "RED"));
        signals.add(new TrafficSignal("SR Nagar Junction", 17.4429, 78.4482, "GREEN"));
    }


    public List<TrafficSignal> getAllSignals() {
        return signals;
    }

    public TrafficSignal findNearestSignal(double lat, double lon) {
        TrafficSignal nearest = null;
        double minDist = Double.MAX_VALUE;

        for (TrafficSignal s : signals) {
            double dist = distance(lat, lon, s.getLatitude(), s.getLongitude());
            if (dist < minDist) {
                minDist = dist;
                nearest = s;
            }
        }
        if (!(minDist >= 1)) {
            return nearest;
        }
        return null;
    }

    // Calculate distance between two lat/lon points (Haversine formula)
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Auto-cycle signals every 60 seconds
    @Scheduled(fixedRate = 60000)
    public void cycleSignals() {
        for (TrafficSignal s : signals) {
            if (s.getStatus().equals("RED")) {
                s.setStatus("GREEN");
            } else {
                s.setStatus("RED");
            }
            System.out.println("🔄 Signal changed: " + s.getName() + " → " + s.getStatus());
        }
    }

    public long getRemainingTime(TrafficSignal signal) {
        long elapsed = signal.getElapsedSeconds();
        long cycleDuration = 60;
        return Math.max(0, cycleDuration - elapsed);
    }
}
