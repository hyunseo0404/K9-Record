package com.gtpd.k9.k9record;

public class Explosive {

    public String name;
    public double quantity;
    public Unit unit;
    public String location;
    public int imageResource;

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
}
