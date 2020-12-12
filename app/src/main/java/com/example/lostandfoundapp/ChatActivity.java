package com.example.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.internal.LinkedHashTreeMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //init views
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("");
        recyclerView = (RecyclerView) findViewById(R.id.chat_recyclerView);
        profileIv = (ImageView) findViewById(R.id.profileIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        userStatusTv = (TextView) findViewById(R.id.userStatusTv);
        messageEt = (EditText) findViewById(R.id.messageEt);
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);

        //Layout for recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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

        hisUid = getIntent().getStringExtra("hisId");
        userName = getIntent().getStringExtra("username");
        hisImage = getIntent().getStringExtra("image");
        Toast.makeText(ChatActivity.this, "hisUid: " + hisUid, Toast.LENGTH_SHORT).show();
        Toast.makeText(ChatActivity.this, "myUid: " + myUid, Toast.LENGTH_SHORT).show();


        //search user to get that user's info
        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);
        nameTv.setText(userName);
        Picasso.get().load(hisImage).into(profileIv);

//        //get picture and name
//        userQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //check until required info is received
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    //get data
//                    String name = "" + ds.child("username").getValue();
//                    hisImage = "" + ds.child("image").getValue();
////                    Log.e("ChatsActivity", "username: " + name + "\nimage: " + hisImage);
//
//                    //set data
//                    nameTv.setText(name);
//                    try {
//                        // image received, set it to imageView
//                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_default_img_white).into(profileIv);
//                    } catch (Exception e) {
//                        //error getting the picture, set the default image
//                        Picasso.get().load(R.drawable.ic_default_img_white).into(profileIv);
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        //click button to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edit text
                String message = messageEt.getText().toString().trim();
                //check if text is empty
                if (TextUtils.isEmpty(message)) {
                    //text empty
                    Toast.makeText(ChatActivity.this, "Cannot send an empty message...", Toast.LENGTH_SHORT).show();
                } else {
                    //text not empty
                    sendMessage(message);
                }
            }
        });
        readMessages();
        seenMessages();
    }

    private void seenMessages() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
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
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                        chatList.add(chat);
                    }
                    //adapter
                    adapterChat = new AdapterChat(ChatActivity.this, chatList, hisImage);
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
        databaseReference.child("Chats").push().setValue(hashMap);

        //reset the edittext after sending the message
        messageEt.setText("");
    }

//    private void checkUserStatus() {
//        //get current user
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        if (user != null) {
//            //user is signed in
//            //set email of logged in user
//            myUid = user.getUid();
//
//        } else {
//            //user not signed in, go to main activity
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        checkUserStatus();
//        super.onStart();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        //hide the search bar
//
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
}