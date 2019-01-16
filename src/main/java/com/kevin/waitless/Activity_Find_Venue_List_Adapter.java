package com.kevin.waitless;

import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.List;
import com.kevin.waitless.databinding.ActivityFindVenueRowItemBinding;

public class Activity_Find_Venue_List_Adapter extends BaseAdapter implements Filterable{

        private List<Venue> venues;
        private List<Venue> mStringFilterList;
        private ValueFilter valueFilter;
        private LayoutInflater inflater;

        public Activity_Find_Venue_List_Adapter(List<Venue> cancel_type) {
            venues = cancel_type;
            mStringFilterList = cancel_type;
        }

        @Override
        public int getCount() {
            return venues.size();
        }

        @Override
        public Venue getItem(int position) {
            return venues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (inflater == null) {
                inflater = (LayoutInflater) parent.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            final ActivityFindVenueRowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater,
                                                        R.layout.activity_find_venue_row_item,
                                                        parent, false);
            rowItemBinding.stringName.setText(venues.get(position).getName());
            rowItemBinding.stringAddress.setText(venues.get(position).getAddress());
            rowItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Activity_Venue.class);
                    intent.putExtra("venue_id", venues.get(position).getVenue_id());
                    v.getContext().startActivity(intent);
                }
            });
            return rowItemBinding.getRoot();
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    List<String> filterList = new ArrayList<>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getName().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            filterList.add(mStringFilterList.get(i).getName());
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                venues = (List<Venue>) results.values;
                notifyDataSetChanged();
            }
        }

        public void refresh(List<Venue> venues) {
            this.venues.clear();
            this.venues.addAll(venues);
            notifyDataSetChanged();
        }
}