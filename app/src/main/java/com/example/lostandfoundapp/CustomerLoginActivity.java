package com.example.lostandfoundapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CustomerLoginActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        editTextUsername = (EditText)findViewById(R.id.editText_loginActivity_username);
        editTextPassword = (EditText)findViewById(R.id.editText_loginActivity_Password);

        btnLogin = (Button)findViewById(R.id.button_loginActivity_Login);

    }
}