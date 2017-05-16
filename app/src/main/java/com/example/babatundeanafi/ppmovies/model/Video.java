package com.example.babatundeanafi.ppmovies.model;

/**
 * Created by null on 11.5.2017.
 */

public class Video {
    public String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }
}