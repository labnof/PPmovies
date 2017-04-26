package com.example.babatundeanafi.ppmovies.control;

import com.example.babatundeanafi.ppmovies.model.RequestResult;
import com.google.gson.Gson;

public class JsonToMovieObjs {

    //Converts Request Result to RequestResult object

    public static RequestResult ConvertToResultObject(String JsonString) {

        Gson gson = new Gson();  //Gson object
        return gson.fromJson(JsonString, RequestResult.class);
    }
}


