package com.example.quotes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceDialog extends DialogFragment {

    MainActivity mainActivity;
    String[] tags, finalTags;
    ArrayList<String> selectedTags, tagsList;
    SharedPreferences sharedPreferences;

    public interface onMultiChoiceListener {
        void onPositiveButtonCLick(String[] list, ArrayList<String> selectedTags);
        void onNegativeButtonClick();
    }

    onMultiChoiceListener multiChoiceListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            multiChoiceListener = (onMultiChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + "onMultiClickListener must be implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mainActivity = new MainActivity();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tags = sharedPreferences.getString("tags", "").split(",");
        Log.i("tags", tags.toString());
        selectedTags = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose the tags")
                .setMultiChoiceItems(tags, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        Log.i("tags", tags[i]);
                        if (b) {
                            selectedTags.add(tags[i]);
                        } else {
                            selectedTags.remove(tags[i]);
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("selectedTags", selectedTags.toString());
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < selectedTags.size(); j++) {
                            sb.append(selectedTags.get(j)).append(",");
                        }
                        Log.i("sb", sb.toString());
                        sharedPreferences.edit().putString("selectedTags", sb.toString()).commit();
                        multiChoiceListener.onPositiveButtonCLick(tags, selectedTags);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        multiChoiceListener.onNegativeButtonClick();
                        selectedTags = new ArrayList<>();
                    }
                });
        return builder.create();
    }
}
