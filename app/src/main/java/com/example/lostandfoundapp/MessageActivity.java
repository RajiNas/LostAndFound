package com.example.lostandfoundapp;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {

    private FirebaseListAdapter<Message> adapter;

    EditText TextMessage;
    ImageView submitButton;
    String contact;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        Intent intent = getIntent();
        contact = intent.getStringExtra("contact");


        submitButton = (ImageView)findViewById(R.id.submit_button);
        TextMessage = findViewById(R.id.text_message);

        displayChatMessage(contact);

        // Sends message.
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseDatabase.getInstance().getReference().child("Message").push().setValue(new Message(TextMessage.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getProviderId(),contact));

                TextMessage.setText("");
                TextMessage.requestFocus();
                displayChatMessage(contact);
            }
        });


    }

    @Override
    public void onResume()
    {
        super.onResume();
        displayChatMessage(contact);
    }


// this is a comment that was added to comment something

    // Gets all messages but only displays the messages between the current user and the current contact (that is, the person who the user is talking to).
    private void displayChatMessage(String contact)
    {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<Message>(this,Message.class,R.layout.message, FirebaseDatabase.getInstance().getReference().child("Message"))
        {
            @Override
            protected void populateView(View v, Message model, int position)
            {

                Log.e("MessageActivity",model.getContact());
                if (model.getContact().equals(contact) && ! model.getContact().isEmpty())
                {
                    TextView messageText, messageUser, messageTime;
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);
                    messageText =  (TextView) v.findViewById(R.id.text_send);

                    messageUser.setText(model.getCurrentUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()) );
                    messageText.setText(model.getMessageText());
                }

            }
        };
        listOfMessage.setAdapter(adapter);
    }

    public void previousActivity(View view) {
        Intent intent = new Intent(MessageActivity.this, ContactActivity.class);
        startActivity(intent);
    }
}

