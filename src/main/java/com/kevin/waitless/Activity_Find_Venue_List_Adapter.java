package com.kevin.waitless;

import android.content.Context;
import android.databinding.DataBindingUtil;
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

        List<Venue> mData;
        List<Venue> mStringFilterList;
        ValueFilter valueFilter;
        private LayoutInflater inflater;

        public Activity_Find_Venue_List_Adapter(List<Venue> cancel_type) {
            mData = cancel_type;
            mStringFilterList = cancel_type;
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Venue getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            if (inflater == null) {
                inflater = (LayoutInflater) parent.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            ActivityFindVenueRowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater,
                                                        R.layout.activity_find_venue_row_item,
                                                        parent, false);
            rowItemBinding.stringName.setText(mData.get(position).getName());
            rowItemBinding.stringAddress.setText(mData.get(position).getAddress());

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
                        if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
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
                mData = (List<Venue>) results.values;
                notifyDataSetChanged();
            }


        }
        public void refresh(List<Venue> venues) {
            this.mData.clear();
            this.mData.addAll(venues);
            notifyDataSetChanged();
        }
}

