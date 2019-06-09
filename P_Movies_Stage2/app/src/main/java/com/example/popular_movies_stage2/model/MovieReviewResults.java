package com.example.popular_movies_stage2.model;

import com.google.gson.annotations.SerializedName;

public class MovieReviewResults {

    @SerializedName("author")
    private String mReviewResultAuthor;

    @SerializedName("content")
    private String mReviewResultContent;

    @SerializedName("id")
    private String mReviewResultId;

    @SerializedName("url")
    private String getmReviewResultUrl;

    public MovieReviewResults(String mReviewResultAuthor, String mReviewResultContent, String mReviewResultId, String getmReviewResultUrl) {
        this.mReviewResultAuthor = mReviewResultAuthor;
        this.mReviewResultContent = mReviewResultContent;
        this.mReviewResultId = mReviewResultId;
        this.getmReviewResultUrl = getmReviewResultUrl;
    }

    public String getmReviewResultAuthor() {
        return mReviewResultAuthor;
    }

    public void setmReviewResultAuthor(String mReviewResultAuthor) {
        this.mReviewResultAuthor = mReviewResultAuthor;
    }

    public String getmReviewResultContent() {
        return mReviewResultContent;
    }

    public void setmReviewResultContent(String mReviewResultContent) {
        this.mReviewResultContent = mReviewResultContent;
    }

    public String getmReviewResultId() {
        return mReviewResultId;
    }

    public void setmReviewResultId(String mReviewResultId) {
        this.mReviewResultId = mReviewResultId;
    }

    public String getGetmReviewResultUrl() {
        return getmReviewResultUrl;
    }

    public void setGetmReviewResultUrl(String getmReviewResultUrl) {
        this.getmReviewResultUrl = getmReviewResultUrl;
    }
}
