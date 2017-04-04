package com.example.babatundeanafi.ppmovies.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by babatundeanafi on 28/03/2017.
 */

public class MovieDB {

    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_ADULT = "adult";
    public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    public static final String COLUMN_MOVIE_GENRE_IDS = "genre_ids";
    public static final String COLUMN_MOVIE_ID = "id";
    public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_MOVIE_ORIGINAL_LANGUAGE = "original_language";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_MOVIE_POPULARITY = "popularity";
    public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";
    public static final String COLUMN_MOVIE_VIDEO = "video";
    public static final String COLUMN_MOVIE_VOTE_AVARAGE = "vote_average";


    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;


    private MovieSQLiteHelper helper;
    private static MovieDB database;


    public static MovieDB getInstance(Context c) {
        if (database == null) {
            database = new MovieDB(c);
        }
        return database;
    }

    private MovieDB(Context c) {
        helper = new MovieSQLiteHelper(c);
    }



    public class MovieSQLiteHelper  extends SQLiteOpenHelper {


        // Database creation sql statement
        private static final String DATABASE_CREATE = "create table "
                + TABLE_MOVIES + "( " + COLUMN_ID
                + " integer primary key autoincrement, " +
                COLUMN_MOVIE_ADULT + " text not null,  " +
                COLUMN_MOVIE_OVERVIEW + " text not null," +
                COLUMN_MOVIE_RELEASE_DATE + " text not null," +
                COLUMN_MOVIE_GENRE_IDS + " text not null," +
                COLUMN_MOVIE_ID + " text not null,  " +
                COLUMN_MOVIE_ORIGINAL_TITLE + " text not null," +
                COLUMN_MOVIE_ORIGINAL_LANGUAGE + " text not null," +
                COLUMN_MOVIE_TITLE + " text not null," +
                COLUMN_MOVIE_BACKDROP_PATH + " text not null,  " +
                COLUMN_MOVIE_POPULARITY + " text not null," +
                COLUMN_MOVIE_VOTE_COUNT + " text not null," +
                COLUMN_MOVIE_VIDEO + " text not null," +
                COLUMN_MOVIE_VOTE_AVARAGE + " text not null);";


        private static final String DATABASE_INSERT = "insert into"
                + TABLE_MOVIES + "( " + COLUMN_ID
                + " , " +
                COLUMN_MOVIE_ADULT + " ,  " +
                COLUMN_MOVIE_OVERVIEW + " ,  " +
                COLUMN_MOVIE_RELEASE_DATE + " ,  " +
                COLUMN_MOVIE_GENRE_IDS + " ,  " +
                COLUMN_MOVIE_ID + " ,  " +
                COLUMN_MOVIE_ORIGINAL_TITLE + " ,  " +
                COLUMN_MOVIE_ORIGINAL_LANGUAGE + " ,  " +
                COLUMN_MOVIE_TITLE + " ,  " +
                COLUMN_MOVIE_BACKDROP_PATH + " ,  " +
                COLUMN_MOVIE_POPULARITY + " ,  " +
                COLUMN_MOVIE_VOTE_COUNT + " ,  " +
                COLUMN_MOVIE_VIDEO + " ,  " +
                COLUMN_MOVIE_VOTE_AVARAGE
                + ");";

        public MovieSQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);


        }


        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MovieSQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
            onCreate(db);
        }


        /**
         * All CRUD(Create, Read, Update, Delete) Operations
         */


    }}