package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class ItemsRegistrationActivity extends AppCompatActivity {


    EditText titletxt, longtxt,lattxt,descriptiontxt;
    Button additem , returnback;

    // initialize fireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference notereference = db.document("TypeOfItems/ Item");
    private static final String ITEM_TITLE ="title";
    private static final String ITEM_LATITUDE ="latitude";
    private static final String ITEM_LONGITUDE ="longitude";
    private static final String ITEM_DESCRIPTION ="description";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_registration);

        titletxt =(EditText) findViewById(R.id.editextTitleItem);
        longtxt =(EditText) findViewById(R.id.editTextLongitude);
        lattxt =(EditText) findViewById(R.id.editTextLatitude);
        descriptiontxt =(EditText) findViewById(R.id.editTextDescriptionItem);

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
                    String itemlong = longtxt.getText().toString();
                    String itemlat = lattxt.getText().toString();
                    String descriptionitem = descriptiontxt.getText().toString();

                    Map<String , Object> note = new HashMap<>();
                    note.put(ITEM_TITLE, itemTitle);
                    note.put(ITEM_LONGITUDE,itemlong);
                    note.put(ITEM_LATITUDE,itemlat);
                    note.put(ITEM_DESCRIPTION,descriptionitem);
                    notereference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ItemsRegistrationActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ItemsRegistrationActivity.this, "Item not saved", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }
}