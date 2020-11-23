package com.example.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentItemList extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    //    private DatabaseReference root;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference reff;
    private ItemAdapter itemAdapter;

    //declare the spinner
    Spinner listCategory;
    String chosenCategory;

    public static final String TAG = "ContainerAccessActivity";
    SharedPreferences sp;
    public FragmentItemList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewItemList);

        //Initialize Spinner values
        listCategory = (Spinner) view.findViewById(R.id.editTextListspinner);


        //Spinner values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.Status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listCategory.setAdapter(adapter);
        listCategory.setOnItemSelectedListener(this);

         sp = getActivity().getSharedPreferences("Categories", Context.MODE_PRIVATE);

        CategoryInitialize();
        itemAdapter.setOnItemclickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {


                Items items = documentSnapshot.toObject(Items.class);
                String id = documentSnapshot.getId();


                Intent intent = new Intent(getContext(), ItemDetails.class);
                intent.putExtra("id", id);
                intent.putExtra("category", items.getCategory());
                intent.putExtra("title", items.getTitle());
                intent.putExtra("date", items.getDate());
                intent.putExtra("description", items.getDescription());
                intent.putExtra("status", items.getStatus());
                intent.putExtra("image", items.getImage());
                startActivity(intent);

                //here we can delete and update an item
            }
        });
        return view;
    }


    private void setUprecycleView() {

        Toast.makeText(getContext(), "chosenCategory", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getActivity().getSharedPreferences("Categories", Context.MODE_PRIVATE);
        String chosenCat = sp.getString("cat","");

        //Query
        Query query = firebaseFirestore.collection("Item");
        FirestoreRecyclerOptions<Items> options;


        switch (chosenCat){
            case "Pet":
                query = query.whereEqualTo("category", "Pet").orderBy("date", Query.Direction.DESCENDING);

                break;
            case "Electronics":
                query = query.whereEqualTo("category", "Electronics").orderBy("date", Query.Direction.DESCENDING);
                break;
            case "Jewelry" :
                query = query.whereEqualTo("category", "Jewelry").orderBy("date", Query.Direction.DESCENDING);
                break;
            case "Cloth":
                query = query.whereEqualTo("category", "Cloth").orderBy("date", Query.Direction.DESCENDING);
                break;
            case "Other":
                query = query.whereEqualTo("category", "Other").orderBy("date", Query.Direction.DESCENDING);
                break;
            case "Book":
                query = query.whereEqualTo("category", "Book").orderBy("date", Query.Direction.DESCENDING);
                break;
            case "All":
                query = query.whereEqualTo("category", "All").orderBy("date", Query.Direction.DESCENDING);
                break;
        }
        options = new FirestoreRecyclerOptions.Builder<Items>()
                .setQuery(query, Items.class)
                .build();
        itemAdapter = new ItemAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.notifyDataSetChanged();
        itemAdapter.startListening();
//        Query query = reff.orderBy("date", Query.Direction.DESCENDING);//
//        FirestoreRecyclerOptions<Items> item = new FirestoreRecyclerOptions.Builder<Items>().setQuery(query, Items.class).build();//
//        itemAdapter = new ItemAdapter(item);//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(itemAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        String categoryText = adapterView.getItemAtPosition(position).toString();
        chosenCategory = categoryText;
          Toast.makeText(adapterView.getContext(), chosenCategory, Toast.LENGTH_SHORT).show();
          SharedPreferences.Editor editor = sp.edit();

          switch (categoryText){
              case "Pet":
                  editor.putString("cat","Pet");
                  editor.commit();
                  setUprecycleView();
                  break;
              case "Electronics":
                  editor.putString("cat","Electronic");
                  editor.commit();
                  setUprecycleView();
                  break;
              case "Jewelry" :
                  editor.putString("cat","Jewelry");
                  editor.commit();
                  setUprecycleView();
                  break;
              case "Cloth":
                  editor.putString("cat","Cloth");
                  editor.commit();
                  setUprecycleView();
                  break;
              case "Other":
                  editor.putString("cat","Other");
                  editor.commit();
                  setUprecycleView();
                  break;
              case "Book":
                  editor.putString("cat","Book");
                  editor.commit();
                  setUprecycleView();
                  break;
          }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    public void CategoryInitialize() {
        Query query3 = firebaseFirestore.collection("Item").whereEqualTo("Category", "Electronics");

        //RecyclerOptions
        FirestoreRecyclerOptions<Items> options = new FirestoreRecyclerOptions.Builder<Items>()
                .setQuery(query3, Items.class)
                .build();

        itemAdapter = new ItemAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
        //  setUprecycleView(chosenCategory);
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}


//        DatabaseReference itemRef = root.child("Item");

//        ValueEventListener eventListener = new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                for(DataSnapshot ds : dataSnapshot.getChildren())
//                {
//                    String category = ds.child("category").getValue(String.class);
//                    String date = ds.child("date").getValue(String.class);
//                    String description = ds.child("description").getValue(String.class);
//                    float lat = ds.child("lat").getValue(Float.class);
//                    float lon = ds.child("lon").getValue(Float.class);
//                    String status = ds.child("status").getValue(String.class);
//                    String title = ds.child("title").getValue(String.class);
//                    String userName = ds.child("userName").getValue(String.class);
//                    String image = ds.child("image").getValue(String.class);
//
//
//                    items.add(new Items(userName,title,lon,lat,description,category,date,status, image));
//

//
//        List<Items> items = new ArrayList<>();
//
//       // root = FirebaseDatabase.getInstance().getReference();
//
//
//
//        DatabaseReference itemRef = root.child("Item");

//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String category = ds.child("category").getValue(String.class);
//                    String date = ds.child("date").getValue(String.class);
//                    String description = ds.child("description").getValue(String.class);
//                    float lat = ds.child("lat").getValue(Float.class);
//                    float lon = ds.child("lon").getValue(Float.class);
//                    String status = ds.child("status").getValue(String.class);
//                    String title = ds.child("title").getValue(String.class);
//                    String userName = ds.child("userName").getValue(String.class);
//                    String image = ds.child("image").getValue(String.class);
//
//                    items.add(new Items(userName, title, lon, lat, description, category, date, status, image));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                //Error handler
//            }
//        };


//        itemRef.addListenerForSingleValueEvent(eventListener);
//
//
//        ItemAdapter itemAdapter = new ItemAdapter(items);
//
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewItemList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(itemAdapter);