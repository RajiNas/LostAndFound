package com.example.lostandfoundapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContactsFragmentList extends Fragment implements AdapterView.OnItemSelectedListener
{

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private MessageAdapter contactAdapter;

    //declare the spinner
    Spinner listCategory;
    String chosenCategory;

    // Identify the current user
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    public static final String TAG = "ContainerAccessActivity";
    SharedPreferences sp;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    public ContactsFragmentList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewContactList);
        CollectionReference reff = firebaseFirestore.collection("Message");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        //Spinner values

        sp = getActivity().getSharedPreferences("Categories", Context.MODE_PRIVATE);

        contactsInitialize();
        contactAdapter.setOnMessageClickListener(new ItemAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position)
            {


                Items items = documentSnapshot.toObject(Items.class);
                String id = documentSnapshot.getId();


                Intent intent = new Intent(getContext(), MessageActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("category", items.getCategory());
                intent.putExtra("title", items.getTitle());
                intent.putExtra("date", items.getDate());
                intent.putExtra("description", items.getDescription());
                intent.putExtra("status", items.getStatus());
                intent.putExtra("image", items.getImage());
                intent.putExtra("address", items.getAddress());
                startActivity(intent);

            }
        });
        return view;
    }





    public void contactsInitialize() {
        Query query = firebaseFirestore.collection("Message");

        //RecyclerOptions
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        contactAdapter = new MessageAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onStart() {
        super.onStart();
        contactAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        contactAdapter.stopListening();
    }
}
