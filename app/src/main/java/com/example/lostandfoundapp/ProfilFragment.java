package com.example.lostandfoundapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.RECEIVER_VISIBLE_TO_INSTANT_APPS;
import static com.google.firebase.database.FirebaseDatabase.getInstance;


public class ProfilFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    //storage
    FirebaseStorage storage;
    StorageReference storageReference;

    //path where images of user profile will be stored
    String storagePath = "Users_Profile_Cover_Imgs/";

    FloatingActionButton updateImgprofilebtn;

    TextView txtUsername, txtEmail,txtPhone, txtDate;
    CircleImageView img;

    //Progress dialog
    ProgressDialog pd;

    //Permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //Arrays of permissions to be requested
    String cameraPermission [];
    String storagePermission[];

    //uri of picked image
    Uri image_uri;

//    //for test
//    Users user;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);


        // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

//        storageReference = getInstance().getStorageReference();

        //init array of permissions
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        // init views
        txtUsername = view.findViewById(R.id.textView_username_ProfileFragment);
        txtEmail = view.findViewById(R.id.textView_email_ProfileFragment);
        txtPhone = view.findViewById(R.id.textView_phone_ProfileFragment);
        txtDate = view.findViewById(R.id.textView_date_ProfileFragment);
        img = view.findViewById(R.id.imageview_ProfileFragment);

        updateImgprofilebtn = view.findViewById(R.id.UpdateUserPicture_ProfilFragment);


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name = "" + ds.child("username").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String date = "" + ds.child("date").getValue();
                    String imgProfilPicture = "" + ds.child("image").getValue();

                    txtUsername.setText(name);
                    txtEmail.setText(email);
                    txtPhone.setText(phone);
                    txtDate.setText(date);
                    try {
                        Picasso.get().load(imgProfilPicture).into(img);
                    }catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(img);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateImgprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });


//        //update images
//        updateImgprofilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRoX6lhTZKutDXlSB0r6828aCbXuxj91BgHUg&usqp=CAU").into(img);
//            }
//        });


        return view;
    }



    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request runtime storage permission
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //check if camera permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        //request runtime camera permission
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        //options to show in dialog
        String options[] = {"Edit Profile Picture", "Edit Username", "Edit Phone"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title
        builder.setTitle("Choose Action");
        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if(which == 0){
                    //edit profile clicked
                    pd.setMessage("Updating Profile Picture");
                    showImagePickDialog();

                }else if(which == 1){
                    //edit username clicked
                    pd.setMessage("Updating Username");
                    //calling method and pass key "name" as parameter to update it's value in database
                    showUsernamePhoneUpdateDialog("username");
                }else if (which == 2){
                    //edit phone clicked
                    pd.setMessage("Updating Phone");
                    showUsernamePhoneUpdateDialog("phone");
                }

            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showUsernamePhoneUpdateDialog(String key) {
        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update" + key);

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText = new EditText(getActivity());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value  = editText.getText().toString().trim();
                //validate if user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)//getUid ---> getEmail()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //update, dismiss progress
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated...", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed, dismiss progress, get and show error message
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                }else{
                    Toast.makeText(getActivity(), "Please enter " + key, Toast.LENGTH_SHORT).show();
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

    private void showImagePickDialog() {
        String options[] = {"Camera", "Gallery"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title
        builder.setTitle("Pick Image From");
        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item click
                if(which == 0){
                    // camera clicked
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }

                }else if(which == 1){
                    // Gallery clicked
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //This method called when user press allow or deny from permission request dialog
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //Picking from camera, first check if camera and storage permissions allowed or not
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        //permissions enabled
                        pickFromCamera();
                    }else{
                        //permissions denied
                        Toast.makeText(getActivity(), "Please enable camera && storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                //Picking from gallery, first check if storage permissions allowed or not
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        //permissions enabled
                        pickFromGallery();
                    }else{
                        //permissions denied
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //This method will be called after picking image from Camera or Gallery
        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                // image is picked from gallery, get uri of image
                image_uri = data.getData();
                uploadProfilePhoto(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
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
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded  or not
                        if(uriTask.isSuccessful()){
                            //image uploaded
                            //add/update url in user's database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put("image", downloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)//getUid ---> getEmail()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //url in database of user is added successfully
                                            //dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error adding url in database of user
                                            //dismiss progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error Updating Image...", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }else{
                            //error
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //there were some error(s), get and show message, dismiss progress dialog
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //put uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

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
}