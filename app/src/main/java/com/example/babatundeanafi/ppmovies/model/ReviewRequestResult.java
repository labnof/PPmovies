package com.example.babatundeanafi.ppmovies.model;

/**
 * Created by null on 11.5.2017.
 */

public class ReviewRequestResult {

    private int id;
    private int page;
    private Review[] results;
    private int  total_pages;
    private int total_results;


    public Review[] getResults() {
        return results;
    }  }