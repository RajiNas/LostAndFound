package com.example.lostandfoundapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContactsFragment extends Fragment {

    //    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    //Firebase auth
    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    //For checking if the user seen the message
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;
    String userName;
    String smgTitle;

    SharedPreferences sp;

    public ContactsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_og, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recyclerView);
        profileIv = (ImageView) view.findViewById(R.id.profileIv);
        nameTv = (TextView) view.findViewById(R.id.nameTv);
        userStatusTv = (TextView) view.findViewById(R.id.userStatusTv);
        messageEt = (EditText) view.findViewById(R.id.messageEt);
        sendBtn = (ImageButton) view.findViewById(R.id.sendBtn);

        //Layout for recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        //recyclerView properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("Users");

        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        myUid = user1.getUid();

        // cause of probably error Note <--- I decided to change it from getString extra to a getSharedPrefereneces
        SharedPreferences sp = getContext().getSharedPreferences("DetailsInfo", Context.MODE_PRIVATE);

        hisUid = sp.getString("hisId", "");//getActivity().getIntent().getStringExtra("hisId");
        userName = sp.getString("username", "");//getActivity().getIntent().getStringExtra("username");
        hisImage = sp.getString("image", "");//getActivity().getIntent().getStringExtra("image");
        smgTitle = sp.getString("ItemTitle","");
        Toast.makeText(getContext(), "hisUid: " + hisUid, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "myUid: " + myUid, Toast.LENGTH_SHORT).show();

        //search user to get that user's info
        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);
        nameTv.setText(userName);
        Picasso.get().load(hisImage).into(profileIv);

        //click button to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edit text
                String message = messageEt.getText().toString().trim();
                //check if text is empty
                if (TextUtils.isEmpty(message)) {
                    //text empty
                    Toast.makeText(getContext(), "Cannot send an empty message...", Toast.LENGTH_SHORT).show();
                } else {
                    //text not empty
                    sendMessage(message);
                }
            }
        });
        readMessages();
        seenMessages();
        Log.e("ContactsFragments"," My uid is : " + myUid);
        Log.e("ContactsFragments"," his uid is : " + hisUid);
        return view;
    }


    private void seenMessages() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);

                   // if(chat.getTitleOfMsg().equals(msgTitle) )
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {
                        HashMap<String, Object> hasSeenhashMap = new HashMap<>();
                        hasSeenhashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hasSeenhashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void readMessages() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                  if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {

                        chatList.add(chat);
                   }
                    //adapter
                    adapterChat = new AdapterChat(getContext(), chatList, hisImage);
                    adapterChat.notifyDataSetChanged();

                    //set adapter to recyclerView
                    recyclerView.setAdapter(adapterChat);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);
        hashMap.put("titleOfMsg",smgTitle);
        databaseReference.child("Chats").push().setValue(hashMap);

        //reset the edittext after sending the message
        messageEt.setText("");
    }


    @Override
    public void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

}