package com.kevin.waitless;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kevin.waitless.databinding.ActivityManageBookingsRowItemBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity_Manage_Bookings_Recycler_Adapter extends
        RecyclerView.Adapter<Activity_Manage_Bookings_Recycler_Adapter.ViewHolder> {

    private static List<Booking> bookings;
    private FragmentActivity activity;
    private static final String TAG = "Activity_Bookings_Adapt";

    Activity_Manage_Bookings_Recycler_Adapter(List<Booking> bookings, FragmentActivity activity){
        Activity_Manage_Bookings_Recycler_Adapter.bookings = bookings;
        this.activity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ActivityManageBookingsRowItemBinding manageBookingsRowItemBinding;

        ViewHolder(ActivityManageBookingsRowItemBinding bookingsRowItemBinding) {
            super(bookingsRowItemBinding.getRoot());
            this.manageBookingsRowItemBinding = bookingsRowItemBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityManageBookingsRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.activity_manage_bookings_row_item, parent, false);
        Activity_Manage_Bookings_Recycler_Adapter.ViewHolder viewHolder = new Activity_Manage_Bookings_Recycler_Adapter.ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Activity_Manage_Bookings_Recycler_Adapter.ViewHolder holder, int position) {
        holder.manageBookingsRowItemBinding.stringVenue.setText(bookings.get(position).getUser());
        holder.manageBookingsRowItemBinding.stringStatus.setText("Pending...");
        holder.manageBookingsRowItemBinding.stringStatus.setTextColor(Color.parseColor("#FF0000"));
        holder.manageBookingsRowItemBinding.stringDateTime.setText(bookings.get(position).getDay()+"/"+
                bookings.get(position).getMonth()+1+"/"+bookings.get(position).getYear()+
                " @ "+bookings.get(position).getHour()+":"+bookings.get(position).getMin());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public Booking getItem(int postition) {
        return bookings.get(postition);
    }

    public List<Booking> getBookings(){ return bookings; }

    public Booking removeItem(int position){
        Booking booking = bookings.remove(position);
        refresh(Activity_Manage_Bookings_Recycler_Adapter.bookings);
        return booking;
    }

    public void refresh(List<Booking> bookings) {
        Activity_Manage_Bookings_Recycler_Adapter.bookings.clear();
        Activity_Manage_Bookings_Recycler_Adapter.bookings.addAll(bookings);
        notifyDataSetChanged();
    }
}