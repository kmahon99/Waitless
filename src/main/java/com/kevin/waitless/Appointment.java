package com.kevin.waitless;

import java.time.Clock;
import java.time.ZonedDateTime;

/**
 * A class to represent a Client's appointment at a Venue
 */

public class Appointment {
    private long patient_id, venue_id;
    private ZonedDateTime app_time;
    private int event_type;

    Appointment(long patient_id,
                long venue_id,
                ZonedDateTime app_time,
                int event_type){
        this.patient_id = patient_id;
        this.venue_id = venue_id;
        this.app_time = app_time;
        this.event_type = event_type;
    }

    public long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(long patient_id) {
        this.patient_id = patient_id;
    }

    public long getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public ZonedDateTime getApp_time() {
        return app_time;
    }

    public void setApp_time(ZonedDateTime app_time) {
        this.app_time = app_time;
    }

    public int getEvent_type() {
        return event_type;
    }

    public void setEvent_type(int event_type) {
        this.event_type = event_type;
    }
}
