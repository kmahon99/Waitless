package com.kevin.waitless;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashSet;

/**
 * Activity to extend if you want a menu in the top right corner
 */
public class WaitlessActivity extends AppCompatActivity {

    protected Fragment_Menu menu;
    protected Fragment_Venue_Selector selector;
    protected static User USER; //The current user of the app, set in Activity_Login

    private static final String TAG = "Waitless_Activity";

    WaitlessActivity(){
        if(USER == null){
            Log.d("Main","Initialised");
            USER = new User(); }
    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    protected void addMenu() {
        menu = new Fragment_Menu();
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(android.R.id.content,menu).commit();
    }

    protected void removeMenu(){
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(menu).commit();
    }

    protected Fragment_Venue_Selector addVenueSelector(){
        selector = new Fragment_Venue_Selector();
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(android.R.id.content,selector);
        transaction.commit();
        return selector;
    }
}
