package com.example.babatundeanafi.ppmovies.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    //API_KEY
    //private Context context;
    //private  String API_KEY = getResources().getString(R.string.themoviedbApi_key);

    //Sort URL parameters
    private static final String SORT_BASE_URL = "https://api.themoviedb.org/3/discover/movie?page=1&include_video=false&include_adult=false";
    private static final String SORT_BY = "sort_by";


    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";


    //quary parameter
    final static String QUERY_PARAM = "q";
    final static String API_KEY_PARAM = "api_key";
    final static String APESANT_PARAM = "&";


    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param image_path The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildImageUrl(String image_path) {


  String img_url = IMAGE_BASE_URL + image_path;

        URL url = null;
        try {
            url = new URL(img_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static URL buildSortUrl(String SortURL, String apIKey ) {
        //Method to build the sorth URL


        Uri builtUri = Uri.parse(SortURL).buildUpon()
                //.appendQueryParameter(SORT_BY,SortURL)
                .appendQueryParameter(API_KEY_PARAM, apIKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL ValidateUrl( String Sort_URL) {

        Uri builtUri = Uri.parse(Sort_URL);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }







    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

    public static  Bitmap getResponseFromImgHttpUrl(URL url ) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


        try {
            Bitmap bmp = BitmapFactory.decodeStream(urlConnection.getInputStream());
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            urlConnection.disconnect();
        }
        return null;
    }
}