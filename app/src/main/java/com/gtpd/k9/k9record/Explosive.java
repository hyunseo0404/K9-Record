package com.gtpd.k9.k9record;

import java.util.Date;

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
}
