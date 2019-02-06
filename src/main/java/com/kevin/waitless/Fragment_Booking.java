package com.kevin.waitless;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;

public class Fragment_Booking extends DialogFragment{

    Activity_Bookings_Recycler_Adapter adapter;
    int position;

    private static final String TAG = "Fragment_Booking";

    Fragment_Booking(Activity_Bookings_Recycler_Adapter adapter,int position){
        this.position = position;
        this.adapter = adapter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.booking_item_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        ((TextView)view.findViewById(R.id.booking_time)).setText(adapter.getItem(position).getHour()+":"+adapter.getItem(position).getMin());
        String s = ""; int c = Color.parseColor("#ffffff");
        switch(adapter.getItem(position).getBooking_status()){
            case PENDING:
                s = "Pending"; c = Color.parseColor("#ff0000"); break;
            case CONFIRMED              :
                s = "Confirmed"; c = R.color.colorPrimaryDark;             break;
            case DECLINED:
                s = "Declined"; c = Color.parseColor("#ff0000"); break;
            case CANCELLED:
                s = "Cancelled"; c = Color.parseColor("#ff0000"); break;
        }
        ((TextView)view.findViewById(R.id.booking_status)).setText(s);
        ((TextView)view.findViewById(R.id.booking_status)).setTextColor(c);
        ((TextView)view.findViewById(R.id.booking_date)).setText(adapter.getItem(position).getDay()+" "+
                new DateFormatSymbols().getMonths()[adapter.getItem(position).getMonth()]+" "+adapter.getItem(position).getYear());
        view.findViewById(R.id.booking_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_Venue.class);
                intent.putExtra("venue_id", adapter.getItem(position).getVenue_id());
                v.getContext().startActivity(intent);
            }
        });
        view.findViewById(R.id.booking_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                new remove(adapter,getActivity(),position).execute();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    private void close(){
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(Fragment_Booking.this).commit();
    }

    private static class remove extends AsyncTask<Void,Void,Void> {

        WeakReference<FragmentActivity> activityWeakReference; int position;
        Activity_Bookings_Recycler_Adapter adapter;

        remove(Activity_Bookings_Recycler_Adapter adapter, FragmentActivity activity, int position){
            activityWeakReference = new WeakReference<>(activity);
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (activityWeakReference != null) {
                BookingDao bookingDao = WaitlessDatabase.getDatabase(activityWeakReference.get()).bookingDao();
                bookingDao.delete(adapter.getBookings().get(position));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(activityWeakReference != null){
                adapter.removeItem(position);
            }
        }
    }
}
