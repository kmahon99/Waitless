package com.kevin.waitless;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class Activity_Register_Venue extends Activity {

    private static Place place;

    private static final String TAG = "Activity_Reg_Venue";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_venue);
        setup();
    }

    private void setup(){
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.dialog_create_account_place);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Activity_Register_Venue.this.place = place;
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG,"Place Selection Error");
            }
        });
    }
}
