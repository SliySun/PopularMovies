package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.adapter.MovieAdapter;
import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTv_error;
    private MovieAdapter mAdapter;

    private boolean isPopular = true;
    private boolean isLodding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        initView();
        initData(isPopular);
    }

    private void initData(boolean isPopular) {
        String path;
        if (isPopular){
            path = Constant.POPULAR_MOVIES_URL;
        }else {
            path = Constant.TOP_RATED_MOVIES_URL;
        }
        new MovieTask().execute(path);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView_movies_list);
        mProgressBar = findViewById(R.id.progressBar_indicator);
        mTv_error = findViewById(R.id.textView_error);
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


    class MovieTask extends AsyncTask<String,Void,JsonModel>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected JsonModel doInBackground(String... urls) {
            Log.d("onPost","a");
            if (!NetworkUtil.isNetworkConnected(MainActivity.this)){
                return null;
            }else {
                isLodding = true;
                if (urls != null) {
                    URL url = NetworkUtil.buildUrl(urls[0]);
                    String response;
                    try {
                        response = NetworkUtil.getResponseFromHttp(url);
                        Log.d(MainActivity.class.getSimpleName(), response);
                        return parseJson(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }

         @Override
         protected void onPostExecute(JsonModel movieModel) {
             isLodding = false;
            if (movieModel!=null){
                showData();
                mAdapter.setMoviesData(movieModel.results);
            }else {
                showError();
            }
         }


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

    private JsonModel parseJson(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<JsonModel>(){}.getType();
        JsonModel model= gson.fromJson(response, type);
        Log.d("model",model.toString());
        return model;
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
                if (isLodding){
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
}
