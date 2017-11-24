package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.app.BaseActivity;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    private JsonModel.MovieModel mMovieModel;

    @BindView(R.id.textView_movie_name) TextView tv_title;
    @BindView(R.id.ratingBar_vote_stars) RatingBar ratingBar_star;
    @BindView(R.id.textView_vote_num) TextView tv_vote;
    @BindView(R.id.imageView_movie_detail_poster) ImageView iv_poster;
    @BindView(R.id.textView_movie_language) TextView tv_lang;
    @BindView(R.id.textView_movie_date) TextView tv_date;
    @BindView(R.id.textView_movie_overview) TextView tv_overView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void handleIntent(Intent intent) {
//        mMovieModel = (JsonModel.MovieModel) intent.getSerializableExtra("MovieModel");
        mMovieModel = intent.getParcelableExtra("MovieModel");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        tv_title.setText(mMovieModel.title);
        Picasso.with(DetailActivity.this)
                .load(NetworkUtil.buildPosterUrl(mMovieModel).toString())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(iv_poster);

        ratingBar_star.setRating(mMovieModel.vote_average/10*5);
        tv_vote.setText(String.valueOf(mMovieModel.vote_average));
        tv_lang.setText(mMovieModel.original_language);
        tv_date.setText(mMovieModel.release_date);
        tv_overView.setText(mMovieModel.overview);

    }

}
