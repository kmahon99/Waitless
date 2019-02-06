package com.kevin.waitless;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.kevin.waitless.databinding.ActivityFindVenueBinding;

import androidx.appcompat.widget.SearchView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Activity_Find_Venue extends WaitlessActivity {

    ActivityFindVenueBinding activityFindVenueBinding;
    Activity_Find_Venue_List_Adapter adapter;
    ArrayList<Venue> venues = new ArrayList<>();

    private static final String TAG = "Activity_Find_Venue";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_venue);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }

        new deleteVenues(Activity_Find_Venue.this,"Restaurant").execute();

        new insertVenues(Activity_Find_Venue.this,venues).execute();

        adapter = new Activity_Find_Venue_List_Adapter(venues);
        activityFindVenueBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_venue);

        adapter = new Activity_Find_Venue_List_Adapter(new ArrayList<Venue>());
        activityFindVenueBinding.venueListView.setAdapter(adapter);

        activityFindVenueBinding.search.setActivated(true);
        activityFindVenueBinding.search.setQueryHint("Find a venue");
        activityFindVenueBinding.search.onActionViewExpanded();
        activityFindVenueBinding.search.setIconified(false);
        activityFindVenueBinding.search.clearFocus();
        activityFindVenueBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.compareTo("") == 0){ adapter.refresh(new ArrayList<Venue>()); }
                else {
                    new getVenues(Activity_Find_Venue.this, adapter, query).execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.compareTo("") == 0){ adapter.refresh(new ArrayList<Venue>()); }
                else {
                    new getVenues(Activity_Find_Venue.this, adapter, newText).execute();
                }
                return false;
            }
        });
    }

    private static class insertVenues extends AsyncTask<Void,Void,Void>{

        private WeakReference<Activity> weakReference;
        private VenueDao venueDao;
        private List<Venue> venues;

        public insertVenues(Activity activity, List<Venue> venues){
            this.venues = venues;
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(weakReference.get() != null){
                this.venueDao = WaitlessDatabase.getDatabase(weakReference.get().getApplicationContext()).venueDao();
                for(Venue venue : venues){
                    venueDao.insertVenue(venue);
                    Log.d(TAG,"Inserted: "+venue.getName());
                }
            }
            return null;
        }
    }

    private static class getVenues extends AsyncTask<Void,Void,List<Venue>>{

        private WeakReference<Activity> weakReference;
        private VenueDao venueDao;
        private Activity_Find_Venue_List_Adapter adapter;
        private String param;

        public getVenues(Activity activity,Activity_Find_Venue_List_Adapter adapter, String query){
            weakReference = new WeakReference<>(activity);
            this.adapter = adapter;
            this.venueDao = WaitlessDatabase.getDatabase(weakReference.get().getApplicationContext()).venueDao();
            this.param = query;
        }

        @Override
        protected List<Venue> doInBackground(Void... voids) {
            List<Venue> venues = new ArrayList<>();
            if(weakReference.get() != null && adapter != null && param != null){
                venues = venueDao.getVenuesByName(param);
            }
            return venues;
        }

        @Override
        protected void onPostExecute(List<Venue> venues) {
            adapter.refresh(venues);
        }
    }

    private static class deleteVenues extends AsyncTask<Void,Void,Void>{

        private WeakReference<Activity> weakReference;
        private VenueDao venueDao;
        private String param;

        deleteVenues(Activity activity, String query){
            weakReference = new WeakReference<>(activity);
            this.venueDao = WaitlessDatabase.getDatabase(weakReference.get().getApplicationContext()).venueDao();
            this.param = query;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(weakReference.get() != null && param != null){
                venueDao.deleteVenue(param);
            }
            return null;
        }
    }
}
