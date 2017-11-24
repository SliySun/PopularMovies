package com.example.sun.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
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

    public static class MovieModel implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.vote_count);
            dest.writeLong(this.id);
            dest.writeByte(this.video ? (byte) 1 : (byte) 0);
            dest.writeFloat(this.vote_average);
            dest.writeString(this.title);
            dest.writeDouble(this.popularity);
            dest.writeString(this.poster_path);
            dest.writeString(this.original_language);
            dest.writeString(this.original_title);
            dest.writeList(this.genre_ids);
            dest.writeString(this.backdrop_path);
            dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
            dest.writeString(this.overview);
            dest.writeString(this.release_date);
        }

        public MovieModel() {
        }

        protected MovieModel(Parcel in) {
            this.vote_count = in.readInt();
            this.id = in.readLong();
            this.video = in.readByte() != 0;
            this.vote_average = in.readFloat();
            this.title = in.readString();
            this.popularity = in.readDouble();
            this.poster_path = in.readString();
            this.original_language = in.readString();
            this.original_title = in.readString();
            this.genre_ids = new ArrayList<Integer>();
            in.readList(this.genre_ids, Integer.class.getClassLoader());
            this.backdrop_path = in.readString();
            this.adult = in.readByte() != 0;
            this.overview = in.readString();
            this.release_date = in.readString();
        }

        public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
            @Override
            public MovieModel createFromParcel(Parcel source) {
                return new MovieModel(source);
            }

            @Override
            public MovieModel[] newArray(int size) {
                return new MovieModel[size];
            }
        };
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
