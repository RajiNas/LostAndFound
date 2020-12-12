package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ContactActivity extends AppCompatActivity
{
    private FirebaseFirestore firebaseFirestore;
    private FirebaseListAdapter<Users> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_list);
        displayUsers();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        displayUsers();
    }

    // comment line
    private void displayUsers()
    {
        ListView listOfUsers = (ListView)findViewById(R.id.ContactListView);
        adapter = new FirebaseListAdapter<Users>(this,Users.class,R.layout.fragment_contact, FirebaseDatabase.getInstance().getReference().child("Users") )
        {
            @Override
            protected void populateView(View v, Users model, int position)
            {
                TextView username;
                ImageView profile_pic;
                CardView cv;
                username = (TextView) v.findViewById(R.id.user_name);
                profile_pic = (ImageView) v.findViewById(R.id.profile_icon);
                cv = findViewById(R.id.cv_contacts);

                if (model.getImage().isEmpty())
                {
                    profile_pic.setImageResource(R.drawable.bg_profile_green);
                }
                else
                {
                    username.setText(model.getUsername());
                    Picasso.get()
                            .load(model.getImage())
                            .resize(50, 50)
                            .centerCrop()
                            .into(profile_pic);
                }

                profile_pic.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        String contact = "contact";
                        intent.putExtra(contact,model.getUsername());
                        startActivity(intent);
                    }
                });
            }
        };
        listOfUsers.setAdapter(adapter);
    }

    public void previousActivity(View view) {
        Intent intent = new Intent(ContactActivity.this, ContainerAccessActivity.class);
        startActivity(intent);
    }
}
