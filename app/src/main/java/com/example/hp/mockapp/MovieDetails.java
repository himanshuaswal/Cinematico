package com.example.hp.mockapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetails extends AppCompatActivity {
    static String id;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private ImageView mPosterPath;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mOriginalTitle = (TextView) findViewById(R.id.original_title);
        mOverview = (TextView) findViewById(R.id.overview);
        mVoteAverage = (TextView) findViewById(R.id.vote_average);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mPosterPath=(ImageView)findViewById(R.id.poster_image);
        mRatingTextView = (TextView) findViewById(R.id.rating_text_view);
        mReleaseDateTextView = (TextView) findViewById(R.id.release_date_text_view);
        Intent intentThatStartedThisActivity = getIntent();
        id = intentThatStartedThisActivity.getStringExtra("id");
        new FetchMovieDetails().execute();
    }

    public class FetchMovieDetails extends AsyncTask<Void, Void, String> {
        private String movieDetailsResponse;

        @Override
        protected String doInBackground(Void... voids) {
           URL movieDetailsUrl = NetworkUtils.buildUrlForMovieDetails();
            try {
                movieDetailsResponse = NetworkUtils.getResponseFromHttpUrl(movieDetailsUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieDetailsResponse;

        }

        @Override
        protected void onPostExecute(String movieDetailsResponse) {
            try {
                JSONObject jsonObject = new JSONObject(movieDetailsResponse);
                mOriginalTitle.setText(jsonObject.getString("original_title"));
                mOverview.setText(jsonObject.getString("overview"));
                mVoteAverage.setText(String.valueOf(jsonObject.getDouble("vote_average")));
                mReleaseDateTextView.setText("Release Date : ");
                mRatingTextView.setText("Rating : ");
                mReleaseDate.setText(jsonObject.getString("release_date"));
                Picasso.with(getApplicationContext()).load(MovieAdapter.BASE_URL+jsonObject.getString("poster_path")).resize(200,300).placeholder(R.color.colorAccent).into(mPosterPath);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
