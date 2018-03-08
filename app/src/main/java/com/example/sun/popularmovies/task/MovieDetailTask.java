package com.example.sun.popularmovies.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.sun.popularmovies.model.JsonDetailModel;
import com.example.sun.popularmovies.util.NetworkUtil;

import java.io.IOError;
import java.io.IOException;
import java.net.URL;

/**
 * Created by sun on 2018/1/17.
 */

public class MovieDetailTask extends AsyncTask<Long,Void,JsonDetailModel> {

    private AsyncTaskListener mListener;
    private Context mContext;

    public MovieDetailTask(){}

    public MovieDetailTask(Context context,AsyncTaskListener listener){
        mListener = listener;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        mListener.onTaskPre();
    }

    @Override
    protected JsonDetailModel doInBackground(Long... strings) {
        if (!NetworkUtil.isNetworkConnected(mContext)){
            return null;
        }else {
            if (strings!=null){
                URL url = NetworkUtil.buildDetailUrl(strings[0]);
                String response;
                try {
                    response = NetworkUtil.getResponseFromHttp(url);
                    return NetworkUtil.parseJson1(response);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    @Override
    protected void onPostExecute(JsonDetailModel jsonDetailModel) {
        mListener.onTaskComplete(jsonDetailModel);
    }
}
