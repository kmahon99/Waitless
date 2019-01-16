package com.kevin.waitless;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;

public class Fragment_Request_Reservation extends DialogFragment {

    private int booking_hour,booking_min;
    private int booking_year, booking_month, booking_day;

    private Venue venue;
    private Client client;

    private static final String TAG = "FRAG_REQ_RES";

    public Fragment_Request_Reservation(Venue venue){
        this.venue = venue;
        this.client = new Client();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final TimePicker timePicker = getView().findViewById(R.id.make_reservation_time);
        timePicker.setIs24HourView(true);
        booking_hour = booking_min = booking_year = booking_month = booking_day = -1;
        booking_hour = timePicker.getHour();
        booking_min = timePicker.getMinute();
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                booking_hour = hourOfDay;
                booking_min = minute;
            }
        });
        final CalendarView calendarView = getView().findViewById(R.id.make_reservation_caleandar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                booking_year = year;
                booking_month = month;
                booking_day = dayOfMonth;
            }
        });
        Button submit = getView().findViewById(R.id.make_reservation_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(booking_day == -1 || booking_hour == -1){
                    Toast.makeText(getContext(),"Choose a later date and time!",Toast.LENGTH_LONG);
                }
                new setBooking(getActivity(),new Booking(booking_year, booking_month, booking_day,
                        booking_hour, booking_min,client.getEmail(),venue.getVenue_id())).execute();
                close();
            }
        });
        RelativeLayout cancel = getActivity().findViewById(R.id.make_reservation_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { close(); }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_request_reservation, container, false);
    }

    private void close(){
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(Fragment_Request_Reservation.this).commit();
    }

    private static class setBooking extends AsyncTask<Void,Void,Void>{

        private WeakReference<Activity> weakReference;
        private Booking booking;
        private BookingDao bookingDao;

        setBooking(Activity activity, Booking booking){
            this.weakReference = new WeakReference<>(activity);
            this.bookingDao = WaitlessDatabase.getDatabase(weakReference.get().getApplicationContext()).bookingDao();
            this.booking = booking;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(weakReference.get() != null && booking != null){
                bookingDao.insert(booking); }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG,"Added booking to DB: "+booking.getYear()+"/"+booking.getMonth()+"/"+booking.getDay()+" "+booking.getHour()+":"+booking.getMin());
        }
    }
}
