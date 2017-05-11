package com.example.babatundeanafi.ppmovies.control;

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
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private final static String API_KEY_PARAM = "api_key";

    //REVIEW AND VIDEO
    private static final String REVIEW_VIDEO_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static String movieIdArg;
    public static final String VIDEO_URL = REVIEW_VIDEO_BASE_URL+ "/movie/"+ movieIdArg +"/videos";
    public static final String REVIEW_URL = REVIEW_VIDEO_BASE_URL + "/movie/" + movieIdArg + "/reviews" ;




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


    public static URL buildSortUrl(String SortURL, String apIKey) {
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




    public static URL buildVideoUrl(String mId, String apIKey) {
        //Method to build the video URL

        movieIdArg = mId;
        String bUrl = VIDEO_URL;



        Uri builtUri = Uri.parse(bUrl).buildUpon()
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


    public static URL buildReviewUrl(String mId, String apIKey) {
        //Method to build the review URL

        movieIdArg = mId;
        String bUrl = REVIEW_URL;



        Uri builtUri = Uri.parse(bUrl).buildUpon()
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







    public static URL ValidateUrl(String Sort_URL) {

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


}