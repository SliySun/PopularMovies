package com.example.sun.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sun on 2017/11/22.
 *
 */

public class JsonModel {
    public int page;
    public int total_results;
    public int total_pages;

    public  List<MovieModel> results;

    public class MovieModel implements Serializable{
        public int vote_count;
        public long id;
        public boolean video;
        public float vote_average;
        public String title;
        public double popularity;
        public String poster_path;
        public String original_language;
        public String original_title;
        public List<Integer> genre_ids;
        public String backdrop_path;
        public boolean adult;
        public String overview;
        public String release_date;

        @Override
        public String toString() {
            return "MovieModel{" +
                    "vote_count=" + vote_count +
                    ", id=" + id +
                    ", video=" + video +
                    ", vote_average=" + vote_average +
                    ", title='" + title + '\'' +
                    ", popularity=" + popularity +
                    ", poster_path='" + poster_path + '\'' +
                    ", original_language='" + original_language + '\'' +
                    ", original_title='" + original_title + '\'' +
                    ", genre_ids=" + genre_ids +
                    ", backdrop_path='" + backdrop_path + '\'' +
                    ", adult=" + adult +
                    ", overview='" + overview + '\'' +
                    ", release_date='" + release_date + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonModel{" +
                "page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", results=" + results +
                '}';
    }
}
