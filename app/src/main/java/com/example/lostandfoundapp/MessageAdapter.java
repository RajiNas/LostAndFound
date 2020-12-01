package com.example.lostandfoundapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder>
{
    private MessageAdapter.OnItemClickListener listener;


    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageAdapter.MessageHolder holder, int position, @NonNull Message message)
    {




    }

    @NonNull
    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);

        return new MessageAdapter.MessageHolder(view);
    }



    class MessageHolder extends RecyclerView.ViewHolder
    {

        TextView category, title, date;
        CardView cv;
        ImageView imgItem;
        LinearLayout p_layout;


        public MessageHolder(@NonNull View itemView)
        {

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

    public void setOnMessageClickListener(ItemAdapter.OnItemClickListener listener){
        this.listener = (OnItemClickListener) listener;
    }
}
