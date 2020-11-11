package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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


    EditText titletxt, longtxt,lattxt,descriptiontxt;
    Spinner categorysp;
    Button additem , returnback;

    String chosenCategory;
    // initialize fireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference notereference = db.document("TypeOfItems/ Item");
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
                    Float itemlong = Float.parseFloat(longtxt.getText().toString());
                    Float itemlat = Float.parseFloat(lattxt.getText().toString());
                    String descriptionitem = descriptiontxt.getText().toString();
                  //  String category = categorytxt.getText().toString();



                // if Category is not selected the user won't be able to add item
                if( itemTitle.equals("") || itemlong == null || itemlat == null ||descriptionitem.equals(""))
                {
                    Toast.makeText(ItemsRegistrationActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }else {
                    Calendar calendar =Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd-MMMM-yyyy");
                    String date =simpleDateFormat.format(calendar.getTime());

                    Items items = new Items(itemTitle,itemlong,itemlat,descriptionitem,chosenCategory, date);
                    if (chosenCategory == "Select Category") {
                        Toast.makeText(ItemsRegistrationActivity.this, "Please Select a category", Toast.LENGTH_SHORT).show();

                    } else
                        reff.add(items).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ItemsRegistrationActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                            }
                        });

//                    Map<String , Object> note = new HashMap<>();
//                    note.put(ITEM_TITLE, itemTitle);
//                    note.put(ITEM_LONGITUDE,itemlong);
//                    note.put(ITEM_LATITUDE,itemlat);
//                    note.put(ITEM_DESCRIPTION,descriptionitem);

//                    reff.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(ItemsRegistrationActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(ItemsRegistrationActivity.this, "Item not saved", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String categoryText = adapterView.getItemAtPosition(position).toString();
       // Toast.makeText(adapterView.getContext(),categoryText,Toast.LENGTH_SHORT).show();
        chosenCategory = categoryText;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}