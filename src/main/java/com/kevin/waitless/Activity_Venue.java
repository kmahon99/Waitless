package com.kevin.waitless;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.List;

public class Activity_Venue extends FragmentActivity {

    private static final String TAG = "Activity_Venue";
    private static Venue venue;
    private static int frag_count = 0;
    private static Fragment_Request_Reservation fragment_make_reservation;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);
        Intent intent = getIntent();
        final String query = intent.getStringExtra("venue_id");
        new getReferencedVenue(Activity_Venue.this,query).execute();
        Button button_make_reservation = findViewById(R.id.button_make_booking);
        button_make_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(((Button)v).getText().toString()){
                    case "REQUEST A BOOKING":
                        if(venue != null){
                            addFragment(R.id.venue_menu, fragment_make_reservation);
                        }
                }
            }
        });
        ((LinearLayout)findViewById(R.id.activity_venue)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        ((LinearLayout)findViewById(R.id.venue_header)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }

    private void addFragment(int container, Fragment fragment){
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("stack_booking");
        transaction.add(container,fragment);
        transaction.commit();
        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(manager.getBackStackEntryCount() < frag_count) {
                    onFragmentRemoval();
                }
                frag_count = manager.getBackStackEntryCount();
            }
        });
        LinearLayout v = findViewById(R.id.venue_menu_buttons);
        v.setVisibility(View.GONE);
        LinearLayout header = findViewById(R.id.venue_header);
        header.setLayoutParams(new LinearLayout.LayoutParams(header.getWidth(),header.getHeight()/2));
        ((LinearLayout)findViewById(R.id.venue_type_container)).getLayoutTransition().enableTransitionType(LayoutTransition.DISAPPEARING);
        (findViewById(R.id.venue_type_container)).setVisibility(View.GONE);
    }

    private void onFragmentRemoval(){
        LinearLayout v = findViewById(R.id.venue_menu_buttons);
        v.setLayoutParams(new LinearLayout.LayoutParams(v.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        v.setVisibility(View.VISIBLE);
        LinearLayout header = findViewById(R.id.venue_header);
        header.setLayoutParams(new LinearLayout.LayoutParams(header.getWidth(),header.getHeight()*2));
        LinearLayout icon_container = findViewById(R.id.venue_type_container);
        icon_container.setVisibility(View.VISIBLE);
        icon_container.setLayoutParams(new LinearLayout.LayoutParams(icon_container.getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout)findViewById(R.id.venue_type_container)).getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
        (findViewById(R.id.venue_type_container)).setVisibility(View.VISIBLE);
    }

    private static class getReferencedVenue extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity_Venue> activityWeakReference;
        VenueDao venueDao;
        List<Venue> result;
        String id;

        getReferencedVenue(Activity_Venue activity, String venue_id){
            activityWeakReference = new WeakReference<>(activity);
            id = venue_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activityWeakReference != null && id != null){
                venueDao = WaitlessDatabase.getDatabase(activityWeakReference.get()).venueDao();
                result = venueDao.getVenueById(id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(activityWeakReference != null && result.size() == 1) {
                ((TextView)activityWeakReference.get().findViewById(R.id.venue_name)).setText(result.get(0).getName());
                venue = result.get(0);
                fragment_make_reservation = new Fragment_Request_Reservation(venue);
            }
            else{ Log.d(TAG,"Expected 1 venue result, got: "+result.size()); }
        }
    }
}
