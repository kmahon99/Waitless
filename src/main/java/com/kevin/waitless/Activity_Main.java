package com.kevin.waitless;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_ACCOUNTS = 1;
    private static final String TAG = "Activity_Main";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Client","Accounts permission denied");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_READ_ACCOUNTS);
        }
        else{
            Setup();
        }
    }

    private void Setup(){
        final Button button = (Button) findViewById(R.id.button_find_venue);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Added listener to Find Venue");
                Intent intent = new Intent(v.getContext(), Activity_Find_Venue.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_ACCOUNTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Client","Account access enabled");
                    Setup();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Waitless needs your account information to log you in!",
                            Toast.LENGTH_LONG);
                }
            }
        }
    }
}
