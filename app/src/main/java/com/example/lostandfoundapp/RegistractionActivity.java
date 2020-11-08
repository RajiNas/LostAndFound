package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistractionActivity extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPhone, editTextPassword, editTextConfrimPass;
    Button btnRegister;

    private FirebaseAuth mAuth;
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

        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        users = new Users();

        //import call
        mAuth =FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, CustomerLoginActivity.class));
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confPass = editTextConfrimPass.getText().toString().trim();

                if(username.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(confPass.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(RegistractionActivity.this, "Password should be minimum 6 chars",Toast.LENGTH_LONG).show();
                    return;
                }
                if(! password.equals(confPass)){
                    Toast.makeText(RegistractionActivity.this, "Passwords don't match",Toast.LENGTH_LONG).show();
                    return;
                }
                callSignup(email,password);
                users.setUsername(username);
                users.setEmail(email);
                users.setPhone(Long.parseLong(phone));
                users.setPassword(Integer.parseInt(password));
                users.setConfPassword(Integer.parseInt(confPass));

                reff.push().setValue(users);
            }
        });
    }

    // create account in FB
    private void callSignup(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(! task.isSuccessful()){
                    Toast.makeText(RegistractionActivity.this, "Sign up failed",Toast.LENGTH_LONG).show();
                }else{
                    userProfile();
                    Toast.makeText(RegistractionActivity.this, "Account created",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void userProfile(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profileUpates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(editTextUsername.getText().toString().trim())
                    .build();

            user.updateProfile(profileUpates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        System.out.println("sign in successful" + task.isSuccessful());
                    }
                    if(! task.isSuccessful()){
                        Toast.makeText(RegistractionActivity.this, "Failed",Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(RegistractionActivity.this, CustomerLoginActivity.class);

                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
    }
}