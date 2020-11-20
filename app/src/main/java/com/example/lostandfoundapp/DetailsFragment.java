package com.example.lostandfoundapp;

import android.os.Bundle;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {

    ImageView image;
    TextView txtCategory, txtTitle, txtDate, txtDescription, txtStatus;
    Button btnSendMessage;

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

        image = (ImageView) getActivity().findViewById(R.id.image_DetailsFragment);
        txtCategory = (TextView) getActivity().findViewById(R.id.textView_category_DetailsFragment);
        txtTitle = (TextView) getActivity().findViewById(R.id.textView_title_DetailsFragment);
        txtDate = (TextView) getActivity().findViewById(R.id.textView_date_DetailsFragment);
        txtDescription = (TextView) getActivity().findViewById(R.id.textView_description_DetailsFragment);
        txtStatus = (TextView) getActivity().findViewById(R.id.textView_status_DetailsFragment);
        btnSendMessage = (Button) getActivity().findViewById(R.id.button_sendAMessage_DetailsFragment);

//        Bundle bundle2 = getArguments();
//        if(bundle2 != null){
//
//            String getCategory = bundle2.getString("category");
//            String getTitle = bundle2.getString("title");
//            String getDate = bundle2.getString("date");
//            String getDescription = bundle2.getString("description");
//            String getStatus = bundle2.getString("status");
//            String getImage = bundle2.getString("image");

//            Log.e(TAG, "image: " + getImage);
//            Log.e(TAG, "category: " + getCategory);
//            Log.e(TAG, "title: "+getTitle);
//            Log.e(TAG, "date: "+getDate);
//            Log.e(TAG, "description: "+getDescription);
//            Log.e(TAG, "status: "+getStatus);

//            Picasso.get().load(getImage).into(image);
//            txtCategory.setText(getCategory);
//            txtTitle.setText(getTitle);
//            txtDate.setText(getDate);
//            txtDescription.setText(getDescription);
//            txtStatus.setText(getStatus);
//
//        }

        String getImage = getActivity().getIntent().getStringExtra("image");
        String getCategory = getActivity().getIntent().getStringExtra("category");
        String getTitle = getActivity().getIntent().getStringExtra("title");
        String getDate = getActivity().getIntent().getStringExtra("date");
        String getDescription = getActivity().getIntent().getStringExtra("description");
        String getStatus = getActivity().getIntent().getStringExtra("status");

            Log.e(TAG, "image: " + getImage);
            Log.e(TAG, "category: " + getCategory);
            Log.e(TAG, "title: "+getTitle);
            Log.e(TAG, "date: "+getDate);
            Log.e(TAG, "description: "+getDescription);
            Log.e(TAG, "status: "+getStatus);

//        Picasso.get().load(getImage).into(image);
//        txtCategory.setText(getCategory);
//        txtTitle.setText(getTitle);
//        txtDate.setText(getDate);
//        txtDescription.setText(getDescription);
//        txtStatus.setText(getStatus);

        return view;
    }
}