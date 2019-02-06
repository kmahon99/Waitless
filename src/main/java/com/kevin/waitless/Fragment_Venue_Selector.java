package com.kevin.waitless;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.kevin.waitless.databinding.FragmentVenueSelectorBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Fragment_Venue_Selector extends Fragment {

    private FloatingActionButton button;
    private Fragment_Venue_Selector_Adapter adapter;
    private ArrayList<String> venue_names;
    private ArrayList<String> venue_ids;
    private HashMap<String,String> venues;
    private HashSet<String> selected_venues;

    private OnSelectedVenuesChangeListener listener;

    public interface OnSelectedVenuesChangeListener{
        void onChange(HashSet<String> venues);
    }

    public Fragment_Venue_Selector(){
        venue_names = new ArrayList<>();
        venue_ids = new ArrayList<>();
        selected_venues = new HashSet<>();
        venues = WaitlessActivity.USER.getStaffed_venues();
        for(String key : venues.keySet()){
            venue_names.add(venues.get(key));
            venue_ids.add(key);
        }
        this.listener = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater,container,saveInstanceState);
        adapter = new Fragment_Venue_Selector_Adapter();
        FragmentVenueSelectorBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_venue_selector, container, false);
        View view = binding.getRoot();
        binding.venueSelectorList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = getActivity().findViewById(R.id.venue_selector);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                boolean[] checked = new boolean[venue_ids.size()];
                for(int i = 0; i < checked.length; i++){
                    if(selected_venues.contains(venue_ids.get(i))){
                        checked[i] = true;
                    }else{ checked[i] = false; }
                }
                adb.setMultiChoiceItems(venue_names.toArray(new String[0]), checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            selected_venues.add(venue_ids.get(which));
                        }else{
                            selected_venues.remove(venue_ids.get(which));
                        }
                        if(listener != null) {
                            listener.onChange(selected_venues);
                        }
                    }
                });
                adb.show();
            }
        });
    }

    public void setOnSelectedVenuesChangeListener(OnSelectedVenuesChangeListener listener){ this.listener = listener; }
}
