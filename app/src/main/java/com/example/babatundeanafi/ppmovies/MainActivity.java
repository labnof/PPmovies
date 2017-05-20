package com.example.babatundeanafi.ppmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babatundeanafi.ppmovies.control.JsonToMovieObjs;
import com.example.babatundeanafi.ppmovies.control.MoviesPostersAdapter;
import com.example.babatundeanafi.ppmovies.control.NetworkUtils;
import com.example.babatundeanafi.ppmovies.model.FavouriteMovie;
import com.example.babatundeanafi.ppmovies.model.Movie;
import com.example.babatundeanafi.ppmovies.model.MovieRequestResult;
import com.example.babatundeanafi.ppmovies.model.database.MovieDbContract;
import com.example.babatundeanafi.ppmovies.model.database.MovieDbHelper;
import com.example.babatundeanafi.ppmovies.views.MovieDetailActivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.babatundeanafi.ppmovies.control.NetworkUtils.buildSortUrl;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {


    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVOURITE_MOVIE_LOADER_ID = 2;


    private String[] posterArray;// Array to save the list of poster URl
    private Movie[] mMovies;
    private FavouriteMovie[] mFavouriteMovie;


    public static final String MOVIE_DETAIL = "com.example.PPmovies.Detail";
    public static final String FAVOURITE_MOVIE_DETAIL = "com.example.PPmovies.Favourite.Detail";
    public static final String MOVIES_LOADER_EXTRA = "com.example.PPmovies.MoviesLoader";
    private static final int MOVIES_DB_LOADER = 29;//Loader indentify
    private static final String SORT_BY_PP_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String SORT_BY_VOTE_AVE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
    private static final String GRID_VIEW_STATE_KEY = "movies";
    private static final String POP_MOVIE_STATE_KEY = "popmovies";

    private static final String MOVIE_STATE_KEY = "popmovies";
    private static final String FAVOURITE_MOVIE_STATE_KEY = "favorite_movies";




    boolean mNetworkAvailable = FALSE;

    public static String mMovieDBApiKey;
    private TextView mErrorMessageDisplay; //TextView variable for the error message display
    private ProgressBar mLoadingIndicator; //ProgressBar variable to show and hide the progress bar
    private GridView mGridView;
    private Context mContext;

    private String getJsonSortResult(String url) {
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

    private Movie[] getMoviesLinks(String NetworkResult) {
        //Method returns an the array of movies contained in the network result

        Movie[] movies;
        MovieRequestResult movieRequestResult;

        if (NetworkResult != null && !NetworkResult.equals("")) {


            movieRequestResult = JsonToMovieObjs.ConvertMovieResultToObject(NetworkResult);
            movies = movieRequestResult.getResults();
            return movies;
        }


        return null;
    }

    private ArrayList<String> getPosterPaths(Movie[] m) {

        ArrayList<String> PosterPaths = new ArrayList<>();


        if (m.length != 0) {


            for (Movie movies : m) {

                URL url = NetworkUtils.buildImageUrl(movies.getPoster_path());
                String sd = url.toString();
                PosterPaths.add(sd);
            }

            return PosterPaths;
        }
        return null;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);



        outState.putParcelable(POP_MOVIE_STATE_KEY, mGridView.onSaveInstanceState());
        outState.putStringArray(GRID_VIEW_STATE_KEY, posterArray);
        outState.putParcelableArray(MOVIE_STATE_KEY , mMovies);
        outState.putParcelableArray(FAVOURITE_MOVIE_STATE_KEY, mFavouriteMovie);


           }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(MainActivity.this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(MainActivity.this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    Parcelable state;//save insatance state
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGridView = (GridView) findViewById(R.id.usage_example_gridview);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        Resources res = getResources();//get ApiKey from resource
        mMovieDBApiKey = res.getString(R.string.themoviedbApi_key);
        mContext = this.getApplication();


        // checks if internet is available and deplays posters and shows error if no internet
        mNetworkAvailable = isNetworkAvailable();
        if (savedInstanceState != null) {

            mMovies = (Movie[]) savedInstanceState.getParcelableArray(MOVIE_STATE_KEY);
            mFavouriteMovie = (FavouriteMovie[]) savedInstanceState.getParcelableArray(FAVOURITE_MOVIE_STATE_KEY);
            state = savedInstanceState.getParcelable(POP_MOVIE_STATE_KEY);// state of list scroll

            if ((mMovies != null) && (mNetworkAvailable == TRUE)) {

                posterArray = savedInstanceState.getStringArray(GRID_VIEW_STATE_KEY); // state of list view
                mGridView.setAdapter(new MoviesPostersAdapter(mContext, posterArray));

                if (state != null) {
                    mGridView.onRestoreInstanceState(state);
                }


                GridViewOnclick(mMovies);

                mLoadingIndicator.setVisibility(View.INVISIBLE);


            } else if ((mFavouriteMovie != null) && (mNetworkAvailable == TRUE)) {

                state = savedInstanceState.getParcelable(POP_MOVIE_STATE_KEY);// state of list scroll
                posterArray = savedInstanceState.getStringArray(GRID_VIEW_STATE_KEY); // state of list view
                mGridView.setAdapter(new MoviesPostersAdapter(mContext, posterArray));
                if (state != null) {
                    mGridView.onRestoreInstanceState(state);
                }
                GridViewOnclick(mFavouriteMovie);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }

        else if (mNetworkAvailable == TRUE) {

            getSortMovies(SORT_BY_PP_URL);//gets the most popular movies
            mLoadingIndicator.setVisibility(View.INVISIBLE);
      }

        else {
            showErrorMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first

        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first

        // mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first


        // mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    /*
    The getActiveNetworkInfo() method of ConnectivityManager returns a NetworkInfo instance
    representing the first connected network interface it can find or null if none of the interfaces
    are connected. Checking if this method returns null should be enough to tell if an internet
    connection is available or not.

    Reference:http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // get sort movies from network
    private void getSortMovies(String sortBy) {
        //Loader

        URL url1 = buildSortUrl(sortBy, mMovieDBApiKey);
        URL url = NetworkUtils.ValidateUrl(url1.toString());
        mGridView = (GridView) findViewById(R.id.usage_example_gridview);


        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIES_LOADER_EXTRA, url.toString());

        LoaderManager mLoaderManager = getSupportLoaderManager();
        Loader<Movie[]> mLoader = mLoaderManager.getLoader(MOVIES_DB_LOADER);


        if (mLoader == null) {
            mLoaderManager.initLoader(MOVIES_DB_LOADER, queryBundle, this).forceLoad();

        } else {
            mLoaderManager.restartLoader(MOVIES_DB_LOADER, queryBundle, this).forceLoad();
        }


    }

    // get sort movies from DB
    private void getSortMovies() {
        //Loader

        mGridView = (GridView) findViewById(R.id.usage_example_gridview);


        LoaderManager mLoaderManager = getSupportLoaderManager();
        Loader<Cursor> mLoader = mLoaderManager.getLoader(FAVOURITE_MOVIE_LOADER_ID);


        if (mLoader == null) {
            mLoaderManager.initLoader(FAVOURITE_MOVIE_LOADER_ID, null, dataResultLoaderListener)
                    .forceLoad();

        } else {
            mLoaderManager.restartLoader(FAVOURITE_MOVIE_LOADER_ID, null, dataResultLoaderListener)
                    .forceLoad();
        }

    }

    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle args) {
        // LoaderManager.LoaderCallbacks<Movie> Starts

        return new AsyncTaskLoader<Movie[]>(this) {
            @Override
            public Movie[] loadInBackground() {
                String MoviesURLString = args.getString(MOVIES_LOADER_EXTRA);
                if (MoviesURLString == null || TextUtils.isEmpty(MoviesURLString)) {
                    return null;
                } else {


                    String JsonSortResult = getJsonSortResult(MoviesURLString);
                    return getMoviesLinks(JsonSortResult);

                }


            }

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, final Movie[] movies) {

        ArrayList<String> results;
        mMovies = movies;


        if (mMovies != null && mMovies.length != 0) {

            results = getPosterPaths(mMovies);


            posterArray = new String[results != null ? results.size() : 0];// initialize sting array to size of result
            posterArray = results != null ? results.toArray(posterArray) : new String[0];

            mGridView.setAdapter(new MoviesPostersAdapter(mContext, posterArray));
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            GridViewOnclick( mMovies);


        } else {
            //If the array list_video of movies poster data is null, show the error message
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {

    }


    //favourie movie loader
    public LoaderManager.LoaderCallbacks<Cursor> dataResultLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(mContext) {


                // Initialize a Cursor, this will hold all the task data
                Cursor mMovieData = null;

                // onStartLoading() is called when a loader first starts loading data
                @Override
                protected void onStartLoading() {
                    if (mMovieData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mMovieData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {
                    // Will implement to load data

                    // COMPLETED (5) Query and load all task data in the background; sort by priority
                    // [Hint] use a try/catch block to catch any errors in loading data

                    try {
                        return getContentResolver().query(MovieDbContract.MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                MovieDbContract.MovieEntry.COLUMN_MOVIE_ID);

                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }

                // deliverResult sends the result of the load, a Cursor, to the registered listener
                public void deliverResult(Cursor data) {
                    mMovieData = data;
                    super.deliverResult(data);
                }

            };
        }


        private ArrayList<String> getPosterPaths(ArrayList<FavouriteMovie> m) {

            ArrayList<String> PosterPaths = new ArrayList<>();


            if (m.size() != 0) {


                for (FavouriteMovie movies : m) {

                    URL url = NetworkUtils.buildImageUrl(movies.getPoster_path());
                    String sd = url.toString();
                    PosterPaths.add(sd);
                }

                return PosterPaths;
            }
            return null;

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

       if (data != null && data.getCount() != 0) {


           ArrayList<FavouriteMovie> mFavouriteM = MovieDbHelper.getListOfFavouriteMovies1(data);
                ArrayList<String> poster_paths = getPosterPaths(mFavouriteM);


                posterArray = new String[mFavouriteM != null ? mFavouriteM.size() : 0];// initialize string array to size of result
                posterArray = poster_paths != null ? poster_paths.toArray(posterArray) : new String[0];

                mGridView.setAdapter(new MoviesPostersAdapter(mContext, posterArray));
                mLoadingIndicator.setVisibility(View.INVISIBLE);

           mFavouriteMovie = new FavouriteMovie[mFavouriteM != null ? mFavouriteM.size() : 0];// initialize string array to size of result
           mFavouriteMovie = mFavouriteM != null ? mFavouriteM.toArray(mFavouriteMovie) : new FavouriteMovie[0];
           GridViewOnclick(mFavouriteMovie);


            } else {
                //If the array list_video of movies poster data is null, show the error message
                showErrorMessage();
            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        ///mGridView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.MP_option:
                getSortMovies(SORT_BY_PP_URL);//gets the most popular movies
                return true;
            case R.id.TR_option:
                getSortMovies(SORT_BY_VOTE_AVE_URL);//gets the top rated  movies
                return true;
            case R.id.favorite_option:
                getSortMovies();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void PacelableMethod(Movie[] movies, int position) {


        Movie mMovie = movies[position];
        Intent mIntent = new Intent(this, MovieDetailActivity.class);

        Bundle mBundle = new Bundle();

        mBundle.putParcelable(MOVIE_DETAIL, mMovie);
        mIntent.putExtras(mBundle);

        startActivity(mIntent);
    }


    public void PacelableMethod(FavouriteMovie[] movies, int position) {


        FavouriteMovie mMovie = movies[position];
        Intent mIntent = new Intent(this, MovieDetailActivity.class);

        Bundle mBundle = new Bundle();

        mBundle.putParcelable(FAVOURITE_MOVIE_DETAIL, mMovie);
        mIntent.putExtras(mBundle);

        startActivity(mIntent);
    }



    public void GridViewOnclick(final Movie[] m){

        //Grid View setOnItemClickListener that leads to detail page
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                PacelableMethod(m, position);


                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void GridViewOnclick( final FavouriteMovie[] fm){

        //Grid View setOnItemClickListener that leads to detail page
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                PacelableMethod(fm, position);


                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }





}