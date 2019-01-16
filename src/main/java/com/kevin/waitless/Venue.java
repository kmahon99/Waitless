package com.kevin.waitless;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverter;

import java.sql.Time;
import java.util.List;

/**
 * A class to represent the establishment customers book to (restaurant/dentist etc.)
 */
@Entity(tableName = "VENUES")
public class Venue implements Comparable<Venue>{

    @PrimaryKey
    private long venue_id;
    @ColumnInfo(name="venue_name")
    private String name;
    @ColumnInfo(name="venue_address")
    private String address;
    @ColumnInfo(name = "venue_openingtime")
    private Time opening_time;
    @ColumnInfo(name = "venue_closingtime")
    private Time closing_time;

    @ColumnInfo(name="venue_type")
    private String type;

    Venue(){}

    Venue(long venue_id,
          String name,
          String address){
        this.venue_id = venue_id;
        this.name = name;
        this.address = address;
        this.type = "";
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

    public long getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public String getType(){ return this.type; }

    public void setType(String type){ this.type = type; }

    @Override
    public int compareTo(Venue v) {
        return this.getName().compareTo(v.getName()) == 0 ? 0 : -1;
    }

    public Time getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(Time opening_time) {
        this.opening_time = opening_time;
    }

    public Time getClosing_time() {
        return closing_time;
    }

    public void setClosing_time(Time closing_time) {
        this.closing_time = closing_time;
    }
}

@Dao
interface VenueDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertVenue(Venue venue);
    @Query("DELETE FROM VENUES WHERE venue_name = :name")
    void deleteVenue(String name);
    @Query("SELECT * FROM VENUES WHERE 1")
    List<Venue> getAllVenues();
    @Query("SELECT DISTINCT * FROM VENUES WHERE venue_name LIKE '%'||:name||'%'")
    List<Venue> getVenuesByName(String name);
    @Query("SELECT * FROM VENUES WHERE venue_id = :venue_id")
    List<Venue> getVenueById(long venue_id);
}


class TimeTypeConverter{
    @TypeConverter
    public static Time toTime(long value){
        return new Time(value);
    }

    @TypeConverter
    public static Long toLong(Time value){
        return value == null ? 0 : value.getTime();
    }
}