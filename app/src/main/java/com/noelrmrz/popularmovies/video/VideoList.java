package com.noelrmrz.popularmovies.video;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {

    @SerializedName("results")
    private List<Video> mVideos;

    public VideoList(List<Video> videos) {
        mVideos = videos;
    }

    public List<Video> getMVideos() {
        return mVideos;
    }

    public void setMVideos(List<Video> videos) {
        mVideos = videos;
    }
}
