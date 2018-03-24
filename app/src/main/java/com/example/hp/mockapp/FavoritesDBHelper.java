package com.example.hp.mockapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 18-03-2018.
 */

public class FavoritesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 16;

    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesContract.FavoritesSpec.TABLE_NAME + " (" +
                FavoritesContract.FavoritesSpec._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID + " STRING NOT NULL," +
                FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH + " STRING NOT NULL" + "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesSpec.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
