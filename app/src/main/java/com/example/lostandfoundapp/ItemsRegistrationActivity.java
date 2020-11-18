package com.example.lostandfoundapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

    ImageView imgItem;
    FloatingActionButton addItemImgbtn;

    EditText titletxt, longtxt, lattxt, descriptiontxt, edStatus;
    Spinner categorysp;
    Button additem, returnback;

    String chosenCategory, chosenCurrentUser, status, saveImgItem;
    //init progress dialog

    //path where images of user profile will be stored
    String storagePath = "Item_Imgs/";


    //Permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //Arrays of permissions to be requested
    private String cameraPermission[];
    private String storagePermission[];

    //Progress dialog
    ProgressDialog pd;
    //uri of picked image
    Uri image_uri;
    // initialize fireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    CollectionReference reff = db.collection("Item");

    RadioButton radioButton;
    RadioGroup radioGroup;

    private static final String ITEM_TITLE = "title";
    private static final String ITEM_LATITUDE = "latitude";
    private static final String ITEM_LONGITUDE = "longitude";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_registration);


        titletxt = (EditText) findViewById(R.id.editextTitleItem);
        longtxt = (EditText) findViewById(R.id.editTextLongitude);
        lattxt = (EditText) findViewById(R.id.editTextLatitude);
        descriptiontxt = (EditText) findViewById(R.id.editTextDescriptionItem);
        //edStatus = (EditText)findViewById(R.id.editextItemStatus_Itemregisration);
        imgItem = (ImageView) findViewById(R.id.imageViewItemActivity);
        pd = new ProgressDialog(this);
        //set the categori of the item
        categorysp = (Spinner) findViewById(R.id.editTextCategoryspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        //init array of permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //intialize the storage for the pictures
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        addItemImgbtn = findViewById(R.id.addpictureiItemRegistraction);

        categorysp.setAdapter(adapter);
        categorysp.setOnItemSelectedListener(this);

        additem = (Button) findViewById(R.id.buttonaddItem);
        returnback = (Button) findViewById(R.id.buttonBacktoContainer);

//         foundItem = (RadioButton) findViewById(R.id.RadioItemFound);

        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");



//
//
//        Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();

//        radioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 checkedBtn = radioButton.getText().toString();
//            }
//        });


        // button that adds the image to the item
        addItemImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImgPicture();
            }
        });


        //Go back to the Container activity
        returnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsRegistrationActivity.this, ContainerAccessActivity.class);
                startActivity(intent);
            }
        });


        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = titletxt.getText().toString();
                String descriptionitem = descriptiontxt.getText().toString();
                //  String status = edStatus.getText().toString();

                //  saveImgItem =  "com.google.android.gms.tasks.zzu@275dd66";
                // Initialize float
                Float itemlong;
                Float itemlat;

                // add values so that the float wont be empty with the string
                if (longtxt.getText().toString().equals("")) {
                    itemlong = 0.0f;
                } else {
                    itemlong = Float.parseFloat(longtxt.getText().toString());
                }
                if (lattxt.getText().toString().equals("")) {
                    itemlat = 0.0f;
                } else {
                    itemlat = Float.parseFloat(lattxt.getText().toString());
                }

                // if Category is not selected the user won't be able to add item
                if (itemTitle.equals("") || itemlong == 0.0 || itemlat == 0.0 || descriptionitem.equals("")) {
                    Toast.makeText(ItemsRegistrationActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
                    String date = simpleDateFormat.format(calendar.getTime());

                    Items items = new Items(chosenCurrentUser, itemTitle, itemlong, itemlat, descriptionitem, chosenCategory, date, status, saveImgItem);
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


        // obtain the name of the user to add into the firestoe
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
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
        Toast.makeText(adapterView.getContext(), categoryText, Toast.LENGTH_SHORT).show();
        chosenCategory = categoryText;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(ItemsRegistrationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requestStoragePermission() {
        //request runtime storage permission

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
        }

    }

    private boolean checkCameraPermission() {
        //check if camera permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(
                ItemsRegistrationActivity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(ItemsRegistrationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }


    private void requestCameraPermission() {
        //request runtime camera permission


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //This method called when user press allow or deny from permission request dialog
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                //Picking from camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permissions enabled
                        pickFromCamera();
                    } else {
                        //permissions denied
                        Toast.makeText(ItemsRegistrationActivity.this, "Please enable camera && storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {
                //Picking from gallery, first check if storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permissions enabled
                        pickFromGallery();
                    } else {
                        //permissions denied
                        Toast.makeText(ItemsRegistrationActivity.this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //This method will be called after picking image from Camera or Gallery
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // image is picked from gallery, get uri of image
                image_uri = data.getData();
                uploadProfilePhoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image is picked from camera, get uri of image
                uploadProfilePhoto(image_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri uri) {
        //show progress
        pd.show();

        //path of image to be stored in firebase storage
        String filePath = storagePath + "_" + user.getUid();//getUid ---> getEmail()

        StorageReference storageReference2nd = storageReference.child(filePath);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to storage, now get it's url and store it in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded  or not
                        if (uriTask.isSuccessful()) {
                            pd.dismiss();
                            //image uploaded
                            //add/update url in user's database
//                            Map<String,Object> map = new HashMap<>();
//                            map.put("image",downloadUri);
//                    reff.document("Image").update(map);

                            saveImgItem = downloadUri.toString();
                            Picasso.get().load(saveImgItem).into(imgItem);
                        } else {
                            //error
                            pd.dismiss();
                            Toast.makeText(ItemsRegistrationActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //there were some error(s), get and show message, dismiss progress dialog
                        pd.dismiss();
                        Toast.makeText(ItemsRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //put uri
        image_uri = ItemsRegistrationActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void addImgPicture() {
        String options[] = {"Camera", "Gallery"};
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemsRegistrationActivity.this);
        //Set title
        builder.setTitle("Pick Image From");
        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if (which == 0) {
                    // camera clicked
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    // Gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()){
            case R.id.RadioItemFound:
                if(checked)
                    status = "Found";
                Toast.makeText(this, "item was clicked", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.RadioItemLost:
                if(checked)
                    status = "Lost";
                Toast.makeText(this, "lost item was clicked", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}