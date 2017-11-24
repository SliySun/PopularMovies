package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.adapter.MovieAdapter;
import com.example.sun.popularmovies.app.BaseActivity;
import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.task.AsyncTaskListener;
import com.example.sun.popularmovies.task.MovieTask;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    @BindView(R.id.recyclerView_movies_list) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar_indicator) ProgressBar mProgressBar;
    @BindView(R.id.textView_error) TextView mTv_error;
    private MovieAdapter mAdapter;

    private boolean isPopular = true;
    public static boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initData(isPopular);
    }

    private void initData(boolean isPopular) {
        String path;
        if (isPopular){
            path = Constant.POPULAR_MOVIES_URL;
        }else {
            path = Constant.TOP_RATED_MOVIES_URL;
        }
        new MovieTask(this, new MovieTaskListener()).execute(path);
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(MainActivity.this,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemOnClick(JsonModel.MovieModel model) {
        Toast.makeText(this,model.title,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,DetailActivity.class).putExtra("MovieModel",model));
    }

    private void showError() {
        mTv_error.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showData() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mTv_error.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mTv_error.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_list_type:
                if (isLoading){
                    Toast.makeText(this,R.string.loading,Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (isPopular){
                    item.setTitle(R.string.movie_popular);
                }else {
                    item.setTitle(R.string.movie_rated);
                }
                isPopular= !isPopular;
                initData(isPopular);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MovieTaskListener implements AsyncTaskListener<JsonModel> {

        @Override
        public void onTaskPre() {
            showLoading();
        }

        @Override
        public void onTaskComplete(JsonModel result) {
            isLoading = false;
            if (result!=null){
                showData();
                mAdapter.setMoviesData(result.results);
            }else {
                showError();
            }
        }
    }
}
