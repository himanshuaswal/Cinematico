package com.example.hp.mockapp;

import android.provider.BaseColumns;

/**
 * Created by HP on 18-03-2018.
 */

public class FavoritesContract {
    public static final class FavoritesSpec implements BaseColumns {
        public static final String TABLE_NAME = "fav";
        public static final String COLUMN_ORIGINAL_TITLE = "Original_Title";
        public static final String COLUMN_POSTER_PATH = "Poster_Path";
        public static final String COLUMN_OVERVIEW = "Overview";
        public static final String COLUMN_VOTE_AVERAGE = "Vote_Average";
        public static final String COLUMN_RELEASE_DATE = "Release_Date";
        public static final String COLUMN_VOTE_COUNT = "Vote_Count";
        public static final String COLUMN_MOVIE_ID = "Movie_Id";
        public static final String COLUMN_BACKDROP_PATH = "Backdrop_Path";

    }
}
