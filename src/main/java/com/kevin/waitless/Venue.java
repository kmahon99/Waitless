package com.kevin.waitless;

import java.util.ArrayList;

/**
 * A class to represent the establishment customers book to (restaurant/dentist etc.)
 */

public class Venue {
    private String name, address;
    private long venue_id;

    Venue(String name,
          String address,
          long system_id){
        this.name = name;
        this.address = address;
        this.venue_id = system_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getSystem_id() {
        return venue_id;
    }

    public void setSystem_id(long system_id) {
        this.venue_id = system_id;
    }
}
