package com.example.sun.popularmovies.task;

/**
 * Created by sun on 2017/11/24.
 */

public interface AsyncTaskListener<T> {

    void onTaskPre();

    void onTaskComplete(T result);

}
