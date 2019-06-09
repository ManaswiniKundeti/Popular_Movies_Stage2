package com.example.popular_movies_stage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideo {

    @SerializedName("id")
    private String mId;

    @SerializedName("results")
    private List<MovieVideoResult> mVideoResults;

    public MovieVideo(String mId, List<MovieVideoResult> mVideoResults) {
        this.mId = mId;
        this.mVideoResults = mVideoResults;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public List<MovieVideoResult> getmVideoResults() {
        return mVideoResults;
    }

    public void setmVideoResults(List<MovieVideoResult> mVideoResults) {
        this.mVideoResults = mVideoResults;
    }
}
