package com.example.sun.popularmovies.model;

import java.util.List;

/**
 * Created by sun on 2018/1/17.
 */

public class JsonDetailModel {
    public boolean adult;
    public String backdrop_path;
    public Belongs_to_collection belongs_to_collection;
    public long budget;
    public List<Genre> genres;
    public String homepage;
    public long id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public List<Production_company> production_companies;
    public List<Production_country> production_countries;
    public String release_date;
    public long revenue;
    public int runtime;

//    private Date release_date;
    public List<Spoken_language> spoken_languages;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public float vote_average;
    public int vote_count;
    public Trailers trailers;
    public Reviews reviews;


    public class Genre{
        public int id;
        public String name;
    }

    public class Belongs_to_collection{
        public int id;
        public String name;
        public String poster_path;
        public String backdrop_path;
    }

    public class Production_company {
        public int id;
        public String name;
    }

    public class Production_country {
        public String iso_3166_1;
        public String name;
    }

    public class Spoken_language {

        public String iso_639_1;
        public String name;
    }

    public class Youtube {

        public String name;
        public String size;
        public String source;
        public String type;
    }

    public class Trailers {
        public List<String> quicktime;
        public List<Youtube> youtube;}

    public class Reviews {
        public int page;
        public List<Results> results;
        public int total_pages;
        public int total_results;
    }

    public class Results {

        public String id;
        public String author;
        public String content;
        public String url;
    }

    @Override
    public String toString() {
        return "JsonDetailModel{" +
                "adult=" + adult +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", belongs_to_collection=" + belongs_to_collection +
                ", budget=" + budget +
                ", genres=" + genres +
                ", homepage='" + homepage + '\'' +
                ", id=" + id +
                ", imdb_id='" + imdb_id + '\'' +
                ", original_language='" + original_language + '\'' +
                ", original_title='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", poster_path='" + poster_path + '\'' +
                ", production_companies=" + production_companies +
                ", production_countries=" + production_countries +
                ", release_date='" + release_date + '\'' +
                ", revenue=" + revenue +
                ", runtime=" + runtime +
                ", spoken_languages=" + spoken_languages +
                ", status='" + status + '\'' +
                ", tagline='" + tagline + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                ", trailers=" + trailers +
                ", reviews=" + reviews +
                '}';
    }
}
