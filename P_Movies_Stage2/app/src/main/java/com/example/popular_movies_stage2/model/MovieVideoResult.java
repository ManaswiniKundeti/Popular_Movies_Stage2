package com.example.popular_movies_stage2.model;

import com.google.gson.annotations.SerializedName;

public class MovieVideoResult {

    @SerializedName("id")
    private String mVideoId;

    @SerializedName("key")
    private String mVideoKey;

    @SerializedName("name")
    private String mVideoName;

    @SerializedName("type")
    private String mType;

    public MovieVideoResult(String mVideoId, String mVideoKey, String mVideoName, String mType) {
        this.mVideoId = mVideoId;
        this.mVideoKey = mVideoKey;
        this.mVideoName = mVideoName;
        this.mType = mType;
    }

    public String getmVideoId() {
        return mVideoId;
    }

    public void setmVideoId(String mVideoId) {
        this.mVideoId = mVideoId;
    }

    public String getmVideoKey() {
        return mVideoKey;
    }

    public void setmVideoKey(String mVideoKey) {
        this.mVideoKey = mVideoKey;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
