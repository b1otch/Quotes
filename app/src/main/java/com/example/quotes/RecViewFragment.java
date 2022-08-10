package com.example.quotes;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ArrayList<Dataset> dataHolder;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollItems;
    LinearLayoutManager layoutManager;
    MyAdapter myAdapter;
    ProgressBar progressBar;
    ListOfQuotes listOfQuotes;
    TextToSpeech textToSpeech;


    public RecViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecViewFragment newInstance(String param1, String param2) {
        RecViewFragment fragment = new RecViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        layoutManager = new LinearLayoutManager(getContext());
        dataHolder = new ArrayList<>();
        myAdapter = new MyAdapter(dataHolder);
        listOfQuotes = new ListOfQuotes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rec_view, container, false);
        recyclerView = view.findViewById(R.id.recview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        String prompt = listOfQuotes.prompt;

        recyclerView.setLayoutManager(layoutManager);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://quotable.io/quotes?page=1");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(myAdapter);
            }
        }, 500);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int i = 2;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollItems = layoutManager.findFirstVisibleItemPosition();
                Log.i("numbers", String.valueOf(currentItems) + " " + scrollItems + " " + totalItems);

                if (isScrolling && (currentItems + scrollItems == totalItems)) {

                    progressBar.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 500);

                    myAdapter.notifyDataSetChanged();
                    if (i < 103) {
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute("https://quotable.io/quotes?page=" + String.valueOf(i));
                        i++;
                    }
                }
            }
        });

        return view;
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
                for (int i = 0; i < 20; i++) {
                    JSONObject jsonObject1 = jsonObject.getJSONArray("results").getJSONObject(i);
                    String content = jsonObject1.getString("content");
                    String author = jsonObject1.getString("author");
                    JSONArray jsonTags = jsonObject1.getJSONArray("tags");
                    String[] tags = new String[jsonTags.length()];
                    for(int j = 0; j < jsonTags.length(); j++) {
                        tags[j] = jsonTags.getString(j);
                    }
                    String id = jsonObject1.getString("_id");
                    Dataset dataset = new Dataset(author, content, tags, id, false);
                    dataHolder.add(dataset);
                }
                Log.i("author", String.valueOf(dataHolder.size()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myAdapter.notifyDataSetChanged();
        }
    }
}