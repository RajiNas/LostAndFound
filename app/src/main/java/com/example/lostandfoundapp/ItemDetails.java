package com.example.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ItemDetails extends AppCompatActivity {

//    FloatingActionButton floatingActionButton, fab_edit, fab_contact, fab_map;
//    Animation fabOpen, fabClose, fabRClockwise, fabRAntiClockwise;
//    boolean isOpen = false;

    String getUserEmail;
    String id;

    //Progress dialog
    ProgressDialog pd;

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        pd = new ProgressDialog(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Item");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

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
//                           MapActivity mapsFragment = new MapActivity();
//                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//                            ft2.replace(R.id.content, mapsFragment, "");
//                            ft2.commit();
//                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                            startActivity(intent);
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

}