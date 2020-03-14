package com.noelrmrz.popularmovies.movie;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName="favorite_movies")
public class Movie {

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name="id")
    private int mId;

    @SerializedName("poster_path")
    @ColumnInfo(name="poster_path")
    private String mPosterPath;

    @SerializedName("title")
    @ColumnInfo(name="title")
    private String mTitle;

    @SerializedName("vote_average")
    @ColumnInfo(name="vote_average")
    private String mRating;

    @SerializedName("overview")
    @ColumnInfo(name="overview")
    private String mSynopsis;

    @SerializedName("release_date")
    @ColumnInfo(name="release_date")
    private String mDate;

    @ColumnInfo(name="favorite")
    private boolean mFavorite;

    @Ignore
    public Movie (String posterPath, String title) {
        mPosterPath = posterPath;
        mTitle = title;
    }


    public Movie (int id, String posterPath, String title, String rating, String synopsis, String date) {
        mId = id;
        mPosterPath = posterPath;
        mTitle = title;
        mRating = rating;
        mSynopsis = synopsis;
        mDate = date;
    }

    public String getMPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getMTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getMSynopsis() {
        return mSynopsis;
    }

    public void setmSynopsis(String mSynopsis) {
        this.mSynopsis = mSynopsis;
    }

    public String getMDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getMId() {
        return mId;
    }

    public void setmId(int mId) { this.mId = mId; }

    public boolean getMFavorite() { return mFavorite; }

    public void setMFavorite(boolean value) { mFavorite = value; }
}
