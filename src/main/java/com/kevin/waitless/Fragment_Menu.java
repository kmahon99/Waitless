package com.kevin.waitless;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;

public class Fragment_Menu extends Fragment {

    public enum state {CLIENT,VENUE}
    private static state app_state;
    private FloatingActionButton venue_toggle;
    private FloatingActionButton menu;
    private OnAppStateChangeListener onAppStateChangeListener;

    private static final String TAG = "Fragment_Menu";

    public Fragment_Menu(){
        app_state = state.CLIENT;
        this.onAppStateChangeListener = null;
    }

    public interface OnAppStateChangeListener{
        void onStateChanged(state s);
    }

    public void addOnAppStateChangedListener(OnAppStateChangeListener listener){
        this.onAppStateChangeListener = listener;
    }

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
        venue_toggle = getActivity().findViewById(R.id.menu_fab_toggle_venue);
        menu = getActivity().findViewById(R.id.menu_fab);
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
                if(getAppState() == state.CLIENT){
                    menu.setBackgroundTintList(ColorStateList.valueOf(Fragment_Menu.this.getResources().getColor(R.color.colorPrimaryVenue)));
                    venue_toggle.setBackgroundTintList(ColorStateList.valueOf(Fragment_Menu.this.getResources().getColor(R.color.colorPrimaryDark)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryVenue));
                    }
                }
                else{
                    menu.setBackgroundTintList(ColorStateList.valueOf(Fragment_Menu.this.getResources().getColor(R.color.colorPrimaryDark)));
                    venue_toggle.setBackgroundTintList(ColorStateList.valueOf(Fragment_Menu.this.getResources().getColor(R.color.colorPrimaryVenue)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
                new changeAppState(Fragment_Menu.this).execute();
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

    public void setAppState(state s){ app_state = s; }

    public state getAppState(){ return app_state; }

    private static class changeAppState extends AsyncTask<Void,Void,Void>{

        WeakReference<Fragment_Menu> fragment_menuWeakReference;

        public changeAppState(Fragment_Menu fragment){
            fragment_menuWeakReference = new WeakReference(fragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(fragment_menuWeakReference != null){
                if(fragment_menuWeakReference.get().getAppState() == state.CLIENT) {
                   fragment_menuWeakReference.get().setAppState(state.VENUE);
                }else{
                    fragment_menuWeakReference.get().setAppState(state.CLIENT);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(fragment_menuWeakReference != null && fragment_menuWeakReference.get().onAppStateChangeListener != null){
                fragment_menuWeakReference.get().onAppStateChangeListener.onStateChanged(fragment_menuWeakReference.get().getAppState());
            }
        }


    }
}
