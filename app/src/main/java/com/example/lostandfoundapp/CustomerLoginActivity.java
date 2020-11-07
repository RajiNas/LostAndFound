package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerLoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        editTextEmail = (EditText)findViewById(R.id.editText_loginActivity_email);
        editTextPassword = (EditText)findViewById(R.id.editText_loginActivity_Password);

        btnLogin = (Button)findViewById(R.id.button_loginActivity_Login);

        // import call
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(CustomerLoginActivity.this, ContainerAccessActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(CustomerLoginActivity.this, "Type email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(CustomerLoginActivity.this, "Type password", Toast.LENGTH_SHORT).show();
                    return;
                }
                callSignin(email, password);
            }
        });
    }

    private void callSignin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("Sign in successful" + task.isSuccessful());

                if(! task.isSuccessful()){
                    Toast.makeText(CustomerLoginActivity.this, "Failed",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(CustomerLoginActivity.this, ContainerAccessActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }
}