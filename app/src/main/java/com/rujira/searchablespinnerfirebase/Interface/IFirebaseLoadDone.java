package com.rujira.searchablespinnerfirebase.Interface;

import com.rujira.searchablespinnerfirebase.Model.Movie;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Movie> movieList);
    void onFirebaseLoadFailed(String message);
}
