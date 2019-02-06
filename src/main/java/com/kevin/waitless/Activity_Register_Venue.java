package com.kevin.waitless;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.lang.ref.WeakReference;

public class Activity_Register_Venue extends WaitlessActivity {

    private static Place place;

    private static final String TAG = "Activity_Reg_Venue";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_venue);
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.dialog_create_account_place);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Activity_Register_Venue.place = place;
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG,"Place Selection Error");
            }
        });
        findViewById(R.id.dialog_create_account_place_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(place != null){
                    new storeVenue(Activity_Register_Venue.this,place).execute();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVenue));
        }
    }

    private static class storeVenue extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity_Register_Venue> activity_register_venueWeakReference;
        Venue venue;

        public storeVenue(Activity_Register_Venue activity_register_venue, Place place){
            activity_register_venueWeakReference = new WeakReference<>(activity_register_venue);
            venue = new Venue(place.getId(),place.getName().toString(),place.getAddress().toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity_register_venueWeakReference != null && venue != null){
                WaitlessDatabase.getDatabase(activity_register_venueWeakReference.get()).venueDao().insertVenue(venue);
                WaitlessActivity.USER.addStaffed_venue(venue);
                WaitlessDatabase.getDatabase(activity_register_venueWeakReference.get()).clientDao().updateUser(WaitlessActivity.USER);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(activity_register_venueWeakReference != null) {
                Toast.makeText(activity_register_venueWeakReference.get(),"Business sucessfully registered: "+venue.getVenue_id(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity_register_venueWeakReference.get(),Activity_Main.class);
                activity_register_venueWeakReference.get().startActivity(intent);
            }
        }
    }
}
