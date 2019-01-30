package com.kevin.waitless;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity to extend if you want a menu in the top right corner
 */
public class WaitlessActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMenu(new Fragment_Menu());
    }

    private void addMenu(Fragment fragment) {
        final FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.commit();
    }
}
