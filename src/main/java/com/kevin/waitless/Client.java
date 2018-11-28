package com.kevin.waitless;

/**
 * Process_State reperesents the current position a patient is in within the system
 */

enum Process_State {
    CLIENT_NOT_BOOKED,
    CLIENT_BOOKED,
    CLIENT_QUEUED,
    CLIENT_IN_PROGRESS,
    CLIENT_DISCHARGED,
    CLIENT_NO_SHOW
}

/**
 * Class to represent a patient, uses the client side of app
 */

public class Client {
    private String first_name,middle_name,surname,address;
    private long system_id;
    private int PROCESS_STATE;

    Client(String first_name,
           String middle_name,
           String surname,
           long id,
           int PROCESS_STATE){
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.surname = surname;
        this.system_id = id;
        this.PROCESS_STATE = PROCESS_STATE;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getSystem_id() {
        return system_id;
    }

    public void setSystem_id(long system_id) {
        this.system_id = system_id;
    }

    public int getPROCESS_STATE() {
        return PROCESS_STATE;
    }

    public void setPROCESS_STATE(int PROCESS_STATE) {
        this.PROCESS_STATE = PROCESS_STATE;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
