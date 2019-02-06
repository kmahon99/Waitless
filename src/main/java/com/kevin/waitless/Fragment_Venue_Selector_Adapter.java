package com.kevin.waitless;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kevin.waitless.databinding.FragmentVenueSelectorRowItemBinding;

import java.util.ArrayList;

public class Fragment_Venue_Selector_Adapter extends RecyclerView.Adapter<Fragment_Venue_Selector_Adapter.ViewHolder>{

    private ArrayList<String> venues;

    public Fragment_Venue_Selector_Adapter(){
        venues = new ArrayList<>();
        for(String key : WaitlessActivity.USER.getStaffed_venues().keySet()){
            venues.add(WaitlessActivity.USER.getStaffed_venues().get(key));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        FragmentVenueSelectorRowItemBinding venueSelectorRowItemBinding;

        ViewHolder(FragmentVenueSelectorRowItemBinding venueSelectorRowItemBinding) {
            super(venueSelectorRowItemBinding.getRoot());
            this.venueSelectorRowItemBinding = venueSelectorRowItemBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentVenueSelectorRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.fragment_venue_selector_row_item, parent, false);
        return new Fragment_Venue_Selector_Adapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Fragment_Venue_Selector_Adapter.ViewHolder holder, int position) {
        holder.venueSelectorRowItemBinding.textView.setText(venues.get(position));
    }

    @Override
    public int getItemCount() {
        return WaitlessActivity.USER.getStaffed_venues().size();
    }
}
