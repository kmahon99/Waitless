package com.kevin.waitless;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Entity(tableName = "BOOKINGS")
public class Booking {

    @PrimaryKey(autoGenerate = true)
    private long booking_id;
    @ColumnInfo(name = "booking_year")
    private int year;
    @ColumnInfo(name = "booking_month")
    private int month;
    @ColumnInfo(name = "booking_day")
    private int day;
    @ColumnInfo(name = "booking_hour")
    private int hour;
    @ColumnInfo(name = "booking_min")
    private int min;
    @ColumnInfo(name = "booking_user")
    private SystemID user;
    @ColumnInfo(name = "booking_venue")
    private long venue;

    public Booking(int year, int month, int day, int hour, int min, SystemID user, long venue){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.user = user;
        this.venue = venue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(long booking_id) {
        this.booking_id = booking_id;
    }

    public SystemID getUser() {
        return user;
    }

    public void setUser(SystemID user_id) {
        this.user = user_id;
    }

    public long getVenue() {
        return venue;
    }

    public void setVenue(long venue) {
        this.venue = venue;
    }
}

@Dao
interface BookingDao{
    @Insert
    void insert(Booking booking);
    @Update
    void update(Booking... bookings);
    @Delete
    void delete(Booking... bookings);
    @Query("SELECT * FROM BOOKINGS")
    List<Booking> getAllBookings();
    @Query("SELECT * FROM BOOKINGS WHERE booking_id = :booking_id")
    List<Booking> getBookingById(long booking_id);
}