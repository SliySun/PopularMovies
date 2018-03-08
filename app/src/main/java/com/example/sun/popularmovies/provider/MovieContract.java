package com.example.sun.popularmovies.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sun on 2018/3/3.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.sun.popularmovies.favor_movie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final long INVALID_MOVIE_ID = -1;

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster_url";



        public static final String COLUMN_MOVIE_NAME = "name";


        public static final String COLUMN_MOVIE_VOTE_NUM = "vote_num";

        public static final String COLUMN_MOVIE_LANGUAGE= "language";

        public static final String COLUMN_MOVIE_DATE = "date";

        public static final String COLUMN_MOVIE_MINUTE = "minute";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

    }


}
