package com.example.hp.mockapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HP on 26-08-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    static String BASE_URL = "http://image.tmdb.org/t/p/w500/";
    private Context context;
    private ArrayList<MovieAttributes> mMovieAttributes = new ArrayList<>();
    private MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler=clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, int position) {
        Glide.with(context).load(BASE_URL + mMovieAttributes.get(position).get_poster_path()).fitCenter().centerCrop().placeholder(R.color.colorAccent).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.androidImage);
    }

    @Override
    public int getItemCount() {
        return mMovieAttributes.size();
    }

    public void setMovieData(ArrayList<MovieAttributes> movieAttributes) {
        mMovieAttributes = movieAttributes;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void handleClicks(int position);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView androidImage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            androidImage = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickHandler.handleClicks(getAdapterPosition());
                }
            });
        }

    }

}
