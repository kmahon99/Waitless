package com.kevin.waitless;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kevin.waitless.databinding.ActivityBookingsBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Activity_Bookings extends Activity {

    private static final String TAG = "Activity_Bookings";

    private ActivityBookingsBinding activityBookingsBinding;
    private Activity_Bookings_Recycler_Adapter adapter;
    ArrayList<Booking> bookings = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        adapter = new Activity_Bookings_Recycler_Adapter(bookings,this);
        activityBookingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_bookings);
        activityBookingsBinding.bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityBookingsBinding.bookingRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        activityBookingsBinding.bookingRecyclerView.setAdapter(adapter);
        new getBookings(this, adapter).execute();
    }

    private static class getBookings extends AsyncTask<Void,Void,List<Booking>>{

        private WeakReference<Activity> weakReference;
        private Activity_Bookings_Recycler_Adapter adapter;
        private BookingDao bookingDao;

        getBookings(Activity activity, Activity_Bookings_Recycler_Adapter adapter){
            this.weakReference = new WeakReference<>(activity);
            this.bookingDao = WaitlessDatabase.getDatabase(weakReference.get().getApplicationContext()).bookingDao();
            this.adapter = adapter;
        }

        @Override
        protected List<Booking> doInBackground(Void... voids) {
            if(weakReference != null){
                List<Booking> bookings = this.bookingDao.getAllBookings();
                return bookings;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Booking> bookings) {
            if(bookings != null) {
                Log.d(TAG,"Got: "+bookings.size());
                adapter.refresh(bookings);
            }
        }
    }
}
