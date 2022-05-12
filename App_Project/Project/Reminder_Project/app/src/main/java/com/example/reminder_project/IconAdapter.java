package com.example.reminder_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.MyViewHolder> {

    private ArrayList<Integer> icons;
    private ItemClickListener mClickListener;

    public IconAdapter(ArrayList<Integer> icons) {
        this.icons = icons;
    }

    @NonNull
    @Override
    public IconAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_icon, parent, false);
        return new IconAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconAdapter.MyViewHolder holder, int position) {
        holder.img.setImageResource(icons.get(position));
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView5);
            itemView.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(itemView.getContext(), "ahhhhhhhhhhhhhhhhhhhhhhhhhh", Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

