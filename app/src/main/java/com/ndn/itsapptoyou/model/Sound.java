package com.ndn.itsapptoyou.model;

public class Sound {
    private int resourceId;
    private String name;

    public Sound(String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }
}
