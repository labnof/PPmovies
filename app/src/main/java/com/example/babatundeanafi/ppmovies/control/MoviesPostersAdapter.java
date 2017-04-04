package com.example.babatundeanafi.ppmovies.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.babatundeanafi.ppmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by babatundeanafi on 19/03/2017.
 * Reference:https://futurestud.io/tutorials/picasso-adapter-use-for-listview-gridview-etc
 */

public class MoviesPostersAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;

    public  MoviesPostersAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.movie_poster, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.movie_poster, parent, false);
        }

        Picasso
                .with(context)
                .load(imageUrls[position])
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }
}