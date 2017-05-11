package com.example.babatundeanafi.ppmovies.control;

import com.example.babatundeanafi.ppmovies.model.MovieRequestResult;
import com.example.babatundeanafi.ppmovies.model.Review;
import com.example.babatundeanafi.ppmovies.model.ReviewRequestResult;
import com.example.babatundeanafi.ppmovies.model.Video;
import com.example.babatundeanafi.ppmovies.model.VideoRequestResult;
import com.google.gson.Gson;

public class JsonToMovieObjs {

    //Converts Request Result to MovieRequestResult object

    public static MovieRequestResult ConvertMovieResultToObject(String JsonString) {

        Gson gson = new Gson();  //Gson object
        return gson.fromJson(JsonString, MovieRequestResult.class);
    }

    public static VideoRequestResult ConvertVideoResultToObject(String JsonString) {

        Gson gson = new Gson();  //Gson object
        return gson.fromJson(JsonString, VideoRequestResult.class);
    }
    public static ReviewRequestResult ConvertReviewResultToObject(String JsonString) {

        Gson gson = new Gson();  //Gson object
        return gson.fromJson(JsonString, ReviewRequestResult.class);
    }
}


