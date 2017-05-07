package com.example.babatundeanafi.ppmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.example.babatundeanafi.ppmovies.model.Movie;
import com.example.babatundeanafi.ppmovies.model.RequestResult;
import com.example.babatundeanafi.ppmovies.views.MovieDetailActivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.babatundeanafi.ppmovies.control.NetworkUtils.buildSortUrl;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie[]> {


    //Loaders
    private static final int NETWORK_MOVIE_LOADER_ID = 1;
    private static final int DB_MOVIE_LOADER_ID = 2;





    public static final String MOVIE_DETAIL = "com.example.PPmovies.Detail";
    public static final String MOVIES_LOADER_EXTRA = "com.example.PPmovies.MoviesLoader";
    private static final int MOVIES_DB_LOADER = 29;//Loader indentify
    private static final String SORT_BY_PP_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String SORT_BY_VOTE_AVE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
    boolean mNetworkAvailable = FALSE;

    private String mMovieDBApiKey;
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
        RequestResult requestResult;

        if (NetworkResult != null && !NetworkResult.equals("")) {


            requestResult = JsonToMovieObjs.ConvertToResultObject(NetworkResult);
            movies = requestResult.getResults();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        if (mNetworkAvailable == TRUE) {
            getSortMovies(SORT_BY_PP_URL);//gets the most popular movies
        } else {
            showErrorMessage();
        }


    }
    @Override
    protected void onResume(){
        super.onResume();
        mLoadingIndicator.setVisibility(View.VISIBLE);


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


    // MovieDB lader  loads movies from the network

    LoaderManager.LoaderCallbacks<Movie[]>

    private LoaderManager.LoaderCallbacks<Movie[]> MovieLoaderListener
            = new  LoaderManager.LoaderCallbacks<Movie[]>() {
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


            if (movies != null && movies.length != 0) {

                results = getPosterPaths(movies);


                String[] posterArray = new String[results != null ? results.size() : 0];// initialize sting array to size of result
                posterArray = results != null ? results.toArray(posterArray) : new String[0];

                mGridView.setAdapter(new MoviesPostersAdapter(mContext, posterArray));
                mLoadingIndicator.setVisibility(View.INVISIBLE);


                //Grid View setOnItemClickListener that leads to detail page
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {


                        PacelableMethod(movies, position);


                        Toast.makeText(MainActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                //If the array list of movies poster data is null, show the error message
                showErrorMessage();
            }
        }

        @Override
        public void onLoaderReset(Loader<Movie[]> loader) {

        }

    };





    //Favorite Movies Loader. Loads favorite movies from DataBase.




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


}