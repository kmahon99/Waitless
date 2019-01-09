package com.kevin.waitless;

import androidx.room.Dao;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A restaurant is a concrete version of a venue, containing extra info on tables and reservations
 */
@Entity
public class Restaurant extends Venue {

    //private List<Table> tables;

    public Restaurant(String name, String address){
        this.setName(name);
        this.setAddress(address);
        //this.tables = new ArrayList<>();
        this.setType("Restaraunt");
    }

    private class Table{

        private int quantity;

    }


}

/**
 * May want to implement specific searches for this class e.g. reservation-based searches
 */
@Dao
interface RestaurantDao extends VenueDao{}