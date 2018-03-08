package com.example.sun.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sun.popularmovies.activity.MainActivity;
import com.example.sun.popularmovies.fragment.MainFragment;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sun on 2017/11/24.
 */

public class MovieTask extends AsyncTask<String,Void,JsonModel> {

    private static final String TAG = "MovieTask";

    private Context mContext;
    private AsyncTaskListener<JsonModel> mListener;

    public MovieTask(Context context,AsyncTaskListener<JsonModel> listener){
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();
        mListener.onTaskPre();
    }

    @Override
    protected JsonModel doInBackground(String... strings) {
        if (!NetworkUtil.isNetworkConnected(mContext)){
            return null;
        }else {
            MainFragment.isLoading = true;
            if (strings != null) {
                URL url = NetworkUtil.buildUrl(strings[0]);
                String response;
                try {
                    response = NetworkUtil.getResponseFromHttp(url);
                    Log.d(MainActivity.class.getSimpleName(), response);
                    return NetworkUtil.parseJson(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(JsonModel jsonModel) {
//        super.onPostExecute(jsonModel);
        mListener.onTaskComplete(jsonModel);
    }

}
