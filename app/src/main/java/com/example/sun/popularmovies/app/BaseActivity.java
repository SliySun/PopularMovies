package com.example.sun.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by sun on 2017/11/24.
 *
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent!=null){
            handleIntent(intent);
        }

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        initView();
        initData(savedInstanceState);
    }

    protected void initData(Bundle savedInstanceState) {
    }

    protected void initView() {
    }

    protected void handleIntent(Intent intent) {
    }

    protected abstract int getLayoutRes();
}
