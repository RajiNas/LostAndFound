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

    FloatingActionButton floatingActionButton, fab_edit, fab_contact, fab_map;
    Animation fabOpen, fabClose, fabRClockwise, fabRAntiClockwise;
    boolean isOpen = false;

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

        floatingActionButton = findViewById(R.id.fab);
        fab_edit = findViewById(R.id.edit_button);
        fab_contact = findViewById(R.id.contact_button);
        fab_map = findViewById(R.id.map_button);

        fabOpen = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.rotate_clockwise);
        fabRAntiClockwise = AnimationUtils.loadAnimation((getApplicationContext()), R.anim.rotate_anticlockwise);


        //Details fragment transaction (default on start)
        //details fragment transaction
//        actionBar.setTitle("Details");
        DetailsFragment detailsFragment = new DetailsFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.add(R.id.content, detailsFragment);
        ft1.commit();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab_edit.startAnimation(fabClose);
                    fab_contact.startAnimation(fabClose);
                    fab_map.startAnimation(fabClose);
                    floatingActionButton.startAnimation(fabRClockwise);

                    fab_edit.setClickable(false);
                    fab_contact.setClickable(false);
                    fab_map.setClickable(false);

                    isOpen = false;
                } else {
                    fab_edit.startAnimation(fabOpen);
                    fab_contact.startAnimation(fabOpen);
                    fab_map.startAnimation(fabOpen);
                    floatingActionButton.startAnimation(fabRAntiClockwise);

                    fab_edit.setClickable(true);
                    fab_contact.setClickable(true);
                    fab_map.setClickable(true);

                    isOpen = true;
                }
            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enables user to edit their item only

                getUserEmail = getIntent().getStringExtra("userEmail");
                if (firebaseUser.getEmail().equals(getUserEmail)) {
                    showEditItemDialog();


                } else {
                    Toast.makeText(ItemDetails.this, "Sorry You Don't Have Access To Edit This Item", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goes to contact activity
            }
        });

        fab_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLocation = getIntent().getStringExtra("address");
                // goes to map activity
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("address", getLocation);
                startActivity(intent);
            }
        });
//        checkUserStatus();
    }

//    private void checkUserStatus(){
//        //get current user
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if(user != null){
//            //user is signed in
//            //set email of logged in user
//            getUserEmail.equals(user.getEmail());
//        }else{
//            //user not signed in, go to main activity
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//    }

    private void showEditItemDialog() {
        //options to show in dialog
        String options[] = {"Edit Title", "Edit Category", "Edit Description", "Edit Status"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set title
        builder.setTitle("Choose Action");
        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if (which == 0) {
                    //edit title clicked
                    pd.setMessage("Updating Title");
//                    showImagePickDialog();
                    showUpdateDialog("title");

                } else if (which == 1) {
                    //edit category clicked
                    pd.setMessage("Updating Category");
                    //calling method and pass key "name" as parameter to update it's value in database
                    showUpdateDialog("category");

                } else if (which == 2) {
                    //edit description clicked
                    pd.setMessage("Updating Description");
                    showUpdateDialog("description");

                } else if (which == 3) {
                    //edit status clicked
                    pd.setMessage("Updating Status");
                    showUpdateDialog("status");
                }

            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showUpdateDialog(String key) {
        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update" + key);

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);
        //add edit text
        EditText editText = new EditText(this);
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();



                //validate if user has entered something or not
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);


                    id = getIntent().getStringExtra("id");
//                    Toast.makeText(ItemDetails.this, id, Toast.LENGTH_SHORT).show();

                    collectionReference.document(id).update(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //update, dismiss progress
                                    pd.dismiss();

                                    Toast.makeText(getApplicationContext(), "Updated...", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getApplicationContext(), ItemDetails.class));

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed, dismiss progress, get and show error message
                                    pd.dismiss();

                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter " + key, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //add button in dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();
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