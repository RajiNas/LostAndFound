package com.example.lostandfoundapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentItemList extends Fragment
{
    private RecyclerView recyclerView;
    private DatabaseReference root;

    public FragmentItemList () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        List<Items> items = new ArrayList<>();

        root = FirebaseDatabase.getInstance().getReference();

        DatabaseReference itemRef = root.child("Item");

        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String category = ds.child("category").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String description = ds.child("description").getValue(String.class);
                    float lat = ds.child("lat").getValue(Float.class);
                    float lon = ds.child("lon").getValue(Float.class);
                    String status = ds.child("status").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    String userName = ds.child("userName").getValue(String.class);

                    items.add(new Items(userName,title,lon,lat,description,category,date,status));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //Error handler
            }
        };

        itemRef.addListenerForSingleValueEvent(eventListener);



        ItemAdapter itemAdapter = new ItemAdapter(items);


        recyclerView = (RecyclerView)view.findViewById(R.id.ItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter);

        return view;
    }
}

