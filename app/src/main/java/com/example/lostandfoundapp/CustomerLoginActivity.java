package com.example.lostandfoundapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CustomerLoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    TextView txtRecoverPass, txtReg;
    Button btnLogin;
    ProgressDialog progressDialog;


    DatabaseReference reff;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        editTextEmail = (EditText) findViewById(R.id.editText_loginActivity_email);
        editTextPassword = (EditText) findViewById(R.id.editText_loginActivity_Password);
        txtRecoverPass = (TextView) findViewById(R.id.textView_Login_recoverpassword);
        txtReg = (TextView) findViewById(R.id.textView_Login_register);

        btnLogin = (Button) findViewById(R.id.button_loginActivity_Login);

        progressDialog = new ProgressDialog(this);


        // import call
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(CustomerLoginActivity.this, ContainerAccessActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Invalid Email");
                    editTextEmail.setFocusable(true);
                }
                if (email.isEmpty()) {
                    Toast.makeText(CustomerLoginActivity.this, "Type email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(CustomerLoginActivity.this, "Type password", Toast.LENGTH_SHORT).show();
                    return;
                }
                callSignin(email, password);
            }
        });

        //Recover password textview click
        txtRecoverPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        //register textView click
        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerLoginActivity.this, RegistractionActivity.class));
                finish();
            }
        });

    }

    private void showRecoverPasswordDialog() {
        //Allert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //Set layout Linear layout
        LinearLayout linearLayout = new LinearLayout(this);

        //views to set in dialog
        EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        //sets the min width of a EditVIew to fit a text of n 'M' letters regardless of the actual text
        //extension and text size
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // input email
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });

        //button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        // show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        //show process dialog
        progressDialog.setMessage("Sending email...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isComplete()) {
                    Toast.makeText(CustomerLoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CustomerLoginActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //get and show proper error message
                Toast.makeText(CustomerLoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void callSignin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("Sign in successful" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(CustomerLoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        } else {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //get user email and uid from auth
//                            String email = user.getEmail();
//                            String uid = user.getUid();
//
//                            //generate date upon creating a new account
//                            Calendar calendar =Calendar.getInstance();
//                            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MMMM-yyyy");
//                            String date =simpleDateFormat.format(calendar.getTime());
//
//                            //using HashMap
//                            HashMap<Object, String> hashMap = new HashMap<>();
//                            hashMap.put("email", email);
//                            hashMap.put("uid", uid);
//                            hashMap.put("username", "");
//                            hashMap.put("phone", "");
//                            hashMap.put("date", date);
//                            hashMap.put("image","");
//
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//                            reff = database.getReference().child("Users");
//                            reff.child(uid).setValue(hashMap);

                            Intent intent = new Intent(CustomerLoginActivity.this, ContainerAccessActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }

                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, get and show error message
                Toast.makeText(CustomerLoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}