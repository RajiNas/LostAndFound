package com.example.lostandfoundapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//public class ProfilFragment extends Fragment {
//
//    DatabaseReference databaseReference;
//    FirebaseUser user;
//
//    String uid;
//    TextView txtUsername, txtEmail,txtPhone, txtPassword;
//    Button btnEdit;
//
//    public ProfilFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profil, container, false);
//    }

public class ProfilFragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseUser user;

    String uid;
    TextView txtUsername, txtEmail,txtPhone, txtPassword;
    Button btnEdit;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        txtUsername = (TextView)getActivity().findViewById(R.id.textView_username_ProfileFragment);
        txtEmail = (TextView)getActivity().findViewById(R.id.textView_email_ProfileFragment);
        txtPhone = (TextView)getActivity().findViewById(R.id.textView_phone_ProfileFragment);
        txtPassword = (TextView)getActivity().findViewById(R.id.textView_password_ProfileFragment);

        btnEdit = (Button)getActivity().findViewById(R.id.button_edit_ProfileFragment);
    }

}