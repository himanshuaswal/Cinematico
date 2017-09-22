package com.example.hp.mockapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 26-08-2017.
 */

public class MovieAttributes implements Parcelable {
    public static final Parcelable.Creator<MovieAttributes> CREATOR = new Parcelable.Creator<MovieAttributes>() {
        @Override
        public MovieAttributes createFromParcel(Parcel in) {
            return new MovieAttributes(in);
        }

        @Override
        public MovieAttributes[] newArray(int size) {
            return new MovieAttributes[size];
        }
    };
    private String poster_path;
    private String original_title;
    private String overview;
    private String release_date;
    private double vote_average;

    public MovieAttributes() {
    }

    protected MovieAttributes(Parcel in) {
        this.poster_path = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    public String get_poster_path() {
        return poster_path;
    }

    public void set_poster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String get_original_title() {
        return original_title;
    }

    public void set_original_title(String original_title) {
        this.original_title = original_title;
    }

    public String get_overview() {
        return overview;
    }

    public void set_overview(String overview) {
        this.overview = overview;
    }

    public String get_release_date() {
        return release_date;
    }

    public void set_release_date(String release_date) {
        this.release_date = release_date;
    }

    public double get_vote_average() {
        return vote_average;
    }

    public void set_vote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.release_date);
    }
}
