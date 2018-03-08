package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.app.BaseActivity;
import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    /*
    *   http://api.themoviedb.org/3/movie/181808?append_to_response=trailers,reviews&api_key=46c0ae11201c1760cd483c97c9433a93
    * */

    public JsonModel.MovieModel getmMovieModel() {
        return mMovieModel;
    }

    private JsonModel.MovieModel mMovieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void handleIntent(Intent intent) {
        mMovieModel = intent.getParcelableExtra("MovieModel");

        long id = mMovieModel.id;
        String urlFormat = "http://api.themoviedb.org/3/movie/%s?append_to_response=trailers,reviews&api_key=%s";
        String url = String.format(urlFormat, id, Constant.API_KEY);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
