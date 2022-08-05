package com.example.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ListOfQuotes extends AppCompatActivity {

    EditText editText;
    Button searchButton;
    String prompt;
    RecViewFragment recViewFragment;
    ArrayList<Dataset> filteredData, dataHolder;
    MyAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quotes);
        dataHolder = new ArrayList<Dataset>();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new RecViewFragment()).commit();

        editText = findViewById(R.id.editTextPrompt);
        searchButton = findViewById(R.id.searchButton);
        recViewFragment = new RecViewFragment();
        dataHolder = recViewFragment.dataHolder;
        searchAdapter = new MyAdapter(filteredData);
    }

    public void onClick(View view) {
        prompt = editText.getText().toString();
        if (prompt == "") {
            recViewFragment.recyclerView.setAdapter(recViewFragment.myAdapter);
        }
        else {
            int size = dataHolder.size();
            for (int i = 0; i < size; i++){
                if (recViewFragment.dataHolder.get(i).getContent().contains(prompt) || recViewFragment.dataHolder.get(i).getAuthor().contains(prompt)){
                    filteredData.add(recViewFragment.dataHolder.get(i));
                }
            }
            recViewFragment.recyclerView.setAdapter(searchAdapter);
        }
    }
}