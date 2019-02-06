package com.kevin.waitless;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "USER")
public class User {

    @NonNull
    @PrimaryKey
    private String email;
    @ColumnInfo(name = "firstname")
    private String firstname;
    @ColumnInfo(name = "surname")
    private String surname;
    @ColumnInfo(name = "staffed_venue")
    private HashMap<String,String> staffed_venues; //If the user works for a venue or set of venues, store the string ID
    @ColumnInfo(name = "rating")
    private short rating; //A value between -100 and 100 indicating the user's reputability


    public User() {
        this.email = this.firstname = this.surname = "";
        this.rating = 0;
        this.staffed_venues = new HashMap<>();
    }


    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public HashMap<String,String> getStaffed_venues() {
        return this.staffed_venues;
    }

    public void setStaffed_venues(HashMap<String,String> staffed_venues) {
        this.staffed_venues = staffed_venues;
    }

    public void addStaffed_venue(Venue venue){ this.staffed_venues.put(venue.getVenue_id(),venue.getName()); }

    public short getRating() {
        return this.rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
        if(this.rating > 100){ this.rating = 100; }
        else if(this.rating < -100){ this.rating = -100; }
    }

    public void updateRating(short value) {
        this.rating += rating;
        if(this.rating > 100){ this.rating = 100; }
        else if(this.rating < -100){ this.rating = -100; }
    }

    @NonNull
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}

@Dao
interface ClientDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);
    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
    @Query("SELECT * FROM USER")
    User getUser();
}

class StringHashMapTypeConverter{
    @TypeConverter
    public HashMap<String,String> toHashMapString(String map){
        return new Gson().fromJson(map,new TypeToken<HashMap<String,String>>(){}.getType());
    }
    @TypeConverter
    public String toJsonString(HashMap<String,String> map){
        return new Gson().toJson(map);
    }
}