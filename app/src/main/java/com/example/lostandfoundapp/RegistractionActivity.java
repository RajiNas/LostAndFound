package com.example.lostandfoundapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistractionActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editTextConfrimPass;
    Button btnRegister;
    TextView txtLogin;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    DatabaseReference reff;

    Users users;
    FirebaseUser fUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraction);


        editTextEmail = (EditText)findViewById(R.id.editText_registrationActivity_email);
        editTextPassword = (EditText)findViewById(R.id.editText_registrationActivity_password);
        editTextConfrimPass = (EditText)findViewById(R.id.editText_registrationActivity_confirmPassword);
        txtLogin = (TextView)findViewById(R.id.textView_RegistrationAct_login);

        btnRegister = (Button)findViewById(R.id.button_registrationAcitvity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");




        reff = FirebaseDatabase.getInstance().getReference("Users");
        users = new Users();




        //import call
        mAuth =FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, CustomerLoginActivity.class));
        }

        //login textview click
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistractionActivity.this, CustomerLoginActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
//                String phone = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confPass = editTextConfrimPass.getText().toString().trim();



//                if(username.isEmpty()){
//                    Toast.makeText(RegistractionActivity.this, "Type username", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Invalid Email");
                    editTextEmail.setFocusable(true);
                }
                if(email.isEmpty()){
                    Toast.makeText(RegistractionActivity.this, "Type email", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(phone.isEmpty()){
//                    Toast.makeText(RegistractionActivity.this, "Type phone", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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



//                users.setUsername(username);
//                users.setEmail(email);
//                users.setPhone(Long.parseLong(phone));
//                users.setPassword(Integer.parseInt(password));
//                users.setDate(date);
//                users.setImage("");

 //               Users users1 = new Users(username,email,Long.parseLong(phone),Integer.parseInt(password),date,"");
//                reff.child("Joel").setValue(users1);
                //reff.push().setValue(users);

//                HashMap<String, Object> data = new HashMap<>();
//                data.put("username", "");
//                data.put("email", email);
//                data.put("phone", "");
//                data.put("password", password);

//                reff.child(user.getUid()).push().setValue(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                //update, dismiss progress
////                                pd.dismiss();
//                                Toast.makeText(getApplicationContext(), "Updated...", Toast.LENGTH_SHORT).show();
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                //failed, dismiss progress, get and show error message
////                                pd.dismiss();
//                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });

            }
        });
    }

    // create account in FB
    private void callSignup(String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(! task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(RegistractionActivity.this, "Sign up failed",Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    //get user email and uid from auth
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //generate date upon creating a new account
                    Calendar calendar =Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MMMM-yyyy");
                    String date =simpleDateFormat.format(calendar.getTime());

                    //using HashMap
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("username", "");
                    hashMap.put("phone", "");
                    hashMap.put("date", date);
                    hashMap.put("image", "");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    reff = database.getReference().child("Users");
                    reff.child(uid).setValue(hashMap);

                    Toast.makeText(RegistractionActivity.this, "Account created " + user.getEmail(),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegistractionActivity.this, CustomerLoginActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistractionActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void userProfile(){
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            UserProfileChangeRequest profileUpates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(editTextEmail.getText().toString().trim())
//                    .build();
//
//            user.updateProfile(profileUpates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        progressDialog.dismiss();
//                        System.out.println("sign in successful" + task.isSuccessful());
//                    }
//                    if(! task.isSuccessful()){
//                        Toast.makeText(RegistractionActivity.this, "Failed",Toast.LENGTH_LONG).show();
//                    }else{
//                        Intent intent = new Intent(RegistractionActivity.this, CustomerLoginActivity.class);
//
//                        finish();
//                        startActivity(intent);
//                    }
//                }
//            });
//        }
//    }
}