package com.example.hp.mockapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {

    MovieAttributes object;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private ImageView mPosterPath;
    private TextView mVoteCount;
    private int movieId;
    private ArrayList<String> movieTrailerKeys = new ArrayList<>();
    private ArrayList<String> movieVideoNames = new ArrayList<>();
    private ArrayList<String> movieReviews = new ArrayList<>();
    private ArrayList<String> movieReviewAuthor = new ArrayList<>();
    private RecyclerView mTrailerRecyclerView;
    private ImageButton mFavoriteButton;
    private RecyclerView mReviewRecyclerView;
    private MovieTrailersAdapter movieDetailsAdapter;
    private MovieReviewsAdapter mMovieReviewAdapter;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mOriginalTitle = findViewById(R.id.original_title);
        mOverview = findViewById(R.id.overview);
        mVoteAverage = findViewById(R.id.vote_average);
        mReleaseDate = findViewById(R.id.release_date);
        mPosterPath = findViewById(R.id.poster_image);
        mVoteCount = findViewById(R.id.vote_count);
        mTrailerRecyclerView = findViewById(R.id.recycler_view_movie_trailers);
        mReviewRecyclerView = findViewById(R.id.recycler_view_movie_reviews);
        Intent intentThatStartedThisActivity = getIntent();
        object = intentThatStartedThisActivity.getParcelableExtra("myDataKey");
        movieId = object.get_movie_id();
        movieTrailerKeys = intentThatStartedThisActivity.getStringArrayListExtra("Movie Trailer Keys");
        movieVideoNames = intentThatStartedThisActivity.getStringArrayListExtra("Movie Video Names");
        LinearLayoutManager movieTrailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager movieReviewLayoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(movieReviewLayoutManager);
        mTrailerRecyclerView.setLayoutManager(movieTrailerLayoutManager);
        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        database = dbHelper.getWritableDatabase();
        mFavoriteButton = findViewById(R.id.favorite_button);
        if (CheckIsDataAlreadyInDborNot())
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_selected);
        else
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_diselected);
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = CheckIsDataAlreadyInDborNot();
                if (flag == false) {
                    mFavoriteButton.setImageResource(R.drawable.ic_favorite_selected);
                    new FavoritesTask().execute("ADD");
                } else {
                    mFavoriteButton.setImageResource(R.drawable.ic_favorite_diselected);
                    removeFromFavorites();
                    CookieBar.build(MovieDetails.this)
                            .setTitle(R.string.remove_fav)
                            .setBackgroundColor(R.color.md_red_700)
                            .setLayoutGravity(Gravity.TOP)
                            .setDuration(2000)
                            .show();
                }
                bounceAnimation();
            }
        });
        movieDetailsAdapter = new MovieTrailersAdapter(this, object.get_backdrop_path());
        mMovieReviewAdapter = new MovieReviewsAdapter(this);
        mReviewRecyclerView.setAdapter(mMovieReviewAdapter);
        mTrailerRecyclerView.setAdapter(movieDetailsAdapter);
        mTrailerRecyclerView.setNestedScrollingEnabled(false);
        new FetchTrailersTask().execute(movieId);
        new FetchReviewsTask().execute(movieId);
        showDetails(object);
    }

    private boolean removeFromFavorites() {
        return database.delete(FavoritesContract.FavoritesSpec.TABLE_NAME, FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(object.get_movie_id())}) > 0;
    }

    private boolean CheckIsDataAlreadyInDborNot() {
        String Query = "Select * from " + FavoritesContract.FavoritesSpec.TABLE_NAME + " where " + FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID + " = " + String.valueOf(object.get_movie_id());
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private void addToFavorites() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE, object.get_original_title());
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW, object.get_overview());
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH, object.get_poster_path());
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE, object.get_release_date());
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE, String.valueOf(object.get_vote_average()));
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT, String.valueOf(object.get_vote_count()));
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID, String.valueOf(movieId));
        contentValues.put(FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH, object.get_backdrop_path());
        try {
            database.beginTransaction();
            database.insert(FavoritesContract.FavoritesSpec.TABLE_NAME, null, contentValues);
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    private void bounceAnimation() {
        final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        mFavoriteButton.startAnimation(myAnim);

    }

    private void showDetails(MovieAttributes object) {
        mOriginalTitle.setText(object.get_original_title());
        mVoteCount.setText(String.valueOf(object.get_vote_count()));
        mVoteCount.append(" votes");
        mReleaseDate.setText(object.get_release_date());
        mVoteAverage.setText(String.valueOf(object.get_vote_average()) + "/10");
        mOverview.setText(object.get_overview());
        movieId = object.get_movie_id();
        Glide
                .with(this)
                .load(MovieAdapter.BASE_URL + object.get_poster_path())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter().placeholder(R.color.colorAccent)
                .into(mPosterPath);
    }

    private void parseJsonTrailerResponse(String json) {
        movieTrailerKeys.clear();
        movieVideoNames.clear();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                movieTrailerKeys.add(c.getString("key"));
                movieVideoNames.add(c.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieDetailsAdapter.setMovieTrailersData(movieVideoNames, movieTrailerKeys);
        movieDetailsAdapter.notifyDataSetChanged();
    }

    private void parseJsonReviewResponse(String json) {
        movieReviews.clear();
        movieReviewAuthor.clear();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                movieReviewAuthor.add(c.getString("author"));
                movieReviews.add(c.getString("content"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMovieReviewAdapter.setMovieReviewsData(movieReviewAuthor, movieReviews);
        mMovieReviewAdapter.notifyDataSetChanged();

    }

    private class FavoritesTask extends AsyncTask<String, Void, Void> {
        String task;

        @Override
        protected Void doInBackground(String... strings) {
            task = strings[0];
            if (task.equals("ADD"))
                addToFavorites();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CookieBar.build(MovieDetails.this)
                    .setTitle(R.string.add_fav)
                    .setBackgroundColor(R.color.md_cyan_300)
                    .setLayoutGravity(Gravity.BOTTOM)
                    .setDuration(2000)
                    .show();
        }
    }

    private class FetchTrailersTask extends AsyncTask<Integer, Void, String> {
        private String movieTrailerResponse;
        private int movieId;

        @Override
        protected String doInBackground(Integer... integers) {
            movieId = integers[0];
            String attribute = "/videos";
            URL movieTrailersUrl = NetworkUtils.buildUrlForMovieTrailersAndReviews(movieId, attribute);
            try {
                movieTrailerResponse = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieTrailerResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            String json = result;
            parseJsonTrailerResponse(json);
        }
    }

    private class FetchReviewsTask extends AsyncTask<Integer, Void, String> {
        private String movieReviewResponse;
        private int movieId;

        @Override
        protected String doInBackground(Integer... integers) {
            movieId = integers[0];
            String attribute = "/reviews";
            URL movieTrailersUrl = NetworkUtils.buildUrlForMovieTrailersAndReviews(movieId, attribute);
            try {
                movieReviewResponse = NetworkUtils.getResponseFromHttpUrl(movieTrailersUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieReviewResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            String json = result;
            parseJsonReviewResponse(json);

        }
    }
}


