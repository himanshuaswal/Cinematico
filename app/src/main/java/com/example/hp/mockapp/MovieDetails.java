package com.example.hp.mockapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        mPosterPath = (ImageView) findViewById(R.id.poster_image);
        mRatingTextView = (TextView) findViewById(R.id.rating_text_view);
        mReleaseDateTextView = (TextView) findViewById(R.id.release_date_text_view);
        Intent intentThatStartedThisActivity = getIntent();
        MovieAttributes object = intentThatStartedThisActivity.getParcelableExtra("myDataKey");
        showDetails(object);
    }

    private void showDetails(MovieAttributes object) {
        mOriginalTitle.setText(object.get_original_title());
        mReleaseDateTextView.setText("Release Date : ");
        mReleaseDate.setText(object.get_release_date());
        mRatingTextView.setText("Rating : ");
        mVoteAverage.setText(String.valueOf(object.get_vote_average()));
        mOverview.setText(object.get_overview());
        Picasso.with(getApplicationContext()).load(MovieAdapter.BASE_URL + object.get_poster_path()).resize(200, 300).placeholder(R.color.colorAccent).into(mPosterPath);


    }

}
