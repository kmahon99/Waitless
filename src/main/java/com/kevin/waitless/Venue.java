package com.kevin.waitless;

/**
 * A class to represent the establishment patients have appointments at (GP/dentist etc.)
 */

public class Venue {
    private String name, address;
    private long system_id;

    Venue(String name,
          String address,
          long system_id){
        this.name = name;
        this.address = address;
        this.system_id = system_id;
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
        return system_id;
    }

    public void setSystem_id(long system_id) {
        this.system_id = system_id;
    }
}
