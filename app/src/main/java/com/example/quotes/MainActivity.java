package com.example.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DownloadTask downloadTask;
    TagDownload tagDownload;
    TextView textViewQuote, textViewAuthor;
    String randomQuote;
    ArrayList<String> tags, tags1;
    Intent intent;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        downloadTask = new DownloadTask();
        downloadTask.execute("https://quotable.io/random");

        tagDownload = new TagDownload();
        tags = new ArrayList<>();
        tagDownload.execute("https://quotable.io/tags");

        textViewQuote = findViewById(R.id.textViewQuote);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        tags1 = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                return result;
            }

            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                randomQuote = jsonObject.getString("content");
                textViewQuote.setText(randomQuote);
                String author = jsonObject.getString("author");
                textViewAuthor.setText(author);
                progressBar.setVisibility(View.INVISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class TagDownload extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "{\"array\":";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    result += (char) data;
                    data = inputStreamReader.read();
                }

                result = result + "}";
                return result;
            }

            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray tagsArray = jsonObject.getJSONArray("array");
                int size = tagsArray.length();
                for (int i = 0; i < size; i++) {
                    tags.add(tagsArray.getJSONObject(i).getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("tags", tags.toString());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tags.size(); i++) {
                sb.append(tags.get(i)).append(",");
            }
            sharedPreferences.edit().putString("tags", sb.toString()).commit();
        }
    }

    public void goToList(View view) {
        Intent intent = new Intent(getApplicationContext(), ListOfQuotes.class);
        startActivity(intent);
    }
}