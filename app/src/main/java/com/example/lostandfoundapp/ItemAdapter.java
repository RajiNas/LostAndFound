package com.example.lostandfoundapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
  This class will help displaying each individual item
  I am not sure if two are going to be needed. What I am trying to ask if there will there be 2 different adapters for
  lost items and found items individual or we only need one (preferably).
  I do not see a variable that indicates whether
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter .ViewHolder>
{
    List<Items> itemsList;
    Context context;

    public ItemAdapter (List<Items> list)
    {
        this.itemsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false); Idk which layout
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        Items item = itemsList.get(position);

        //if (isFound) This single adapter can work for both if a variable is given to identify this.

        holder.username.setText(item.getUserName());
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.date.setText(item.getDate());

        holder.cv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfilFragment.class);
//                String stuff = "";
//                intent.putExtra(stuff, stuff);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, title,description,date;
        CardView cv;

        public ViewHolder(View itemView)
        {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.);
            title = (TextView) itemView.findViewById(R.id.);
            description = (TextView) itemView.findViewById(R.id.);
            date = (TextView) itemView.findViewById(R.id.);
            cv = (CardView) itemView.findViewById(R.id.cv);
        }

    }
}

