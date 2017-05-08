package com.example.babatundeanafi.ppmovies.control;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.babatundeanafi.ppmovies.R;

/**
 * Created by null on 7.5.2017.
 */

public class FavoriteMovieAdapter extends CursorAdapter {

    public FavoriteMovieAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_poster, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        ImageView imageView = (ImageView) view. findViewById(R.id.movie_item_poster);

    }
}
