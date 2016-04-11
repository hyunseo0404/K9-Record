package com.gtpd.k9.k9record;

import java.util.Date;

import java.sql.Time;
import java.sql.Timestamp;

public class Explosive {

    public String name;
    public double quantity;
    public Unit unit;
    public String location;
    public double height;
    public double depth;
    public Date placementTime;
    public String odor;
    public String container;
    public int imageResource;
    public int unitResource;
    public Timestamp startTime;
    public Timestamp endTime;

    public Explosive(String name, double quantity, Unit unit, String location, int imageResource, int unitResource) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.imageResource = imageResource;
        this.unitResource = unitResource;
    }

    public Explosive(String name, int imageResource, int unitResource) {
        this(name, 0, null, null, imageResource, unitResource);
    }

    public String getQuantityAsString() {
        if (unit == Unit.STICK) {
            return String.format("%d stick%c", (int) quantity, quantity > 1 ? 's' : '\0');
        } else {
            return String.format("%s %s", Double.toString(quantity), unit.name().toLowerCase());
        }
    }

    public enum Unit {
        KG, G, LB, OZ, IN, FT, STICK
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
