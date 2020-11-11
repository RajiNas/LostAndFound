package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ItemsRegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    EditText titletxt, longtxt,lattxt,descriptiontxt;
    Spinner categorysp;
    Button additem , returnback;

    String chosenCategory , chosenCurrentUser;
    // initialize fireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference reff = db.collection("Item");

    private static final String ITEM_TITLE ="title";
    private static final String ITEM_LATITUDE ="latitude";
    private static final String ITEM_LONGITUDE ="longitude";
    private static final String ITEM_DESCRIPTION ="description";
    private static final String ITEM_CATEGORY = "category";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_registration);

        titletxt =(EditText) findViewById(R.id.editextTitleItem);
        longtxt =(EditText) findViewById(R.id.editTextLongitude);
        lattxt =(EditText) findViewById(R.id.editTextLatitude);
        descriptiontxt =(EditText) findViewById(R.id.editTextDescriptionItem);

        //set the categori of the item
        categorysp = (Spinner) findViewById(R.id.editTextCategoryspinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Categories, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorysp.setAdapter(adapter);
        categorysp.setOnItemSelectedListener(this);

        additem =(Button) findViewById(R.id.buttonaddItem);
        returnback =(Button) findViewById(R.id.buttonBacktoContainer);

        //Go back to the Container activity
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsRegistrationActivity.this , ContainerAccessActivity.class);
                startActivity(intent);
            }
        });


        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = titletxt.getText().toString();
                String descriptionitem = descriptiontxt.getText().toString();
               // Initialize float
                Float itemlong;
                Float itemlat;

                  // add values so that the float wont be empty with the string
                if( longtxt.getText().toString().equals("")){
                    itemlong = 0.0f;
                }
                else {
                    itemlong = Float.parseFloat(longtxt.getText().toString());
                }
                if( lattxt.getText().toString().equals("")){
                    itemlat = 0.0f;
                }else {
                    itemlat = Float.parseFloat(lattxt.getText().toString());
                }

                // if Category is not selected the user won't be able to add item
                if( itemTitle.equals("") || itemlong == 0.0 || itemlat == 0.0 ||descriptionitem.equals(""))
                {
                    Toast.makeText(ItemsRegistrationActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }else {
                    Calendar calendar =Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MMMM-yyyy");
                    String date =simpleDateFormat.format(calendar.getTime());

                    Items items = new Items(chosenCurrentUser,itemTitle,itemlong,itemlat,descriptionitem,chosenCategory, date);
                    if (chosenCategory.equals("Select Category")) {
                        Toast.makeText(ItemsRegistrationActivity.this, "Please Select a category", Toast.LENGTH_SHORT).show();

                    } else
                        reff.add(items).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ItemsRegistrationActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                            }
                        });

                }
            }
        });
        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        // obtain the name of the user to add into the firestoe
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name = " " + ds.child("username").getValue();

                    // Select user name and add it into the item
                    chosenCurrentUser = name;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String categoryText = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(),categoryText,Toast.LENGTH_SHORT).show();
        chosenCategory = categoryText;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





}