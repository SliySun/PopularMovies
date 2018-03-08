package com.example.sun.popularmovies.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.activity.DetailActivity;
import com.example.sun.popularmovies.activity.MainActivity;
import com.example.sun.popularmovies.adapter.MovieAdapter;
import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.provider.MovieContract;
import com.example.sun.popularmovies.task.AsyncTaskListener;
import com.example.sun.popularmovies.task.MovieTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sun.popularmovies.activity.MainActivity.twoPane;
import static com.example.sun.popularmovies.provider.MovieContract.BASE_CONTENT_URI;
import static com.example.sun.popularmovies.provider.MovieContract.PATH_MOVIES;

/**
 * Created by sun on 2018/1/16.
 */

public class MainFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler,LoaderManager.LoaderCallbacks<Cursor>,SwipeRefreshLayout.OnRefreshListener{

    private static final int MOVIE_LOADER_ID = 100;
    @BindView(R.id.recyclerView_movies_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.textView_error)
    TextView mTv_error;
    @BindView(R.id.swipeLayout_movies_refresh)
    SwipeRefreshLayout mSwipe_refresh;

    private MovieAdapter mAdapter;
    public static boolean isLoading;
    private String sort_order;
    private boolean isRefreshing = false;

    public MainFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initView();
        initData(savedInstanceState);
    }

    private void initData(Bundle savedInstanceState) {
        sort_order = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getContext().getString(R.string.sort_order_key),"popular");
        setSortOrder(sort_order);
    }

    private void setSortOrder(String order) {
        switch (order){
            case "popular":
                String path = Constant.POPULAR_MOVIES_URL;
                new MovieTask(getContext(), new MovieTaskListener()).execute(path);
                break;
            case "rate":
                path = Constant.TOP_RATED_MOVIES_URL;
                new MovieTask(getContext(), new MovieTaskListener()).execute(path);
                break;
            case "favor":
                showLoading();
                getDataFromDB();
                break;
        }
    }

    private void initView() {
        initRecyclerView();
        initSwipeLayout();
    }

    private void initSwipeLayout() {
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipe_refresh.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        mSwipe_refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipe_refresh.setSize(SwipeRefreshLayout.DEFAULT);
        //设置监听
        mSwipe_refresh.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        /*
        * 手机屏幕时，2列
        * 平板屏幕是，3列
        * */
        int columnCount;
        getConfiguration();
        if (twoPane && getConfiguration() == Configuration.ORIENTATION_LANDSCAPE ) columnCount = 3;
        else columnCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),columnCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(getContext(),this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 获取屏幕是否为横屏
     * @return
     */
    private int getConfiguration() {
        Configuration cfg = getContext().getResources().getConfiguration();
        return  cfg.orientation;

    }

    @Override
    public void onItemOnClick(JsonModel.MovieModel model) {
        if (twoPane){
            Fragment detail =  getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
            ((OnItemChangedListener)detail).onItemChanged(model);
        }else {
            startActivity(new Intent(getContext(),DetailActivity.class).putExtra("MovieModel",model));
        }
    }

    private void showError(String msg) {
        mTv_error.setVisibility(View.VISIBLE);
        mTv_error.setText(msg);
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

    public void changeSortOrder(String order) {
        if (order.equals(sort_order)) {
            return;
        }
        sort_order = order;
        setSortOrder(order);

        if (twoPane) {
            DetailFragment detail = (DetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
            detail.showLoading();
        }
    }

    private void getDataFromDB() {
        Loader<Cursor> cursorLoader = getActivity().getSupportLoaderManager().getLoader(MOVIE_LOADER_ID);
        if (cursorLoader == null){
            System.out.println("init");
            getActivity().getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            System.out.println("restart");
            getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri MOVIE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        return new CursorLoader(getContext(), MOVIE_URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        isRefreshing = false;
        mSwipe_refresh .setRefreshing(false);
        showData();
        List<JsonModel.MovieModel> list = new ArrayList<>();
        if (data == null || data.getCount()<1) showError("没有收藏电影");
        else
        while (data.moveToNext()){
            JsonModel.MovieModel model  = new JsonModel.MovieModel();
            long id =data.getLong(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            String path = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URL));
            model.id = id;
            model.poster_path = path;
            list.add(model);
        }
        mAdapter.setMoviesData(list);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.out.println("a");
        mAdapter.setMoviesData(null);
    }

    @Override
    public void onRefresh() {
        if (!isRefreshing){
            isRefreshing = true;
            setSortOrder(sort_order);
        }
    }

    class MovieTaskListener implements AsyncTaskListener<JsonModel> {

        @Override
        public void onTaskPre() {
            isRefreshing = true;
            showLoading();
        }

        @Override
        public void onTaskComplete(JsonModel result) {
            isLoading = false;
            mSwipe_refresh.setRefreshing(false);
            isRefreshing = false;
            if (result!=null){
                mAdapter.setMoviesData(result.results);
                showData();
                if (twoPane){
                    Fragment detail = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
                    ((OnItemChangedListener) detail).onItemChanged(result.results.get(0));
                }
            }else {
                showError("当前网络不可用");
            }
        }
    }

    public interface OnItemChangedListener{
        void onItemChanged(JsonModel.MovieModel model);
    }
}
