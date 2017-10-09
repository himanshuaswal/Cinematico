package com.example.hp.mockapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by HP on 27-08-2017.
 */

public class NetworkUtils {

    private static final String MOVIES_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;
    private static String api_param = "api_key";

    public static URL buildUrlForMovieList(String genre) {
        Uri builtUri = Uri.parse(MOVIES_URL + genre).buildUpon()
                .appendQueryParameter(api_param, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForMovieTrailersAndReviews(int movieId, String attribute) {
        Uri builtUri = Uri.parse(MOVIES_URL + movieId + attribute).buildUpon()
                .appendQueryParameter(api_param, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
