package com.example.lostandfoundapp;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//
//import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MessageActivity extends AppCompatActivity {
//
//    private static int SIGN_IN_REQUEST_CODE = 1;
//    private FirebaseListAdapter<Message> adapter;
//    RelativeLayout activity_main;
//
//    //Add Emojicon
//    EditText TextMessage;
//    ImageView submitButton;
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
////        if(item.getItemId() == R.id.menu_sign_out)
////        {
////            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    Snackbar.make(activity_main,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
////                    finish();
////                }
////            });
////        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == SIGN_IN_REQUEST_CODE)
//        {
//            if(resultCode == RESULT_OK)
//            {
//                Snackbar.make(activity_main,"Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
//                displayChatMessage();
//            }
//            else{
//                Snackbar.make(activity_main,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.message_activity);
//
//        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
//
//        //Add Emoji
//        submitButton = (ImageView)findViewById(R.id.submit_button);
//        TextMessage = findViewById(R.id.text_message);
//
//
//        submitButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                FirebaseDatabase.getInstance().getReference().push().setValue(new Message(TextMessage.getText().toString(),
//                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
//                TextMessage.setText("");
//                TextMessage.requestFocus();
//            }
//        });
//
//        //Check if not sign-in then navigate Signin page
//        if(FirebaseAuth.getInstance().getCurrentUser() == null)
//        {
//            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
//        }
//        else
//        {
//            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
//            //Load content
//            displayChatMessage();
//        }
//
//
//    }
//
//
//
//    private void displayChatMessage() {
//
//        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
//        adapter = new FirebaseListAdapter<Message>(this,Message.class,R.layout.list_message, FirebaseDatabase.getInstance().getReference())
//        {
//            @Override
//            protected void populateView(View v, Message model, int position) {
//
//                //Get references to the views of list_item.xml
//                TextView messageText, messageUser, messageTime;
//                messageUser = (TextView) v.findViewById(R.id.message_user);
//                messageTime = (TextView) v.findViewById(R.id.message_time);
//
//
//                messageUser.setText(model.getMessageText());
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//
//            }
//        };
//        listOfMessage.setAdapter(adapter);
//    }
//}
//
