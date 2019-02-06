package com.kevin.waitless;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kevin.waitless.databinding.ActivityManageBookingsBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Activity_Manage_Bookings extends WaitlessActivity{

    private static final String TAG = "Activity_Bookings";

    private ActivityManageBookingsBinding activityManageBookingsBinding;
    private Activity_Manage_Bookings_Recycler_Adapter adapter;
    HashMap<String,Booking> bookings = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVenue));
        }
        List<Booking> list = new ArrayList<>();
        for(String key : bookings.keySet()){ list.add(bookings.get(key)); }
        adapter = new Activity_Manage_Bookings_Recycler_Adapter(list,this);
        activityManageBookingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_bookings);
        activityManageBookingsBinding.manageBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityManageBookingsBinding.manageBookingsRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        activityManageBookingsBinding.manageBookingsRecyclerView.setAdapter(adapter);
        Fragment_Venue_Selector selector = addVenueSelector();
        selector.setOnSelectedVenuesChangeListener(new Fragment_Venue_Selector.OnSelectedVenuesChangeListener() {
            @Override
            public void onChange(HashSet<String> venues) {
                for(String venue : venues)
                    new Activity_Manage_Bookings.getBookings(Activity_Manage_Bookings.this,adapter,venue).execute();
            }
        });
    }

    private static class getBookings extends AsyncTask<Void,Void,List<Booking>>{

        private WeakReference<Activity_Manage_Bookings> activity_manage_bookingsWeakReference;
        private Activity_Manage_Bookings_Recycler_Adapter adapter;
        private BookingDao bookingDao;
        private String venue_id;

        getBookings(Activity_Manage_Bookings activity, Activity_Manage_Bookings_Recycler_Adapter adapter, String venue_id){
            this.activity_manage_bookingsWeakReference = new WeakReference<>(activity);
            this.bookingDao = WaitlessDatabase.getDatabase(activity_manage_bookingsWeakReference.get().getApplicationContext()).bookingDao();
            this.adapter = adapter;
            this.venue_id = venue_id;
        }

        @Override
        protected List<Booking> doInBackground(Void... voids) {
            if(activity_manage_bookingsWeakReference != null){
                List<Booking> bookings = this.bookingDao.getBookingsByVenue(venue_id);
                return bookings;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Booking> bookings) {
            if(bookings != null) {
                Toast.makeText(activity_manage_bookingsWeakReference.get(),"Got: "+venue_id,Toast.LENGTH_SHORT).show();
                adapter.refresh(bookings);
                activity_manage_bookingsWeakReference.get().activityManageBookingsBinding.manageBookingsSummary.setText(""+bookings.size());
            }
        }
    }
}
