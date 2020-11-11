package com.example.lostandfoundapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfilFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    TextView txtUsername, txtEmail,txtPhone, txtPassword, txtDate;
    ImageView img;
    Button btnEdit;

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
        databaseReference = firebaseDatabase.getReference("Users");

        // init views
        txtUsername = view.findViewById(R.id.textView_username_ProfileFragment);
        txtEmail = view.findViewById(R.id.textView_email_ProfileFragment);
        txtPhone = view.findViewById(R.id.textView_phone_ProfileFragment);
        txtPassword = view.findViewById(R.id.textView_password_ProfileFragment);
        txtDate = view.findViewById(R.id.textView_date_ProfileFragment);
        img = view.findViewById(R.id.imageview_ProfileFragment);

        btnEdit = (Button)getActivity().findViewById(R.id.button_edit_ProfileFragment);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name = " " + ds.child("username").getValue();
                    String email = " " + ds.child("email").getValue();
                    String phone = " " + ds.child("phone").getValue();
                    String password = " " + ds.child("password").getValue();
                    String date = " " + ds.child("date").getValue();
                    String image = " " + ds.child("image").getValue();

                    txtUsername.setText(name);
                    txtEmail.setText(email);
                    txtPhone.setText(phone);
                    txtPassword.setText(password);
                    txtDate.setText(date);
//                    Picasso.get().load(image).into(img);

                    try {
                        Picasso.get().load(image).into(img);
                    }catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(img);
//                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }


}