package com.example.hp.mockapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MovieAttributes> movieAttributes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Spinner spinner;
    private String genre;
    private ProgressBar mLoadingIndicator;
    private LinearLayout mLinearLayout;
    private TextView mErrorMessageDisplay;

    public static Boolean hasNetwork(Context context) {
        Boolean isThereNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            isThereNetwork = true;
        return isThereNetwork;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movies);
        spinner = (Spinner) findViewById(R.id.filter_spinner);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mLinearLayout = (LinearLayout) findViewById(R.id.movie_list_linear_layout);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        Boolean hasConnectivity = hasNetwork(this);
        Log.d("Connectivity", String.valueOf(hasConnectivity));
        if (!hasConnectivity) {
            mLinearLayout.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_filter, R.layout.spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                    genre = (String) parent.getItemAtPosition(pos);
                    if (genre.compareTo("Popular") == 0)
                        genre = "popular";
                    else
                        genre = "top_rated";
                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mMovieAdapter = new MovieAdapter(getApplicationContext(), new MovieAdapter.MovieAdapterOnClickHandler() {
                        @Override
                        public void handleClicks(int position) {
                            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                            intent.putExtra("id", String.valueOf(movieAttributes.get(position).get_id()));
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mMovieAdapter);
                    new FetchMoviesTask().execute(genre);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 20, true));
        }
    }

    public void parseJson(String json) {
        movieAttributes.clear();
        try {

            JSONObject jsonObj = new JSONObject(json);
            JSONArray results = jsonObj.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject c = results.getJSONObject(i);
                MovieAttributes obj = new MovieAttributes();
                obj.set_poster_path(c.getString("poster_path"));
                obj.set_movie_id(c.getInt("id"));
                movieAttributes.add(obj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMovieAdapter.setMovieData(movieAttributes);

    }

    private class FetchMoviesTask extends AsyncTask<String, Void, String> {
        private String movieListResponse;
        private String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String genre = strings[0];
            URL movieListUrl = NetworkUtils.buildUrlForMovieList(genre);
            try {
                movieListResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieListResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            json = result;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
            parseJson(json);
        }
    }
}