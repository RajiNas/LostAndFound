package com.example.lostandfoundapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistractionActivity extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPhone, editTextPassword, editTextConfrimPass;
    Button btnRegister;

    DatabaseReference reff;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraction);

        editTextUsername = (EditText)findViewById(R.id.editText_registrationActivity_username);
        editTextEmail = (EditText)findViewById(R.id.editText_registrationActivity_email);
        editTextPhone = (EditText)findViewById(R.id.editText_registrationActivity_phone);
        editTextPassword = (EditText)findViewById(R.id.editText_registrationActivity_password);
        editTextConfrimPass = (EditText)findViewById(R.id.editText_registrationActivity_confirmPassword);

        btnRegister = (Button)findViewById(R.id.button_registrationAcitvity_register);

        reff = FirebaseDatabase.getInstance().getReference().child("User");
        users = new Users();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString().trim();
                String confirmPass = editTextConfrimPass.getText().toString().trim();

                users.setUsername(username);
                users.setEmail(email);
                users.setPhone(Long.parseLong(phone));
                users.setPassword(Integer.parseInt(password));
                users.setConfirmPassword(Integer.parseInt(confirmPass));

                reff.push().setValue(users);
            }
        });

    }
}