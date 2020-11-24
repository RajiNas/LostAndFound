package com.example.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //Details fragment transaction (default on start)
        //details fragment transaction
//        actionBar.setTitle("Details");
        DetailsFragment detailsFragment = new DetailsFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.add(R.id.content, detailsFragment);
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //Handle item clicks
                    switch (item.getItemId()) {
                        case R.id.nav_details:
                            //details fragment transaction
//                            actionBar.setTitle("Details");
                            DetailsFragment detailsFragment = new DetailsFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, detailsFragment, "");
                            ft1.commit();
                            return true;

                        case R.id.nav_location:
                            //location fragment transaction
//                            actionBar.setTitle("Location");
                           Fragment mapsFragment = new MapsFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, mapsFragment, "");
                            ft2.commit();



//                            Handler mHandler = new Handler();
//                            Runnable mPendingRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    MapsFragment mapsFragment = new MapsFragment();
//                                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//                                    ft2.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                                    ft2.replace(R.id.content, mapsFragment, "");
//                                    ft2.commitAllowingStateLoss();
//                                }
//                            };
//                            if (mPendingRunnable != null) {
//                                mHandler.post(mPendingRunnable);
//                            }

                            return true;

                        case R.id.nav_contact:
                            //contact fragment transaction
//                            actionBar.setTitle("Contact");
                            ContactsFragment contactsFragment = new ContactsFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, contactsFragment, "");
                            ft3.commit();
                            return true;
                    }
                    return false;
                }
            };


//    public void f1(String getCategory, String getTitle, String getDate, String getDescription, String getStatus, String getImage) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction t1 = fm.beginTransaction();
//        DetailsFragment detailsFragment  = new DetailsFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("category", getCategory);
//        bundle.putString("title", getTitle);
//        bundle.putString("date", getDate);
//        bundle.putString("description", getDescription);
//        bundle.putString("status", getStatus);
//        bundle.putString("image", getImage);
//        detailsFragment.setArguments(bundle);
//        t1.add(R.id.content, detailsFragment);
//        t1.commit();
//    }
}