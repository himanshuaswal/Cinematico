package com.example.hp.mockapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.mockapp.data.TestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<MovieAttributes> movieAttributes = new ArrayList<>();
    Cursor cursor = null;
    private MovieAdapter mMovieAdapter;
    private String genre;
    private ArrayList<String> movieTrailerKeys = new ArrayList<>();
    private ArrayList<String> movieVideoNames = new ArrayList<>();
    private ArrayList<String> movieImageFilePaths = new ArrayList<>();
    private GridRecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private AsyncTask<String, Void, String> mTask;
    private LinearLayout mLinearLayout;
    private SQLiteDatabase fDB;
    private boolean flag = false;
    private TextView mErrorMessageDisplay;
    private Spinner spinner;

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
        Toast.makeText(this, R.string.developer_name, Toast.LENGTH_LONG).show();
        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        fDB = dbHelper.getWritableDatabase();
        mRecyclerView = findViewById(R.id.recycler_view_movies);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLinearLayout = findViewById(R.id.movie_list_linear_layout);
        mErrorMessageDisplay = findViewById(R.id.error_message_display);
        spinner = findViewById(R.id.filter_spinner);
        Boolean hasConnectivity = hasNetwork(this);
        if (!hasConnectivity) {
            mLinearLayout.setVisibility(View.INVISIBLE);
            //mErrorMessageDisplay.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        } else {
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_filter, R.layout.spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int pos, long l) {
                    genre = (String) parent.getItemAtPosition(pos);
                    if (genre.compareTo("Popular") == 0) {
                        genre = "popular";
                        mTask = new FetchMoviesTask().execute(genre);
                    } else if (genre.compareTo("Favorites") == 0) {
                        movieAttributes.clear();
                        mTask.cancel(true);
                        cursor = mMovieAdapter.swapCursor(getAllFavoriteMovies(), cursor);
                        try {
                            while (cursor.moveToNext()) {
                                MovieAttributes obj = new MovieAttributes();
                                obj.set_poster_path(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH)));
                                obj.set_original_title(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE)));
                                obj.set_overview(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW)));
                                obj.set_vote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE))));
                                obj.set_release_date(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE)));
                                obj.set_vote_count(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT))));
                                obj.set_movie_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID))));
                                obj.set_backdrop_path(cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH)));
                                movieAttributes.add(obj);
                            }
                        } finally {
                            cursor.close();
                        }
                        flag = true;
                    } else {
                        genre = "top_rated";
                        mTask = new FetchMoviesTask().execute(genre);
                    }
                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mMovieAdapter = new MovieAdapter(getApplicationContext(), new MovieAdapter.MovieAdapterOnClickHandler() {

                        @Override
                        public void handleClicks(int position) {
                            Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                            MovieAttributes dataToSend = movieAttributes.get(position);

                            intent.putExtra("myDataKey", dataToSend);
                            intent.putStringArrayListExtra("Movie Trailer Keys", movieTrailerKeys);
                            intent.putStringArrayListExtra("Movie Video Names", movieVideoNames);
                            intent.putStringArrayListExtra("Movie Image File Paths", movieImageFilePaths);
                            startActivity(intent);
                        }

                    });
                    mRecyclerView.setAdapter(mMovieAdapter);
                    if (flag == true) {
                        mMovieAdapter.setMovieData(movieAttributes);
                        mMovieAdapter.notifyDataSetChanged();
                        runLayoutAnimation();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, 20, true));
        }
    }

    private Cursor getAllFavoriteMovies() {
        return fDB.query(FavoritesContract.FavoritesSpec.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
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
                obj.set_original_title(c.getString("original_title"));
                obj.set_overview(c.getString("overview"));
                obj.set_vote_average(c.getDouble("vote_average"));
                obj.set_release_date(c.getString("release_date"));
                obj.set_vote_count(c.getInt("vote_count"));
                obj.set_movie_id(c.getInt("id"));
                obj.set_backdrop_path(c.getString("backdrop_path"));
                movieAttributes.add(obj);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMovieAdapter.setMovieData(movieAttributes);
        mMovieAdapter.notifyDataSetChanged();
        runLayoutAnimation();


    }

    private void runLayoutAnimation() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.grid_layout_animation_from_bottom);
        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.scheduleLayoutAnimation();
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