package com.example.babatundeanafi.ppmovies.model.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by babatundeanafi on 03/05/2017.
 */

public class MovieDbContract {


    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.babatundeanafi.ppmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "movies";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MovieDbContract() {
    }

    public static class MovieEntry implements BaseColumns {
        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        //Table Name
        public static final String TABLE_MOVIES = "movies";

        // COLUMNS
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_MOVIE_VOTE_AVARAGE = "vote_average";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
    }

}
