package com.kevin.waitless;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Activity_Login extends FragmentActivity {

    private static WaitlessDatabase database;
    private Place place;
    private static final int MY_PERMISSIONS_REQUEST_READ_ACCOUNTS = 1;
    private static final int REQUEST_CODE_PICK_ACCOUNT = 2;
    private final static int PLACE_PICKER_REQUEST = 1;

    private static final String TAG = "Activity_Login";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        new setup(this).execute();
    }

    private void createLoginDialog(){
        final Button create_account = findViewById(R.id.button_create_account);
        database = WaitlessDatabase.getDatabase(getApplicationContext());
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AccountManager.newChooseAccountIntent(null, null,
                        new String[] {"com.google", "com.google.android.legacyimap"},
                        false, null, null, null, null);
                intent.putExtra("Invoker",v.getId());
                startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                Client client = new Client();
                client.setEmail(new SystemID(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)));
                client.setFirstname(((TextView)findViewById(R.id.dialog_create_account_firstname)).getText().toString());
                client.setSurname(((TextView)findViewById(R.id.dialog_create_account_surname)).getText().toString());
                if(isValidForm(client)){
                    new launch(this,client).execute();
                }
            }
        }
    }

    private boolean isValidForm(Client client){
        if(client.getFirstname().compareTo("") == 0){
            ((TextView)findViewById(R.id.dialog_create_account_firstname)).setHint("Please provide a name");
            return false;
        }else if(client.getSurname().compareTo("") == 0){
            ((TextView)findViewById(R.id.dialog_create_account_surname)).setHint("Please provide a surname");
            return false;
        }
        return true;
    }

    private static class setup extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity_Login> activity_loginWeakReference;
        Client client;

        setup(Activity_Login activity){
            activity_loginWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity_loginWeakReference != null){
                client = WaitlessDatabase.getDatabase(activity_loginWeakReference.get()).clientDao().getClient();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity_loginWeakReference.get().setContentView(R.layout.activity_login);
//            if(activity_loginWeakReference != null && client != null){
//                Intent intent = new Intent(activity_loginWeakReference.get().findViewById(R.id.button_create_account).getContext(), Activity_Main.class);
//                activity_loginWeakReference.get().findViewById(R.id.button_create_account).getContext().startActivity(intent);
//            }
//            else{
                if (ContextCompat.checkSelfPermission(activity_loginWeakReference.get().getApplicationContext(),
                        Manifest.permission.GET_ACCOUNTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity_loginWeakReference.get(),
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            MY_PERMISSIONS_REQUEST_READ_ACCOUNTS);
                }
                else{
                    activity_loginWeakReference.get().createLoginDialog();
                }
//            }
        }
    }

    private static class launch extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity_Login> activity_loginWeakReference;
        Client client;

        launch(Activity_Login activity_login, Client client){
            activity_loginWeakReference = new WeakReference<>(activity_login);
            this.client = client;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity_loginWeakReference != null){
                WaitlessDatabase.getDatabase(activity_loginWeakReference.get()).clientDao().insertClient(client);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(activity_loginWeakReference != null) {
                Intent intent = new Intent(activity_loginWeakReference.get(), Activity_Main.class);
                activity_loginWeakReference.get().startActivity(intent);
            }
        }
    }
}
