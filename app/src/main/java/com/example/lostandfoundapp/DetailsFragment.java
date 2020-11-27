package com.example.lostandfoundapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.concurrent.Executor;


public class DetailsFragment extends Fragment {

    FloatingActionButton floatingActionButton, fab_edit, fab_contact, fab_map;
    Animation fabOpen, fabClose, fabRClockwise, fabRAntiClockwise;
    boolean isOpen = false;

    ImageView image;
    TextView txtCategory, txtTitle, txtDate, txtDescription, txtStatus;

    String getUserEmail;
    String getId;

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    DocumentReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    //Progress dialog
    ProgressDialog pd;


    public static final String TAG = "DetailsFragment";

    public DetailsFragment() {
        // Required empty public constructor
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        pd = new ProgressDialog(getContext());

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Item");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        reference = firebaseFirestore.document(getId);

        floatingActionButton = view.findViewById(R.id.fab);
        fab_edit = view.findViewById(R.id.edit_button);
        fab_contact = view.findViewById(R.id.contact_button);
        fab_map = view.findViewById(R.id.map_button);

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        fabRAntiClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anticlockwise);


        image = (ImageView) view.findViewById(R.id.image_DetailsFragment);
        txtCategory = (TextView) view.findViewById(R.id.textView_category_DetailsFragment);
        txtTitle = (TextView) view.findViewById(R.id.textView_title_DetailsFragment);
        txtDate = (TextView) view.findViewById(R.id.textView_date_DetailsFragment);
        txtDescription = (TextView) view.findViewById(R.id.textView_description_DetailsFragment);
        txtStatus = (TextView) view.findViewById(R.id.textView_status_DetailsFragment);

        String getImage = getActivity().getIntent().getStringExtra("image");
        String getCategory = getActivity().getIntent().getStringExtra("category");
        String getTitle = getActivity().getIntent().getStringExtra("title");
        String getDate = getActivity().getIntent().getStringExtra("date");
        String getDescription = getActivity().getIntent().getStringExtra("description");
        String getStatus = getActivity().getIntent().getStringExtra("status");
        getId = getActivity().getIntent().getStringExtra("id");

        Picasso.get().load(getImage).into(image);
        txtCategory.setText(getCategory);
        txtTitle.setText(getTitle);
        txtDate.setText(getDate);
        txtDescription.setText(getDescription);
        txtStatus.setText(getStatus);




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

                getUserEmail = getActivity().getIntent().getStringExtra("userEmail");
                if (firebaseUser.getEmail().equals(getUserEmail)) {
                    showEditItemDialog();

//                    collectionReference.document(getId).update("title", "category");



//                    String getImage = getActivity().getIntent().getStringExtra("image");
//                    String getCategory = getActivity().getIntent().getStringExtra("category");
//                    String getTitle = getActivity().getIntent().getStringExtra("title");
//                    String getDate = getActivity().getIntent().getStringExtra("date");
//                    String getDescription = getActivity().getIntent().getStringExtra("description");
//                    String getStatus = getActivity().getIntent().getStringExtra("status");

//                    Picasso.get().load(getImage).into(image);
//                    txtCategory.setText(getCategory);
//                    txtTitle.setText("onehter trial");
//                    txtDate.setText(getDate);
//                    txtDescription.setText(getDescription);
//                    txtStatus.setText(getStatus);

                } else {
                    Toast.makeText(getContext(), "Sorry You Don't Have Access To This Item", Toast.LENGTH_SHORT).show();
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
                String getLocation = getActivity().getIntent().getStringExtra("address");
                // goes to map activity
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("address", getLocation);
                startActivity(intent);
            }
        });

//        Log.e(TAG, "image: " + getImage);
//        Log.e(TAG, "category: " + getCategory);
//        Log.e(TAG, "title: "+getTitle);
//        Log.e(TAG, "date: "+getDate);
//        Log.e(TAG, "description: "+getDescription);
//        Log.e(TAG, "status: "+getStatus);



        return view;
    }

    private void showEditItemDialog() {
        //options to show in dialog
        String options[] = {"Edit Title", "Edit Category", "Edit Description", "Edit Status"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + key);

        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);
        //add edit text
        EditText editText = new EditText(getContext());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

//        Spinner spinner = new Spinner(getContext());
//        spinner.

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


//                    getId = getActivity().getIntent().getStringExtra("id");
//                    Toast.makeText(ItemDetails.this, id, Toast.LENGTH_SHORT).show();

                    collectionReference.document(getId).update(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //update, dismiss progress
                                    pd.dismiss();

                                    DocumentReference docRef = firebaseFirestore.collection("Item").document(getId);

                                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                            Picasso.get().load(value.getString("image")).into(image);
                                            txtCategory.setText(value.getString("category"));
                                            txtTitle.setText(value.getString("title"));
                                            txtDate.setText(value.getString("date"));
                                            txtDescription.setText(value.getString("description"));
                                            txtStatus.setText(value.getString("status"));

                                        }
                                    });


//                                    Picasso.get().load(getImage).into(image);
//                                    txtCategory.setText(getCategory);
//                                   txtTitle.setText(items.getTitle());
//                                    txtDate.setText(getDate);
//                                    txtDescription.setText(getDescription);
//                                    txtStatus.setText(getStatus);


                                    Toast.makeText(getContext(), "Updated...", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getApplicationContext(), ItemDetails.class));

                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed, dismiss progress, get and show error message
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(getContext(), "Please enter " + key, Toast.LENGTH_SHORT).show();
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
}