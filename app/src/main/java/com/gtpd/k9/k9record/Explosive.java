package com.gtpd.k9.k9record;

import java.sql.Time;
import java.sql.Timestamp;

public class Explosive {

    public String name;
    public double quantity;
    public Unit unit;
    public String location;
    public int imageResource;
    public Timestamp startTime;
    public Timestamp endTime;

    public Explosive(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public Explosive(String name, double quantity, Unit unit, String location, int imageResource) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.imageResource = imageResource;
    }

    public String getQuantityAsString(){
        return String.format("%.0f", quantity);
    }

    public enum Unit {
        KG, G, LB, OZ
    }

    public void setStartTime(Timestamp start) {
        startTime = start;
    }

    public void setEndTime(Timestamp end){
        endTime = end;
    }

    public String getEllapsedTime() {
        long diff = endTime.getTime() - startTime.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String hour_str = (hours < 10) ? "0" + hours: "" + hours;
        String minutes_str = (minutes < 10) ? "0" + minutes: "" + minutes;
        String seconds_str = (seconds < 10) ? "0" + seconds: "" + seconds;
        return "" + hour_str + ":" + minutes_str + ":" + seconds_str;
    }
}
