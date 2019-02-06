package com.kevin.waitless;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

public class Activity_Login extends WaitlessActivity {

    private static WaitlessDatabase database;
    private static final int PERMISSIONS_REQUEST_READ_ACCOUNTS = 1;
    private static final int REQUEST_CODE_PICK_ACCOUNT = 2;
    private static final int REQUEST_CODE_ALLOW_OVERLAY = 3;

    private static final String TAG = "Activity_Login";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSIONS_REQUEST_READ_ACCOUNTS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(myIntent, REQUEST_CODE_ALLOW_OVERLAY);
            }
        }
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
                User client = new User();
                client.setEmail(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                client.setFirstname(((TextView)findViewById(R.id.dialog_create_account_firstname)).getText().toString());
                client.setSurname(((TextView)findViewById(R.id.dialog_create_account_surname)).getText().toString());
                if(isValidForm(client)){
                    new launch(this,client).execute();
                }
            }
        }
    }

    private boolean isValidForm(User client){
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

        setup(Activity_Login activity){
            activity_loginWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity_loginWeakReference != null){
                USER = WaitlessDatabase.getDatabase(activity_loginWeakReference.get()).clientDao().getUser();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activity_loginWeakReference.get().setContentView(R.layout.activity_login);
            //Toast.makeText(activity_loginWeakReference.get(),"Client venues: "+USER.getStaffed_venues().toString(),Toast.LENGTH_LONG).show();
            if(activity_loginWeakReference != null && USER != null){
                Intent intent = new Intent(activity_loginWeakReference.get().findViewById(R.id.button_create_account).getContext(), Activity_Main.class);
                activity_loginWeakReference.get().findViewById(R.id.button_create_account).getContext().startActivity(intent);
            }
            else{
                activity_loginWeakReference.get().createLoginDialog();
            }
        }
    }

    private static class launch extends AsyncTask<Void,Void,Void>{

        WeakReference<Activity_Login> activity_loginWeakReference;

        launch(Activity_Login activity_login, User user){
            activity_loginWeakReference = new WeakReference<>(activity_login);
            USER = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(activity_loginWeakReference != null){
                WaitlessDatabase.getDatabase(activity_loginWeakReference.get()).clientDao().insertUser(USER);
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
