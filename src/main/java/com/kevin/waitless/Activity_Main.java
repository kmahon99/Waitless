package com.kevin.waitless;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Activity_Main extends AppCompatActivity {

    private static final String TAG = "Activity_Main";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup(){
        findViewById(R.id.button_find_venue).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Find_Venue.class);
                v.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.button_bookings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Activity_Bookings.class);
                v.getContext().startActivity(intent);
            }
        });
        findViewById(R.id.button_register_venue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Activity_Register_Venue.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}
