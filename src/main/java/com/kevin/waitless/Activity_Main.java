package com.kevin.waitless;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;

public class Activity_Main extends WaitlessActivity {

    Button button_find_venue;
    Button button_bookings;
    Button button_register_venue;
    Button button_manage_bookings;

    private static final String TAG = "Activity_Main";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMenu();
        setup();
    }

    private void setup(){
        button_find_venue = findViewById(R.id.button_find_venue);
        button_bookings = findViewById(R.id.button_bookings);
        button_register_venue = findViewById(R.id.button_register_venue);
        button_manage_bookings = findViewById(R.id.button_manage_bookings);

        button_find_venue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Find_Venue.class);
                v.getContext().startActivity(intent);
            }
        });
        button_bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Activity_Bookings.class);
                v.getContext().startActivity(intent);
            }
        });
        button_register_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Activity_Register_Venue.class);
                v.getContext().startActivity(intent);
            }
        });
        button_manage_bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Activity_Manage_Bookings.class);
                v.getContext().startActivity(intent);
            }
        });
        setMenu();
    }

    private void setMenu(){
        if(super.menu.getAppState() == Fragment_Menu.state.CLIENT) {
            setMenuState(View.VISIBLE,View.GONE);
        }else{
            setMenuState(View.GONE,View.VISIBLE);
        }
        menu.addOnAppStateChangedListener(new Fragment_Menu.OnAppStateChangeListener() {
            @Override
            public void onStateChanged(Fragment_Menu.state s) {
                if(s == Fragment_Menu.state.CLIENT){
                    setMenuState(View.VISIBLE,View.GONE);
                }else{
                    setMenuState(View.GONE,View.VISIBLE);
                }
            }
        });
    }

    private void setMenuState(int client_vis, int venue_vis){
        button_find_venue.setVisibility(client_vis);
        button_bookings.setVisibility(client_vis);
        button_register_venue.setVisibility(venue_vis);
        button_manage_bookings.setVisibility(venue_vis);
    }
}
