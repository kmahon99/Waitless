package com.kevin.waitless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Fragment_Menu extends Fragment {

    private enum state {CLIENT,VENUE};
    private static state menu_state = state.CLIENT;

    @Override
        public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FloatingActionButton venue_toggle = getActivity().findViewById(R.id.menu_fab_toggle_venue);
        final FloatingActionButton menu = getActivity().findViewById(R.id.menu_fab);
        venue_toggle.hide();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onClickMenu(venue_toggle);
                }
        });
        venue_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVenueToggle(menu,venue_toggle);
            }
        });
    }

    private void onClickMenu(FloatingActionButton venue_toggle){
        if(venue_toggle.getVisibility() != View.VISIBLE){
            venue_toggle.show();
        }else{
            venue_toggle.hide();
        }
    }

    private void onClickVenueToggle(FloatingActionButton menu, FloatingActionButton venue_toggle){
        if(menu_state == state.CLIENT) {
            menu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryVenue)));
            venue_toggle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            menu_state = state.VENUE;
        }else{
            menu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            venue_toggle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryVenue)));
            menu_state = state.CLIENT;
        }
    }
}
