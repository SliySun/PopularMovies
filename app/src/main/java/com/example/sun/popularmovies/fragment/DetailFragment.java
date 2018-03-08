package com.example.sun.popularmovies.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.activity.MainActivity;
import com.example.sun.popularmovies.model.JsonDetailModel;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.provider.MovieContract;
import com.example.sun.popularmovies.task.AsyncTaskListener;
import com.example.sun.popularmovies.task.MovieDetailTask;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sun.popularmovies.provider.MovieContract.BASE_CONTENT_URI;
import static com.example.sun.popularmovies.provider.MovieContract.PATH_MOVIES;

/**
 * Created by sun on 2018/1/16.
 */

public class DetailFragment extends Fragment implements MainFragment.OnItemChangedListener, AsyncTaskListener<JsonDetailModel> ,LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.textView_movie_name)
    TextView tv_title;
    @BindView(R.id.ratingBar_vote_stars)
    RatingBar ratingBar_star;
    @BindView(R.id.textView_vote_num)
    TextView tv_vote;
    @BindView(R.id.imageView_movie_detail_poster)
    ImageView iv_poster;
    @BindView(R.id.textView_movie_language)
    TextView tv_lang;
    @BindView(R.id.textView_movie_date)
    TextView tv_date;
    @BindView(R.id.textView_movie_min)
    TextView tv_min;
    @BindView(R.id.textView_movie_overview)
    TextView tv_overView;
    @BindView(R.id.progressBar_indicator)
    ProgressBar pb_indicator;
    @BindView(R.id.linearLayout_trailers)
    LinearLayout ll_trailers;
    @BindView(R.id.textView_reviews)
    TextView tv_reviews;
    @BindView(R.id.linearLayout_reviews)
    LinearLayout ll_reviews;
    @BindView(R.id.scrollView_detail)
    ScrollView sv_detail;
    @BindView(R.id.imageView_movie_favor)
    ImageView iv_favor;
    @BindView(R.id.textView_detail_error)
    TextView tv_error;


    private JsonModel.MovieModel mMovieModel;
    private boolean isFavor;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (MainActivity.twoPane) {
            mMovieModel = null;
        } else {
            Intent intent = getActivity().getIntent();
            mMovieModel = intent.getParcelableExtra("MovieModel");
            initData(savedInstanceState);
        }
    }

    private void initData(Bundle savedInstanceState) {
        setData(mMovieModel);
    }

    @Override
    public void onItemChanged(JsonModel.MovieModel model) {
        mMovieModel = model;
        setData(model);
    }

    private static final int SINGLE_LOADER_ID = 200;

    private void setData(JsonModel.MovieModel model) {
        long id = model.id;
        Log.e("detail", id + "");
        new MovieDetailTask(getContext(), this).execute(id);
        getActivity().getSupportLoaderManager().initLoader(SINGLE_LOADER_ID, null,this);;
    }

    @Override
    public void onTaskPre() {
        showLoading();
    }

    public void showLoading() {
        pb_indicator.setVisibility(View.VISIBLE);
        sv_detail.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTaskComplete(JsonDetailModel result) {
        if (result != null) {
            setDetailData(result);
            showData();
        } else {
            showError("该电影没有详细信息");
        }
    }

    private void setDetailData(final JsonDetailModel result) {
        tv_title.setText(result.title);
        Picasso.with(getContext())
                .load(NetworkUtil.buildPosterUrl(result).toString())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(iv_poster);
        String min = String.valueOf(result.runtime) + " min";
        tv_min.setText(min);
        ratingBar_star.setRating(result.vote_average / 10 * 5);
        tv_vote.setText(String.valueOf(result.vote_average));
        if (result.spoken_languages.size()>0){
        tv_lang.setText(result.spoken_languages.get(0).name);}
        tv_date.setText(result.release_date);
        tv_overView.setText(result.overview);
        ll_trailers.removeAllViews();
        ll_reviews.removeAllViews();
        int trailerSize = result.trailers.youtube.size();
        if (trailerSize > 0) {
            for (int i = 0; i < trailerSize; i++) {
                final int index = i;
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_trailer_item, ll_trailers, false);
                final TextView tv = view.findViewById(R.id.textView_trailer_name);
                tv.setText(result.trailers.youtube.get(i).name);
                ll_trailers.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String v = result.trailers.youtube.get(index).source;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(NetworkUtil.buildYoutubeUri(v));
                        System.out.println(NetworkUtil.buildYoutubeUri(v).toString());
                        startActivity(intent);
                    }
                });
            }
        }
        int reviewSize = result.reviews.results.size();
        if (reviewSize > 0) {
            for (int j = 0; j < reviewSize; j++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_review_item, ll_reviews, false);
                TextView tv_name = view.findViewById(R.id.textView_review_name);
                TextView tv_content = view.findViewById(R.id.textView_review_content);

                tv_name.setText(result.reviews.results.get(j).author);
                tv_content.setText(result.reviews.results.get(j).content);
                ll_reviews.addView(view);
            }
        }
    }

    private void showData() {
        pb_indicator.setVisibility(View.INVISIBLE);
        sv_detail.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
    }

    private void showError(String msg) {
        pb_indicator.setVisibility(View.INVISIBLE);
        sv_detail.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        tv_error.setText(msg);
    }

    @OnClick(R.id.imageView_movie_favor)
    public void setIv_favor(ImageView view){
        if (isFavor){
            Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build(), mMovieModel.id);
            Uri MOVIE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
            getContext().getContentResolver().delete(MOVIE_URI, MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{String.valueOf(mMovieModel.id)});
            view.setImageResource(R.drawable.b_newcare_tabbar);
            Toast.makeText(getContext(),"取消收藏",Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieModel.id);
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL, mMovieModel.poster_path);
            getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
            view.setImageResource(R.drawable.b_newcare_tabbar_press);
            Toast.makeText(getContext(),"添加收藏",Toast.LENGTH_SHORT).show();
        }
        isFavor = !isFavor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build(), mMovieModel.id);
        Uri MOVIE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
/*
        return new CursorLoader(getContext(), SINGLE_PLANT_URI, new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(mMovieModel.id)}, null);
*/
        return new CursorLoader(getContext(), MOVIE_URI, new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(mMovieModel.id)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1){
            iv_favor.setImageResource(R.drawable.b_newcare_tabbar);
            isFavor = false;
        }else {
            iv_favor.setImageResource(R.drawable.b_newcare_tabbar_press);
            isFavor = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
