package com.example.babatundeanafi.ppmovies.views;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babatundeanafi.ppmovies.MainActivity;
import com.example.babatundeanafi.ppmovies.R;
import com.example.babatundeanafi.ppmovies.control.FavoriteMovieAdapter;
import com.example.babatundeanafi.ppmovies.control.NetworkUtils;
import com.example.babatundeanafi.ppmovies.model.FavouriteMovie;
import com.example.babatundeanafi.ppmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.*;


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


        //reference: https://futurestud.io/tutorials/picasso-placeholders-errors-and-fading
        Picasso.with(context)
                .load(mPosterPath)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mImageView);
    }


    //return Movie's poster path
    private String MakePosterPath(Movie m) {


        if (m != null) {


            URL url = NetworkUtils.buildImageUrl(m.getPoster_path());
            return url.toString();

        }
        return null;

    }


    private void addMovieToFavorite(FavouriteMovie movie){


        ContentValues mValues = new ContentValues();

        mValues.put(COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        mValues.put(COLUMN_MOVIE_ID, movie.getId());
        mValues.put(COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginal_title());
        mValues.put(COLUMN_MOVIE_VOTE_AVARAGE, movie.getVote_average());
        mValues.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        mValues.put(COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());



        // Insert the content values via a ContentResolver

        Uri uri = getContentResolver().insert(CONTENT_URI, mValues);

        // COMPLETED (8) Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }


    }

    public void onClickaddMovieToFavorite(View view) {

        //Movie mMovie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
        Movie mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);


        FavouriteMovie fMovie = new FavouriteMovie(mMovie.getPoster_path(),mMovie.getOverview(),
                mMovie.getRelease_date(), mMovie.getId(), mMovie.getOriginal_title(),
                mMovie.getVote_average());

        addMovieToFavorite(fMovie);
    }
}