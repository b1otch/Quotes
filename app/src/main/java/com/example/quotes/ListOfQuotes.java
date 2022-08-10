package com.example.quotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Objects;

public class ListOfQuotes extends AppCompatActivity implements MultipleChoiceDialog.onMultiChoiceListener {

    EditText editText;
    Button searchButton;
    String prompt;
    RecViewFragment recViewFragment;
    ArrayList<Dataset> filteredData;
    MyAdapter searchAdapter;
    Button homepageButton, filterButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quotes);
        filteredData = new ArrayList<>();
        recViewFragment = new RecViewFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, recViewFragment).commit();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        editText = findViewById(R.id.editTextPrompt);
        searchButton = findViewById(R.id.searchButton);
        homepageButton = findViewById(R.id.homepageButton);
        filterButton = findViewById(R.id.filterButton);

        homepageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment multiChoiceDialog = new MultipleChoiceDialog();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
            }
        });
    }

    public void onClick(View view) {
        filteredData = new ArrayList<>();
        searchAdapter = new MyAdapter(filteredData);
        prompt = editText.getText().toString();
        if (prompt == "") {
            recViewFragment.recyclerView.setAdapter(recViewFragment.myAdapter);
            recViewFragment.myAdapter.notifyDataSetChanged();
        }
        else {
            int size = recViewFragment.dataHolder.size();
            Log.i("author1", String.valueOf(size));
            for (int i = 0; i < size; i++){
                if (recViewFragment.dataHolder.get(i).getContent().contains(prompt) || recViewFragment.dataHolder.get(i).getAuthor().contains(prompt)){
                    filteredData.add(recViewFragment.dataHolder.get(i));
                }
            }
            for (int i = 0; i < filteredData.size(); i++) {
                Log.i("author", filteredData.get(i).getAuthor());
                Log.i("author1", searchAdapter.dataHolder.get(i).getAuthor());
            }
            Log.i("alt", searchAdapter.toString());
            recViewFragment.recyclerView.setAdapter(searchAdapter);
        }
    }

    @Override
    public void onPositiveButtonCLick(String[] list, ArrayList<String> selectedTags) {
        filteredData = new ArrayList<>();
        searchAdapter = new MyAdapter(filteredData);
        String[] finalTags1 = sharedPreferences.getString("selectedTags", "").split(",");
        int size = recViewFragment.dataHolder.size();

        for (int i = 0; i < size; i++){
            for (int j = 0; j < recViewFragment.dataHolder.get(i).getTags().length; j++) {
                for (int k = 0; k < finalTags1.length; k++) {
                    if (Objects.equals(finalTags1[k], recViewFragment.dataHolder.get(i).getTags()[j])) {
                        filteredData.add(recViewFragment.dataHolder.get(i));
                        Log.i("huh", finalTags1[k] + " + "  + recViewFragment.dataHolder.get(i).getTags()[j]);
                    }
                }
            }
        }
        Log.i("filteredData", filteredData.toString());
        recViewFragment.recyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onNegativeButtonClick() {
        recViewFragment.recyclerView.setAdapter(recViewFragment.myAdapter);
    }
}