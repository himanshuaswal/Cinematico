package com.example.hp.mockapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hp.mockapp.FavoritesContract;

import java.util.ArrayList;
import java.util.List;

import static com.example.hp.mockapp.FavoritesContract.FavoritesSpec.TABLE_NAME;

/**
 * Created by HP on 21-03-2018.
 */

public class TestUtils {
    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE, "Dilwale Dulhania Le Jayenge");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW, "Raj is a rich, carefree, happy-go-lucky second generation NRI. Simran is the daughter of Chaudhary Baldev Singh, who in spite of being an NRI is very strict about adherence to Indian values. Simran has left for India to be married to her childhood fiancé. Raj leaves for India with a mission at his hands, to claim his lady love under the noses of her whole family. Thus begins a saga.");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH, "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE, "1995-10-20");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE, "9.2");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT, "1234");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID, "19404");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH, "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE, "The Shawshank Redemption");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW, "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH, "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE, "1994-09-23");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE, "8.5");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT, "9745");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID, "278");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH, "/xBKGJQsAIeweesB79KC89FpBrVr.jpg");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_ORIGINAL_TITLE, "The Godfather");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_OVERVIEW, "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_POSTER_PATH, "/rPdtLWNsZmAtoZl9PK7S2wE3qiS.jpg");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_RELEASE_DATE, "1972-03-14");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_AVERAGE, "8.5");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_VOTE_COUNT, "7331");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_MOVIE_ID, "238");
        cv.put(FavoritesContract.FavoritesSpec.COLUMN_BACKDROP_PATH, "/6xKCYgH16UuwEGAyroLU6p8HLIn.jpg");
        list.add(cv);


        try {
            db.beginTransaction();
            //clear the table first
            db.delete(TABLE_NAME, null, null);
            //go through the list and add one by one
            for (ContentValues c : list)
                db.insert(TABLE_NAME, null, c);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {

        //db.delete(FavoritesContract.FavoritesSpec.TABLE_NAME,null,null);
        db.execSQL("delete from " + FavoritesContract.FavoritesSpec.TABLE_NAME);
        //db.execSQL("TRUNCATE table" + TABLE_NAME);
        // db.close();
    }
}


