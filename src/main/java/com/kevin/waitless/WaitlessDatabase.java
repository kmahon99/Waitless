package com.kevin.waitless;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * SQLLite table to cache venues searched by client
 */
public class WaitlessDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "waitless_database.db";
    public static final String TABLE_NAME_VENUES = "Table_Venues";
    public static final String COLUMN_VENUES_ID = "Venue_ID";
    public static final String COLUMN_VENUES_NAME = "Venue_Name";
    public static final String COLUMN_VENUES_ADDRESS = "Venue_Address";

    public enum SearchColumn{
        VENUE_NAME,
        VENUE_ADDRESS
    }

    /**
     * Creates a new database object to store venues and clients
     * @param context The current context of the invoker
     * @param name Set to anything, the database file name is fixed to "waitless_database.db"
     * @param factory ??
     * @param version Set to anything, the database version is fixed to V1
     */
    public WaitlessDatabase(Context context,
                            String name,
                            SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, NAME, factory, VERSION);
        SQLiteDatabase db = getWritableDatabase();
        if(db != null) {
            db.delete(TABLE_NAME_VENUES, null, null);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME_VENUES + "(" +
                COLUMN_VENUES_ID + " TEXT PRIMARY KEY, " +
                COLUMN_VENUES_NAME+" TEXT, "+
                COLUMN_VENUES_ADDRESS+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VENUES);
        onCreate(db);
    }

    /**
     * Add a venue item to the database of cached venues for this device
     * @param venue A venue object
     */
    public void addRow(Venue venue){
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        //Always extract hashes from SystemID class when storing to remove redundancy
        //Log.d("Activity_Find_Venue",gson.toJson(venue.getSystem_id().getID()));
        values.put(COLUMN_VENUES_ID, gson.toJson(venue.getSystem_id().getID()));
        values.put(COLUMN_VENUES_NAME, venue.getName());
        values.put(COLUMN_VENUES_ADDRESS, venue.getAddress());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME_VENUES,null,values);
    }

    public void deleteRow(String venue_name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME_VENUES+" WHERE "+COLUMN_VENUES_NAME+" = "+
                "\""+venue_name+"\";");
    }

    /**
     * Finds the best match cached venue in the server based off the venue name
     * @param venue_name The name of the venue (or partial name)
     * @return A set of the most appropriate results, may be empty if no match found
     */
    public ArrayList<Venue> getVenue(String venue_name, SearchColumn col){
        SQLiteDatabase db = getWritableDatabase();
        Gson gson = new Gson();
        ArrayList<Venue> venues = new ArrayList<Venue>();
        String column;
        switch(col){
            case VENUE_NAME: column = COLUMN_VENUES_NAME;
            case VENUE_ADDRESS: column = COLUMN_VENUES_ADDRESS;
            default: column = COLUMN_VENUES_NAME;
        }
        Cursor cursor = db.rawQuery("SELECT * " +
                "FROM "+TABLE_NAME_VENUES+
                " WHERE "+column+" LIKE ? " +
                "ORDER BY " +
                "  CASE " +
                "    WHEN "+column+" LIKE ? THEN 1 " +
                "    WHEN "+column+" LIKE ? THEN 3 " +
                "    ELSE 2 " +
                "  END",new String[] {'%'+venue_name+'%',venue_name+'%','%'+venue_name});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            if(cursor.getString(cursor.getColumnIndex("Venue_ID")) != null){
                Venue venue = new Venue();
                SystemID ID = new SystemID();
                TypeToken<ArrayList<byte[]>> token = new TypeToken<ArrayList<byte[]>>(){};
                ArrayList<byte[]> list = gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_ID)),token.getType());
                venue.setSystem_id(new SystemID(list));
                venue.setName(cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_NAME)));
                venue.setAddress(cursor.getString((cursor.getColumnIndex(COLUMN_VENUES_ADDRESS))));
                venues.add(venue);
            }
            cursor.moveToNext();
        }
        db.close();
        return venues;
    }

    public String toString(){
        String s = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_VENUES+" WHERE 1";
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            if(cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_ID)) != null){
                s += cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_ID)) + "\n";
                s += cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_NAME)) + "\n";
                s += cursor.getString(cursor.getColumnIndex(COLUMN_VENUES_ADDRESS)) + "\n";
            }
            cursor.moveToNext();
        }
        db.close();
        return s;
    }
}
