package com.kevin.waitless;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kevin.waitless.databinding.ActivityBookingsRowItemBinding;

import java.lang.ref.WeakReference;
import java.util.List;

public class Activity_Bookings_Recycler_Adapter extends
        RecyclerView.Adapter<Activity_Bookings_Recycler_Adapter.ViewHolder>{

    private List<Booking> bookings;
    private Activity activity;
    private static final String TAG = "Activity_Bookings_Adapt";

    public Activity_Bookings_Recycler_Adapter(List<Booking> bookings, Activity activity){
        this.bookings = bookings;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ActivityBookingsRowItemBinding bookingsRowItemBinding;

        public ViewHolder(ActivityBookingsRowItemBinding bookingsRowItemBinding) {
            super(bookingsRowItemBinding.getRoot());
            this.bookingsRowItemBinding = bookingsRowItemBinding;
        }
    }

    @Override
    public Activity_Bookings_Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        ActivityBookingsRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.activity_bookings_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        new setup(holder,activity,bookings.get(position),bookings.get(position).getVenue()).execute();
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public void refresh(List<Booking> bookings) {
        this.bookings.clear();
        this.bookings.addAll(bookings);
        notifyDataSetChanged();
    }

    private static class setup extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity> activityWeakReference;
        List<Venue> venue;
        ViewHolder holder;
        Booking booking;
        long venue_id;

        setup(ViewHolder holder, Activity activity, Booking booking, long venue_id){
            activityWeakReference = new WeakReference<>(activity);
            this.booking = booking;
            this.holder = holder;
            this.venue_id = venue_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activityWeakReference != null) {
                Log.d(TAG,"Searching for: "+venue_id);
                venue = WaitlessDatabase.getDatabase(activityWeakReference.get()).venueDao().getVenueById(venue_id);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(venue != null && venue.size() != 0){
                holder.bookingsRowItemBinding.stringVenue.setText(venue.get(0).getName());
                holder.bookingsRowItemBinding.stringDateTime.setText(booking.getDay()+"/"+
                        booking.getMonth()+1+"/"+booking.getYear()+
                        " @ "+booking.getHour()+":"+booking.getMin());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Activity_Venue.class);
                        intent.putExtra("venue_id", venue.get(0).getVenue_id());
                        v.getContext().startActivity(intent);
                    }
                });
            }
            else{
                Log.d(TAG,"Got venues: "+venue.size());
            }
            super.onPostExecute(aVoid);
        }
    }
}