package com.example.babatundeanafi.ppmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by null on 7.5.2017.
 */

public class FavouriteMovie implements Parcelable {
    private String poster_path;
    private String overview;
    private String release_date;
    private int id;
    private String original_title;
    private float vote_average;

    public FavouriteMovie() {
    }

    public FavouriteMovie(String poster_path, String overview, String release_date, int id, String original_title, float vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.original_title = original_title;
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeFloat(this.vote_average);
    }

    protected FavouriteMovie(Parcel in) {
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.id = in.readInt();
        this.original_title = in.readString();
        this.vote_average = in.readFloat();
    }

    public static final Creator<FavouriteMovie> CREATOR = new Creator<FavouriteMovie>() {
        @Override
        public FavouriteMovie createFromParcel(Parcel source) {
            return new FavouriteMovie(source);
        }

        @Override
        public FavouriteMovie[] newArray(int size) {
            return new FavouriteMovie[size];
        }
    };
}
