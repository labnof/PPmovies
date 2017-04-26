package com.example.babatundeanafi.ppmovies.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babatundeanafi.ppmovies.MainActivity;
import com.example.babatundeanafi.ppmovies.model.Movie;
import com.example.babatundeanafi.ppmovies.R;
import com.example.babatundeanafi.ppmovies.control.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {


    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        TextView mTittle = (TextView) findViewById(R.id.textview_movie_tittle);
        TextView mReleaseDate = (TextView) findViewById(R.id.textview_movie_year);
        TextView mVoteAverage = (TextView) findViewById(R.id.textview_ratings);
        TextView mOverview = (TextView) findViewById(R.id.textview_overview);
        ImageView mImageView = (ImageView) findViewById(R.id.imageview_poster);


        //Movie mMovie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
        Movie mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
        context = getApplication();

        mTittle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getRelease_date());
        mVoteAverage.setText(String.format("%s%s", Float.toString(mMovie.getVote_average()), getString(R.string.hAverageVote)));
        mOverview.setText(mMovie.getOverview());
        String mPosterPath = MakePosterPath(mMovie);
        Picasso.with(context).load(mPosterPath).into(mImageView);
    }


    //return Movie's poster path
    private String MakePosterPath(Movie m) {


        if (m != null) {


            URL url = NetworkUtils.buildImageUrl(m.getPoster_path());
            return url.toString();

        }
        return null;

    }
}