package com.example.sun.popularmovies.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.sun.popularmovies.fragment.DetailFragment;
import com.example.sun.popularmovies.fragment.MainFragment;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.task.AsyncTaskListener;
import com.example.sun.popularmovies.task.MovieTask;

import butterknife.BindView;


public class MainActivity extends BaseActivity {

    public static boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twoPane = (findViewById(R.id.fragment_detail) != null);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static String Sort_Order;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_type_pop:
                Sort_Order = Constant.POPULAR_SORT;
                Toast.makeText(this, "pop", Toast.LENGTH_SHORT).show();
                changeOrderSort(Sort_Order);
                break;
            case R.id.action_list_type_rate:
                Sort_Order = Constant.RATE_SORT;
                Toast.makeText(this, "rate", Toast.LENGTH_SHORT).show();
                changeOrderSort(Sort_Order);
                break;
            case R.id.action_list_type_favor:
                Sort_Order = Constant.FAVOR_SORT;
                Toast.makeText(this, "favor", Toast.LENGTH_SHORT).show();
                changeOrderSort(Sort_Order);
                break;

        }
        return true;
    }

    private void changeOrderSort(String sort_order) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        fragment.changeSortOrder(sort_order);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.sort_order_key), Sort_Order);
        editor.apply();
    }


}
