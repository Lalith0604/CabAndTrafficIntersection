package com.example.booking.model;

public class Cab {

    private double curr_lon;

    private double curr_lat;

    private double dist_lon;

    private double dist_lat;

    private int speed;

    public double getCurr_lon() {return curr_lon;}

    public double getCurr_lat() {return curr_lat;}

    public double getDist_lon() {return dist_lon;}

    public double getDist_lat() {return dist_lat;}

    public int getSpeed() {return speed;}

    public void setCurr_lon(double curr_lon) {this.curr_lon = curr_lon;}

    public void setCurr_lat(double curr_lat) {this.curr_lat = curr_lat;}

    public void setDist_lon(double dist_lon) {this.dist_lon = dist_lon;}

    public void setDist_lat(double dist_lat) {this.dist_lat = dist_lat;}

    public void setSpeed(int speed) {this.speed = speed;}
}
