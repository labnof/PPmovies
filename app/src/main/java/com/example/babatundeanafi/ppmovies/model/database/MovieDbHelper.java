package com.example.babatundeanafi.ppmovies.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.babatundeanafi.ppmovies.model.FavouriteMovie;
import com.example.babatundeanafi.ppmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_ID;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVARAGE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.TABLE_MOVIES;

/**
 * Created by babatundeanafi on 03/05/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;



    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIES + "( " + COLUMN_ID
            + " integer primary key autoincrement, " +
            COLUMN_MOVIE_OVERVIEW + " text not null," +
            COLUMN_MOVIE_RELEASE_DATE + " text not null," +
            COLUMN_MOVIE_ID + " text not null,  " +
            COLUMN_MOVIE_ORIGINAL_TITLE + " text not null," +
            COLUMN_MOVIE_VOTE_AVARAGE + " text not null);";


    private static final String DATABASE_INSERT = "insert into"
            + TABLE_MOVIES + "( " + COLUMN_ID
            + " , " +
            COLUMN_MOVIE_OVERVIEW + " ,  " +
            COLUMN_MOVIE_RELEASE_DATE + " ,  " +
            COLUMN_MOVIE_ID + " ,  " +
            COLUMN_MOVIE_ORIGINAL_TITLE + " ,  " +
            COLUMN_MOVIE_VOTE_AVARAGE
            + ");";



    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MovieDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }



    /*
     * DataBase Manupulation
     */


// get all Favorite Movies
    public Cursor getallFavouriteMovies(){

        SQLiteDatabase mDb = this.getReadableDatabase();

        Cursor c = mDb.query(TABLE_MOVIES,null,null, null, null, null,
                COLUMN_MOVIE_ORIGINAL_TITLE + " asc ");

        if (c != null){
            c.moveToFirst();
        }

        return c;

    }



    // Getting All Movies
    public List<FavouriteMovie> getListOfFavouriteMovies() {
        List<FavouriteMovie> mMovietList = new ArrayList<FavouriteMovie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavouriteMovie mMovie = new FavouriteMovie();

                mMovie.setPoster_path(cursor.getString(0));
                mMovie.setOverview(cursor.getString(1));
                mMovie.setRelease_date(cursor.getString(2));
                mMovie.setId(Integer.parseInt(cursor.getString(3)));
                mMovie.setOriginal_title(cursor.getString(4));
                mMovie.setVote_average(Float.parseFloat(cursor.getString(5)));

                // Adding contact to list
                mMovietList.add(mMovie);
            } while (cursor.moveToNext());}

        // return contact list
        return mMovietList;
    }

    // Adding new Movie
    public void addMovie(Movie movie) {
        SQLiteDatabase mDb = this.getWritableDatabase();



        ContentValues mValues = new ContentValues();
        mValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        mValues.put(COLUMN_MOVIE_ID, movie.getId());
        mValues.put(COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginal_title());
        mValues.put(COLUMN_MOVIE_VOTE_AVARAGE, movie.getVote_average());
        mValues.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        mValues.put(COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());

        // Inserting Row
        mDb.insert(TABLE_MOVIES, null, mValues);
        mDb.close(); // Closing database connection

    }


    // Getting single
     FavouriteMovie getMovie(int id) {
         SQLiteDatabase db = this.getReadableDatabase();


         Cursor cursor = db.query(TABLE_MOVIES, new String[]{COLUMN_MOVIE_POSTER_PATH,
                         COLUMN_MOVIE_OVERVIEW, COLUMN_MOVIE_RELEASE_DATE, COLUMN_MOVIE_ID,
                         COLUMN_MOVIE_ORIGINAL_TITLE, COLUMN_MOVIE_VOTE_AVARAGE}, COLUMN_MOVIE_ID + "=?",
                 new String[]{String.valueOf(id)}, null, null, null, null);
         if (cursor != null)
             cursor.moveToFirst();

         FavouriteMovie mMovie = new FavouriteMovie(cursor.getString(0),
                 cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                 Float.parseFloat(cursor.getString(5)));
         // return mosque
         return mMovie;

     }

         // Getting single Method 2
         FavouriteMovie getAmovieFromList(int id) {
             SQLiteDatabase db = this.getReadableDatabase();




             Cursor cursor = db.query(TABLE_MOVIES, new String[]{COLUMN_MOVIE_POSTER_PATH,
                             COLUMN_MOVIE_OVERVIEW, COLUMN_MOVIE_RELEASE_DATE, COLUMN_MOVIE_ID,
                             COLUMN_MOVIE_ORIGINAL_TITLE, COLUMN_MOVIE_VOTE_AVARAGE},  COLUMN_ID  + "=?",
                     new String[]{String.valueOf(id)}, null, null, null, null);
             if (cursor != null)
                 cursor.moveToFirst();

             FavouriteMovie mMovie = new FavouriteMovie(cursor.getString(0),
                     cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                     Float.parseFloat(cursor.getString(5)));
             // return mosque
             return mMovie;

}

// Deleting single contact
    public void deleteMosque(FavouriteMovie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, COLUMN_MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
        db.close();
    }

    private boolean removeMovieFromList(long id) {

        SQLiteDatabase mDb = this.getWritableDatabase();
        return mDb.delete(TABLE_MOVIES, COLUMN_ID + "=" + id, null) > 0;
    }





    // Updating single contact
    public int updateMovie(FavouriteMovie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues  mValues = new ContentValues();
        mValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        mValues.put(COLUMN_MOVIE_ID, movie.getId());
        mValues.put(COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginal_title());
        mValues.put(COLUMN_MOVIE_VOTE_AVARAGE, movie.getVote_average());
        mValues.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        mValues.put(COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());

        // updating row
        return db.update(TABLE_MOVIES,  mValues, COLUMN_MOVIE_ID+ " = ?",
                new String[] { String.valueOf(movie.getId()) });
    }




    // Getting contacts Count
    public int getMosquesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOVIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }



}