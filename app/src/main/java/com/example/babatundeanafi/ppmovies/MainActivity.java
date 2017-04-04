package com.example.babatundeanafi.ppmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babatundeanafi.ppmovies.Model.Movie;
import com.example.babatundeanafi.ppmovies.Model.RequestResult;
import com.example.babatundeanafi.ppmovies.control.JsonToMovieObjs;
import com.example.babatundeanafi.ppmovies.control.MoviesPostersAdapter;
import com.example.babatundeanafi.ppmovies.control.NetworkUtils;
import com.example.babatundeanafi.ppmovies.views.MovieDetailActivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.babatundeanafi.ppmovies.control.NetworkUtils.buildSortUrl;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class MainActivity extends AppCompatActivity{



    public static final String MOVIE_DETAIL = "com.example.PPmovies.Detail";
    private static final String PP_URL = "popularity.desc";
    private static final String VOTE_AVE_URL = "vote_average.desc";
    private String Api_key;
    boolean mNetworkAvailable = FALSE;


    private TextView mErrorMessageDisplay; //TextView variable for the error message display
    private ProgressBar mLoadingIndicator; //ProgressBar variable to show and hide the progress bar
    private GridView mGridView;
    private Context mContext;






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
        Api_key = res.getString(R.string.themoviedbApi_key);
        mContext = this.getApplication();


        // checks if internet is available and deplays posters and shows error if no internet
        mNetworkAvailable=isNetworkAvailable();
        if(  mNetworkAvailable == TRUE){
            getSortMovies(PP_URL);//gets the most popular movies
        }
        else{
            showErrorMessage();
        }

















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
    private void getSortMovies(String sortBy){

        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        URL url1 = buildSortUrl( sortBy, Api_key);
        URL url = NetworkUtils.ValidateUrl(url1.toString());
        mGridView = (GridView)findViewById(R.id.usage_example_gridview);

        //execute AsykTask
        themoviedbSortTask tm =  new themoviedbSortTask();
        tm.execute(url);



    }



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
                getSortMovies(PP_URL);//gets the most popular movies
                return true;
            case R.id.TR_option:
                getSortMovies(VOTE_AVE_URL);//gets the top rated  movies
                return true;
            case R.id.favorite_option:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    //Network AsyncTask
    private class themoviedbSortTask extends AsyncTask<URL, Void, Movie[]> {

        // COMPLETED (18) Within your AsyncTask, override the method onPreExecute and show the loading indicator
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }



        @Override
        protected  Movie[] doInBackground(URL... urls) {

            URL sortUrl = urls[0];

                String JsonSortResult = getJsonSortResult(sortUrl.toString());
                Movie[] movies = getMoviesLinks(JsonSortResult);


            if (JsonSortResult != null &&!JsonSortResult.isEmpty()){
                return movies;

            }



              return null;
        }

        @Override
        protected void onPostExecute (final Movie[] movies) {

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




                        PacelableMethod(movies,position );





                        Toast.makeText(MainActivity.this, "" + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

            else {
              //If the array list of movies poster data is null, show the error message
                showErrorMessage();
            }
        }

        public void PacelableMethod(Movie[] movies, int position){


            Movie mMovie = movies[position];
            Intent mIntent = new Intent(MainActivity.this, MovieDetailActivity.class);

            Bundle mBundle = new Bundle();

            mBundle.putParcelable(MOVIE_DETAIL, mMovie);
            mIntent.putExtras(mBundle);

            startActivity(mIntent);
        }


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

            private Movie[] getMoviesLinks (String NetworkResult){
                //Method returns an the array of movies contained in the network result

                Movie[] movies;
                RequestResult requestResult;

                if (NetworkResult != null && !NetworkResult.equals("")) {


                    requestResult = JsonToMovieObjs.ConvertToResultObject(NetworkResult);
                    movies =requestResult.getResults();
                    return movies;
                }


                return null;
            }


            private ArrayList<String> getPosterPaths (Movie[]m){

                ArrayList<String>  PosterPaths = new ArrayList<>();



                if (m.length != 0) {



                    for (Movie movies :m) {

                        URL url = NetworkUtils.buildImageUrl( movies.getPoster_path());
                        String sd = url.toString();
                        PosterPaths.add(sd);
                    }

                    return PosterPaths;
                }
                return null;

            }




        }

      }