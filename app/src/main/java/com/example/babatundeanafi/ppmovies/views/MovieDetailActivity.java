package com.example.babatundeanafi.ppmovies.views;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babatundeanafi.ppmovies.MainActivity;
import com.example.babatundeanafi.ppmovies.R;
import com.example.babatundeanafi.ppmovies.control.JsonToMovieObjs;
import com.example.babatundeanafi.ppmovies.control.NetworkUtils;
import com.example.babatundeanafi.ppmovies.control.VideoRecyclerViewAdapter;
import com.example.babatundeanafi.ppmovies.model.FavouriteMovie;
import com.example.babatundeanafi.ppmovies.model.Movie;
import com.example.babatundeanafi.ppmovies.model.Review;
import com.example.babatundeanafi.ppmovies.model.ReviewRequestResult;
import com.example.babatundeanafi.ppmovies.model.Video;
import com.example.babatundeanafi.ppmovies.model.VideoRequestResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.babatundeanafi.ppmovies.MainActivity.mMovieDBApiKey;
import static com.example.babatundeanafi.ppmovies.model.database.MovieContentProvider.isFavorite;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.COLUMN_MOVIE_VOTE_AVARAGE;
import static com.example.babatundeanafi.ppmovies.model.database.MovieDbContract.MovieEntry.CONTENT_URI;


