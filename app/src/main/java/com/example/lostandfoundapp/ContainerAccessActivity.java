package com.example.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ContainerAccessActivity extends AppCompatActivity {
    // sidebar navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private FirebaseAuth mAuth;

    Fragment fr;

    // declare getShared Preferences
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_access);

        //ActioBar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Container");

        mAuth = FirebaseAuth.getInstance();

        // sidebar navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("ItemIdentifier", Context.MODE_PRIVATE);

        navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.messages_item:
                        fr = new MessageFragment();
                        displayFragment();
                        return true;
                    case R.id.profile_item:
                        fr = new ProfilFragment();
                        displayFragment();
                        return true;
                    case R.id.list_item:
                        fr = new FragmentItemList();
                        displayFragment();
                        return true;

                    case R.id.logout_item:
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(ContainerAccessActivity.this, MainActivity.class));
                        return true;
                    case R.id.addItem:
                        Intent intent = new Intent(ContainerAccessActivity.this, ItemsRegistrationActivity.class);

                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void displayFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fr);
        transaction.commit();
    }

}