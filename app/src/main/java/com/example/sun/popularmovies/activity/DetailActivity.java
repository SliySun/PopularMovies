package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private JsonModel.MovieModel mMovieModel;

    private TextView tv_title;
    private RatingBar ratingBar_star;
    private TextView tv_vote;
    private ImageView iv_poster;
    private TextView tv_lang;
    private TextView tv_date;
    private TextView tv_overView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity!=null){
            mMovieModel = (JsonModel.MovieModel) intentThatStartedThisActivity.getSerializableExtra("MovieModel");
        }
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

    private void initView() {
        tv_title = findViewById(R.id.textView_movie_name);
        ratingBar_star = findViewById(R.id.ratingBar_vote_stars);
        tv_vote = findViewById(R.id.textView_vote_num);
        iv_poster = findViewById(R.id.imageView_movie_detail_poster);
        tv_lang = findViewById(R.id.textView_movie_language);
        tv_date = findViewById(R.id.textView_movie_date);
        tv_overView = findViewById(R.id.textView_movie_overview);
    }
}
