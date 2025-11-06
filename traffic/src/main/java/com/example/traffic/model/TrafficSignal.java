// File: model/TrafficSignal.java
package com.example.traffic.model;

public class TrafficSignal {
    private String name;
    private double latitude;
    private double longitude;
    private String status; // RED or GREEN
    private long lastChanged; // timestamp of last change

    public TrafficSignal(String name, double latitude, double longitude, String status) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.lastChanged = System.currentTimeMillis();
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getStatus() { return status; }
    public long getLastChanged() { return lastChanged; }

    public void setStatus(String status) {
        this.status = status;
        this.lastChanged = System.currentTimeMillis();
    }

    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - lastChanged) / 1000;
    }
}
