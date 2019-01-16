package com.kevin.waitless;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kevin.waitless.databinding.ActivityBookingsRowItemBinding;

import java.lang.ref.WeakReference;
import java.util.List;

public class Activity_Bookings_Recycler_Adapter extends
        RecyclerView.Adapter<Activity_Bookings_Recycler_Adapter.ViewHolder>{

    private static List<Booking> bookings;
    private FragmentActivity activity;
    private static Fragment_Booking fragment_booking;
    private static int frag_count = 0;
    private static final String TAG = "Activity_Bookings_Adapt";

    public Activity_Bookings_Recycler_Adapter(List<Booking> bookings, FragmentActivity activity){
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
        new setup(holder,this,activity,position,bookings.get(position).getVenue()).execute();
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public Booking getItem(int position) {
        return bookings.get(position);
    }

    public List<Booking> getBookings(){ return bookings; }

    public Booking removeItem(int position){
        Booking booking = bookings.remove(position);
        refresh(this.bookings);
        return booking;
    }

    public void refresh(List<Booking> bookings) {
        this.bookings.clear();
        this.bookings.addAll(bookings);
        notifyDataSetChanged();
    }

    private static class setup extends AsyncTask<Void,Void,Void>{

        WeakReference<FragmentActivity> activityWeakReference;
        List<Venue> venue;
        ViewHolder holder;
        Booking booking;
        Activity_Bookings_Recycler_Adapter adapter;
        long venue_id; int position;

        setup(ViewHolder holder, Activity_Bookings_Recycler_Adapter adapter, FragmentActivity activity, int position, long venue_id){
            activityWeakReference = new WeakReference<>(activity);
            this.adapter = adapter;
            this.booking = adapter.getItem(position);
            this.holder = holder;
            this.venue_id = venue_id;
            this.position = position;
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
                holder.bookingsRowItemBinding.stringStatus.setText("Pending...");
                holder.bookingsRowItemBinding.stringStatus.setTextColor(Color.parseColor("#FF0000"));
                holder.bookingsRowItemBinding.stringDateTime.setText(booking.getDay()+"/"+
                        booking.getMonth()+1+"/"+booking.getYear()+
                        " @ "+booking.getHour()+":"+booking.getMin());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextView vn = holder.itemView.findViewById(R.id.stringVenue);
                        final TextView dt = holder.itemView.findViewById(R.id.stringDateTime);
                        final TextView s = holder.itemView.findViewById(R.id.stringStatus);
                        holder.itemView.setClickable(false);
                        vn.setVisibility(View.GONE);
                        dt.setVisibility(View.GONE);
                        s.setVisibility(View.GONE);
                        fragment_booking = new Fragment_Booking(adapter,position);
                        final FragmentManager manager = activityWeakReference.get().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.addToBackStack("stack_booking");
                        transaction.add(R.id.booking_item,fragment_booking);
                        transaction.commit();
                        manager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                            @Override
                            public void onBackStackChanged() {
                                if(manager.getBackStackEntryCount() < frag_count) {
                                    vn.setVisibility(View.VISIBLE);
                                    dt.setVisibility(View.VISIBLE);
                                    s.setVisibility(View.VISIBLE);
                                    holder.itemView.setClickable(true);
                                }
                                frag_count = manager.getBackStackEntryCount();
                            }
                        });
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