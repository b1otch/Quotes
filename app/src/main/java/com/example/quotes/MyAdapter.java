package com.example.quotes;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    ArrayList<Dataset> dataHolder;
    String author, content;

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
        author = dataHolder.get(position).getAuthor();
        content = dataHolder.get(position).getContent();
        holder.authorView.setText(author);
        holder.contentView.setText(content);
        Log.i("2", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView contentView, authorView;
        Button shareButton, micButton;
        TextToSpeech textToSpeech;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.contentView);
            authorView = itemView.findViewById(R.id.authorView);
            shareButton = itemView.findViewById(R.id.shareButton);
            micButton = itemView.findViewById(R.id.micButton);
            Log.i("1", "1");

            textToSpeech = new TextToSpeech(itemView.getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i == TextToSpeech.SUCCESS) {
                        Log.i("success", "yes");
                        int language = textToSpeech.setLanguage(Locale.ENGLISH);
                    }
                }
            });

            micButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int speechContent = textToSpeech.speak(contentView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, contentView.getText().toString() + "\n -" +authorView.getText().toString());
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    itemView.getContext().startActivity(shareIntent);
                }
            });
        }
    }
}