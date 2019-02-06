package com.kevin.waitless;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

@Database(entities = {User.class,Venue.class,Restaurant.class,Booking.class}, version = 19)
@TypeConverters({SystemIDTypeConverter.class,TimeTypeConverter.class,StatusTypeConverter.class,StringHashMapTypeConverter.class})
public abstract class WaitlessDatabase extends RoomDatabase {

    private static volatile WaitlessDatabase INSTANCE;
    public abstract VenueDao venueDao();
    public abstract BookingDao bookingDao();
    public abstract ClientDao clientDao();
    public abstract RestaurantDao restaurantDao();

    public static WaitlessDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,
                    WaitlessDatabase.class,
                    "database").fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){ INSTANCE = null; }
}
