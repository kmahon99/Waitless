package com.kevin.waitless;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.TypeConverter;
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
    private String user;
    @ColumnInfo(name = "booking_venue")
    private String venue_id;
    @ColumnInfo(name = "booking_status")
    private Status booking_status;

    enum Status{
        PENDING,
        CONFIRMED,
        CANCELLED,
        DECLINED
    }

    public Booking(int year, int month, int day, int hour, int min, String user, String venue_id){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.user = user;
        this.venue_id = venue_id;
        this.booking_status = Status.PENDING;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user_id) {
        this.user = user_id;
    }

    public Status getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(Status booking_status) {
        this.booking_status = booking_status;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
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
    @Query("SELECT * FROM BOOKINGS WHERE booking_venue = :booking_venue")
    List<Booking> getBookingsByVenue(String booking_venue);
}

class StatusTypeConverter{
    @TypeConverter
    public static Booking.Status toStatus(String value){ return Booking.Status.valueOf(value); }

    @TypeConverter
    public static String toString(Booking.Status value){ return value.name(); }
}
