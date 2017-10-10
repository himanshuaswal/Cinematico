package com.example.hp.mockapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by HP on 03-10-2017.
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {
    static String BASE_URL = "http://image.tmdb.org/t/p/w500/";
    static String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";
    Context context;
    private ArrayList<String> videoNames = new ArrayList<>();
    private ArrayList<String> mMovieTrailerKeys = new ArrayList<>();
    //private Bitmap mPosterBitmap;
    private String mPosterPath;

    public MovieTrailersAdapter(Context context, String posterPath) {
        this.context = context;
        mPosterPath = posterPath;
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_item, parent, false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        //holder.posterBackdrop.setImageBitmap(mPosterBitmap);
        Glide.with(context).load(MovieAdapter.BASE_URL + mPosterPath).fitCenter().placeholder(R.color.colorAccent).into(holder.posterBackdrop);

        holder.mVideoHeading.setText(videoNames.get(position));
        holder.mPlayTrailerButton.setBackgroundResource(R.drawable.ic_play_circle_outline);

    }

    @Override
    public int getItemCount() {
        return videoNames.size();
    }

    public void setMovieTrailersData(ArrayList<String> movieVideoNames, ArrayList<String> movieTrailerKeys) {
        videoNames = movieVideoNames;
        mMovieTrailerKeys = movieTrailerKeys;
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView posterBackdrop;
        TextView mVideoHeading;
        Button mPlayTrailerButton;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            posterBackdrop = itemView.findViewById(R.id.movie_backdrop_path);
            mVideoHeading = itemView.findViewById(R.id.video_heading);
            mPlayTrailerButton = itemView.findViewById(R.id.play_button);
            mPlayTrailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String trailerKey = mMovieTrailerKeys.get(getAdapterPosition());
                    Uri youtubeUri = Uri.parse(TRAILER_BASE_URL + trailerKey);
                    Intent openYouTube = new Intent(Intent.ACTION_VIEW, youtubeUri);
                    context.startActivity(openYouTube);
                }
            });
        }
    }
}
