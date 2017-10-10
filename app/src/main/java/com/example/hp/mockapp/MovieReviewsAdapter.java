package com.example.hp.mockapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by HP on 09-10-2017.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {
    Context mContext;
    private ArrayList<String> movieReviewAuthorArrayList = new ArrayList<>();
    private ArrayList<String> movieReviewArrayList = new ArrayList<>();

    public MovieReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_review_item, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        holder.reviewAuthor.setText(movieReviewAuthorArrayList.get(position));
        holder.review.setText(movieReviewArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return movieReviewArrayList.size();
    }

    public void setMovieReviewsData(ArrayList<String> movieReviewAuthor, ArrayList<String> movieReviews) {
        movieReviewAuthorArrayList = movieReviewAuthor;
        movieReviewArrayList = movieReviews;
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView review;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.review_author);
            review = itemView.findViewById(R.id.reviews);
        }
    }
}
