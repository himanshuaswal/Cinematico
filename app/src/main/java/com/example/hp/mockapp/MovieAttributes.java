package com.example.hp.mockapp;

/**
 * Created by HP on 26-08-2017.
 */

public class MovieAttributes {
    private String poster_path;
    private int id;

    public void set_poster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String get_poster_path() {
        return poster_path;
    }

    public void set_movie_id(int id) {
        this.id = id;
    }

    public int get_id() {
        return id;
    }

}
