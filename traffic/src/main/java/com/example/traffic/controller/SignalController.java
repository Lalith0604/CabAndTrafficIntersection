// File: controller/SignalController.java
package com.example.traffic.controller;

import com.example.traffic.model.TrafficSignal;
import com.example.traffic.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class SignalController {

    @Autowired
    private SignalService signalService;


    @GetMapping("/signal-status")
    public Map<String, Object> getSignalStatus(@RequestParam double lat, @RequestParam double lon) {
        TrafficSignal nearest = signalService.findNearestSignal(lat, lon);

        if (nearest == null) {
            return Map.of("error", "No nearby signal found");
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("signal_name", nearest.getName());
        response.put("status", nearest.getStatus());
        response.put("latitude", nearest.getLatitude());
        response.put("longitude", nearest.getLongitude());

        if (nearest.getStatus().equals("RED")) {
            response.put("remaining_time_sec", signalService.getRemainingTime(nearest));
        }

        return response;
    }


    @GetMapping("/all-signals")
    public List<TrafficSignal> getAllSignals() {
        return signalService.getAllSignals();
    }
}
