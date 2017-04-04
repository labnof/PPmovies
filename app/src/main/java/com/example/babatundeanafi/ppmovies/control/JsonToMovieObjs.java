package com.example.babatundeanafi.ppmovies.control;

import com.example.babatundeanafi.ppmovies.Model.RequestResult;
import com.google.gson.Gson;

/**
 * Created by babatundeanafi on 23/02/2017.
 */

public class JsonToMovieObjs {

    //Converts Request Result to RequestResult object

    public static RequestResult ConvertToResultObject(String JsonString ) {

        Gson gson = new Gson();  //Gson object
        RequestResult result = gson.fromJson(JsonString, RequestResult.class);
        return result;

    }



}