public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Video[]> {


    Context context;
    Button button;
    private List<Video> videoList = new ArrayList<>();
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;
    private VideoRecyclerViewAdapter mAdapter;
    private static final int VIDEO_MOVIE_LOADER_ID = 20;
    private static final int _MOVIE_LOADER_ID = 21;

    Movie mMovie;
    FavouriteMovie fMovie;

    public static final String VIDEO_LOADER_EXTRA = "com.example.PPmovies.VideoLoader";
    public static final String REVIEW_LOADER_EXTRA = "com.example.PPmovies.ReviewLoader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        TextView mTittle = (TextView) findViewById(R.id.textview_movie_tittle);
        TextView mReleaseDate = (TextView) findViewById(R.id.textview_movie_year);
        TextView mVoteAverage = (TextView) findViewById(R.id.textview_ratings);
        TextView mOverview = (TextView) findViewById(R.id.textview_overview);
        ImageView mImageView = (ImageView) findViewById(R.id.imageview_poster);
        button = (Button) findViewById(R.id.addFavorite_button);
        videoRecyclerView = (RecyclerView) findViewById(R.id.list_video);
        reviewRecyclerView = (RecyclerView) findViewById(R.id.list_reviews);


        if (getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL) != null) {
            mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
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

            getVideos(mMovie.getId());


        } else {

            //Movie mMovie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
            FavouriteMovie fMovie = getIntent().getParcelableExtra(MainActivity.FAVOURITE_MOVIE_DETAIL);
            context = getApplication();

            mTittle.setText(fMovie.getOriginal_title());
            mReleaseDate.setText(fMovie.getRelease_date());
            mVoteAverage.setText(String.format("%s%s", Float.toString(fMovie.getVote_average()), getString(R.string.hAverageVote)));
            mOverview.setText(fMovie.getOverview());
            String mPosterPath = MakePosterPath(fMovie);
            //grab the layout, then set the text of the Button called R.id.Counter:
            button.setText(R.string.delete_favorite);


            //reference: https://futurestud.io/tutorials/picasso-placeholders-errors-and-fading
            Picasso.with(context)
                    .load(mPosterPath)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mImageView);

            getVideos(fMovie.getId());

        }

    }

    //return Movie's poster path
    private String MakePosterPath(Movie m) {


        if (m != null) {


            URL url = NetworkUtils.buildImageUrl(m.getPoster_path());
            return url.toString();

        }
        return null;

    }

    private String MakePosterPath(FavouriteMovie m) {


        if (m != null) {


            URL url = NetworkUtils.buildImageUrl(m.getPoster_path());
            return url.toString();

        }
        return null;

    }


    public void addMovieToFavorite(FavouriteMovie movie) {


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
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }


    }


    public void onClickaddMovieToFavorite(View view) {

        Button b = (Button) view;
        String bText = b.getText().toString();
        String addText = getString(R.string.add_favorite);
        String delText = getString(R.string.delete_favorite);
        //Movie mMovie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);
        Movie mMovie = getIntent().getParcelableExtra(MainActivity.MOVIE_DETAIL);


        if ((bText.equals((addText)) && (!isFavorite(context, mMovie.getId())))) {


            FavouriteMovie fMovie = new FavouriteMovie(mMovie.getPoster_path(), mMovie.getOverview(),
                    mMovie.getRelease_date(), mMovie.getId(), mMovie.getOriginal_title(),
                    mMovie.getVote_average());

            addMovieToFavorite(fMovie);

            b.setText(R.string.delete_favorite);

        }
        // delete from database
        else if (bText.equals((delText))) {

        } else {

        }

    }


    // Trailer  codes Starts
    private String getJsonVideoResult(String url) {
        //methos return Json result from network

        URL sortUrl = NetworkUtils.ValidateUrl(url);

        String themoviedbSortResults = null;
        try {
            themoviedbSortResults = NetworkUtils.getResponseFromHttpUrl(sortUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return themoviedbSortResults;

    }


    private Video[] getVideosLinks(String NetworkResult) {
        //Method returns an the array of movies contained in the network result

        VideoRequestResult videoRequestResult;

        if (NetworkResult != null && !NetworkResult.equals("")) {


            videoRequestResult = JsonToMovieObjs.ConvertVideoResultToObject(NetworkResult);

            return videoRequestResult.getResults();
        }


        return null;
    }


    @Override
    public Loader<Video[]> onCreateLoader(int id, final Bundle args) {
        // LoaderManager.LoaderCallbacks<Movie> Starts

        return new AsyncTaskLoader<Video[]>(this) {
            @Override
            public Video[] loadInBackground() {
                String MoviesURLString = args.getString(VIDEO_LOADER_EXTRA);
                if (MoviesURLString == null || TextUtils.isEmpty(MoviesURLString)) {
                    return null;
                } else {

                    String url = MoviesURLString;
                    return getVideosLinks(getJsonVideoResult(url));

                }


            }

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Video[]> loader, Video[] data) {


        // Video videoRecyclerView

        videoList = Arrays.asList(data);

        mAdapter = new VideoRecyclerViewAdapter(videoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        videoRecyclerView.setLayoutManager(mLayoutManager);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onLoaderReset(Loader<Video[]> loader) {

    }


    // get sort movies from network
    private void getVideos(int videoId) {
        //Loader

        URL url1 = NetworkUtils.buildVideoUrl(videoId, mMovieDBApiKey);
        URL url = NetworkUtils.ValidateUrl(url1.toString());


        Bundle queryBundle = new Bundle();
        queryBundle.putString(VIDEO_LOADER_EXTRA, url.toString());

        LoaderManager mLoaderManager = getSupportLoaderManager();
        Loader<Video[]> mLoader = mLoaderManager.getLoader(VIDEO_MOVIE_LOADER_ID);


        if (mLoader == null) {
            mLoaderManager.initLoader(VIDEO_MOVIE_LOADER_ID, queryBundle, this).forceLoad();

        } else {
            mLoaderManager.restartLoader(VIDEO_MOVIE_LOADER_ID, queryBundle, this).forceLoad();
        }
    }
    // Trailer  codes ends

    // Trailer  codes Starts
    private String getJsonReviewResult(String url) {
        //methos return Json result from network

        URL sortUrl = NetworkUtils.ValidateUrl(url);

        String themoviedbSortResults = null;
        try {
            themoviedbSortResults = NetworkUtils.getResponseFromHttpUrl(sortUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return themoviedbSortResults;

    }

    private LoaderManager.LoaderCallbacks<Review[]> dataResultLoaderListener
            = new LoaderManager.LoaderCallbacks<Review[]>() {
        @Override
        public Loader<Review[]> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Review[]> loader, Review[] data) {

        }

        @Override
        public void onLoaderReset(Loader<Review[]> loader) {

        }
    };
































    private Review[] getReviewLinks(String NetworkResult) {
        //Method returns an the array of movies contained in the network result

        Review[] review;
        ReviewRequestResult reviewRequestResult;

        if (NetworkResult != null && !NetworkResult.equals("")) {


            reviewRequestResult = JsonToMovieObjs.ConvertReviewResultToObject(NetworkResult);

            return reviewRequestResult.getResults();
        }


        return null;
    }



}
