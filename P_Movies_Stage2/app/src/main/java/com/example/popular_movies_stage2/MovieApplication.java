package com.example.popular_movies_stage2;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
