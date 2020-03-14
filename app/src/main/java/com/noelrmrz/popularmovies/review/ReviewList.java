package com.noelrmrz.popularmovies.review;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewList {

    @SerializedName("results")
    private List<Review> mReviewList;

    public ReviewList(List<Review> reviewList) {
        mReviewList = reviewList;
    }

    public List<Review> getmReviewList() {
        return mReviewList;
    }

    public void setmReviewList(List<Review> mReviewList) {
        this.mReviewList = mReviewList;
    }
}
