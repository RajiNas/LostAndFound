
package com.example.lostandfoundapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
  This class will help displaying each individual item
  I am not sure if two are going to be needed. What I am trying to ask if there will there be 2 different adapters for
  lost items and found items individual or we only need one (preferably).
  I do not see a variable that indicates whether
 */

public class ItemAdapter extends FirestoreRecyclerAdapter<Items, ItemAdapter.ItemHolder> {
    private OnItemClickListener listener;
    //    FirestoreRecyclerOptions<Items> options;
//    Context mContext;

    public ItemAdapter(@NonNull FirestoreRecyclerOptions<Items> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Items items) {

        holder.category.setText(items.getCategory());
        holder.title.setText(items.getTitle());
        holder.date.setText(items.getDate());
        Picasso.get().load(items.getImage()).into(holder.imgItem);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);

        return new ItemHolder(view);
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView category, title, date;
        CardView cv;
        ImageView imgItem;
        LinearLayout p_layout;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.itemTitle);
            category = (TextView) itemView.findViewById(R.id.itemCategory);
            date = (TextView) itemView.findViewById(R.id.itemDate);
            cv = (CardView) itemView.findViewById(R.id.cv);
            imgItem = (ImageView) itemView.findViewById(R.id.ItemimageviewFragment);
            p_layout = (LinearLayout) itemView.findViewById(R.id.parent_layout);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemclickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}


//____________________________________________________________________

// i commented the recycle adapter because that it won't work with the firebase

//For this project we had to use FirestoreRecycleAdapter, science we have to fetch from the firestore
//for info on how i did it follow this link : https://www.youtube.com/watch?v=lAGI6jGS4vs .


//___________________________________________________________

//    List<Items> itemsList;
//    Context context;
//
//    public ItemAdapter() {
//    }
//
//    public ItemAdapter(List<Items> itemsList, Context context) {
//        this.itemsList = itemsList;
//        this.context = context;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
//    {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
//
//        ViewHolder viewHolder = new ViewHolder(view);
//        context = parent.getContext();
//        return viewHolder;
//
//
//
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position)
//    {
//      //  Items item = itemsList.get(position);
//
//
//        holder.category.setText(itemsList.get(position).getCategory());
//        holder.title.setText(itemsList.get(position).getTitle());
//        holder.date.setText(itemsList.get(position).getDate());
//        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/9%2C_Strada_Sp%C4%83tarului%2C_Bucharest_%28Romania%29.jpg/180px-9%2C_Strada_Sp%C4%83tarului%2C_Bucharest_%28Romania%29.jpg").fit().into(holder.imgItem);
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return itemsList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView category, title,date;
//        CardView cv;
//        ImageView imgItem;
//        public ViewHolder(View itemView)
//        {
//            super(itemView);
//            title = (TextView) itemView.findViewById(R.id.itemTitle);
//            category= (TextView) itemView.findViewById(R.id.itemCategory);
//            date = (TextView) itemView.findViewById(R.id.itemDate);
//            cv = (CardView) itemView.findViewById(R.id.cv);
//            imgItem =(ImageView) itemView.findViewById(R.id.ItemimageviewFragment);
//        }
//
//    }
//}




