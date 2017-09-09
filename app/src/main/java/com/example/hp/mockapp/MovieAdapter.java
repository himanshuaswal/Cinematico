package com.example.hp.mockapp;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public interface MovieAdapterOnClickHandler {
        void handleClicks(int position);
    }

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
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(context).load(BASE_URL + mMovieAttributes.get(position).get_poster_path()).fit().centerCrop().placeholder(R.color.colorAccent).into(holder.androidImage);

    }

    @Override
    public int getItemCount() {
        return mMovieAttributes.size();
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

    public void setMovieData(ArrayList<MovieAttributes> movieAttributes) {
        mMovieAttributes = movieAttributes;
        notifyDataSetChanged();
    }

}
