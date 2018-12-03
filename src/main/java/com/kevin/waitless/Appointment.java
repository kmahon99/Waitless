package com.kevin.waitless;

import java.time.ZonedDateTime;

/**
 * A class to represent a Customer's appointment at a Venue
 */
public class Appointment {

    /**
     * The type of appointment the customer has booked
     */
    public enum AppointmentType{
        RESTAURANT_TABLE_BOOKING,
        GP_STANDARD,
        DENTIST_STANDARD
    }

    private byte[] customer_id, venue_id;
    private ZonedDateTime app_time;
    private AppointmentType event_type;

    Appointment(byte[] customer_id,
                byte[] venue_id,
                ZonedDateTime app_time,
                AppointmentType event_type){
        this.customer_id = customer_id;
        this.venue_id = venue_id;
        this.app_time = app_time;
        this.event_type = event_type;
    }

    public byte[] getPatient_id() {
        return customer_id;
    }

    public void setPatient_id(byte[] patient_id) {
        this.customer_id = patient_id;
    }

    public byte[] getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(byte[] venue_id) {
        this.venue_id = venue_id;
    }

    public ZonedDateTime getApp_time() {
        return app_time;
    }

    public void setApp_time(ZonedDateTime app_time) {
        this.app_time = app_time;
    }

    public AppointmentType getEvent_type() {
        return event_type;
    }

    public void setEvent_type(AppointmentType event_type) {
        this.event_type = event_type;
    }
}
