package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ContainerAccessActivity extends AppCompatActivity {
    Button btnSignout;
    private FirebaseAuth mAuth;

    Fragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_access);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSignout = (Button)findViewById(R.id.buttonSignout);
        mAuth = FirebaseAuth.getInstance();
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(ContainerAccessActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.messages_item:
                fr = new MessageFragment();
                displayFragment();
                return true;
            case R.id.profile_item:
                fr = new ProfilFragment();
                displayFragment();
                return true;
            case R.id.lostList_item:
                fr = new LostFragment();
                displayFragment();
                return true;
            case R.id.foundList_item:
                fr = new FoundFragment();
                displayFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void displayFragment(){
        FragmentManager manager =getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fr);
        transaction.commit();
    }
}