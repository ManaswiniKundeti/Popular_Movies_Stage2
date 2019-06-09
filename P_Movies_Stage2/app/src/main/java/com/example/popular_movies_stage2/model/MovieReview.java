package com.example.popular_movies_stage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReview {

    @SerializedName("results")
    private List<MovieReviewResults> mReviewResults;

    public MovieReview(List<MovieReviewResults> mReviewResults) {
        this.mReviewResults = mReviewResults;
    }

    public List<MovieReviewResults> getmReviewResults() {
        return mReviewResults;
    }

    public void setmReviewResults(List<MovieReviewResults> mReviewResults) {
        this.mReviewResults = mReviewResults;
    }
}
