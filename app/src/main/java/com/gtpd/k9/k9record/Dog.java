package com.gtpd.k9.k9record;

public class Dog {

    private int id;
    private String name;
    private String description;
    private String imageResource;

    public Dog(int id, String name, String description, String imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageResource() {
        return imageResource;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Dog && ((Dog) o).id == id;
    }
}
