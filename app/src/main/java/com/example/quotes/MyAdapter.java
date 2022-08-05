package com.example.quotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    ArrayList<Dataset> dataHolder;

    public MyAdapter(ArrayList<Dataset> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.authorView.setText(dataHolder.get(position).getAuthor());
        holder.contentView.setText(dataHolder.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView contentView, authorView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.contentView);
            authorView = itemView.findViewById(R.id.authorView);
        }
    }
}
