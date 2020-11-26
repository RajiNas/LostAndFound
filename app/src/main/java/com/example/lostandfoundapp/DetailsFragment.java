package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;


public class DetailsFragment extends Fragment {

    ImageView image;
    TextView txtCategory, txtTitle, txtDate, txtDescription, txtStatus;


    FirebaseFirestore firebaseFirestore;
//    CollectionReference cReff;

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



        image = (ImageView) view.findViewById(R.id.image_DetailsFragment);
        txtCategory = (TextView) view.findViewById(R.id.textView_category_DetailsFragment);
        txtTitle = (TextView) view.findViewById(R.id.textView_title_DetailsFragment);
        txtDate = (TextView) view.findViewById(R.id.textView_date_DetailsFragment);
        txtDescription = (TextView) view.findViewById(R.id.textView_description_DetailsFragment);
        txtStatus = (TextView) view.findViewById(R.id.textView_status_DetailsFragment);


//        String getLocation = getActivity().getIntent().getStringExtra("address");
//        btnSendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapActivity.class);
//                intent.putExtra("address", getLocation);
//                startActivity(intent);
//            }
//        });

        String getId = getActivity().getIntent().getStringExtra("id");

        firebaseFirestore = FirebaseFirestore.getInstance();
//        cReff = firebaseFirestore.collection("Item");
        DocumentReference documentReference = firebaseFirestore.collection("Item").document(getId);


        String getImage = getActivity().getIntent().getStringExtra("image");
        String getCategory = getActivity().getIntent().getStringExtra("category");
        String getTitle = getActivity().getIntent().getStringExtra("title");
        String getDate = getActivity().getIntent().getStringExtra("date");
        String getDescription = getActivity().getIntent().getStringExtra("description");
        String getStatus = getActivity().getIntent().getStringExtra("status");

//        Log.e(TAG, "image: " + getImage);
//        Log.e(TAG, "category: " + getCategory);
//        Log.e(TAG, "title: "+getTitle);
//        Log.e(TAG, "date: "+getDate);
//        Log.e(TAG, "description: "+getDescription);
//        Log.e(TAG, "status: "+getStatus);

        Picasso.get().load(getImage).into(image);
        txtCategory.setText(getCategory);
        txtTitle.setText(getTitle);
        txtDate.setText(getDate);
        txtDescription.setText(getDescription);
        txtStatus.setText(getStatus);

        return view;
    }

}