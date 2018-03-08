package com.example.sun.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonDetailModel;
import com.example.sun.popularmovies.model.JsonModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sun on 2017/11/22.
 *
 */

public class NetworkUtil {

    /**
     *
     * @return url
     * http://api.themoviedb.org/3/movie/popular?api_key=46c0ae11201c1760cd483c97c9433a93
     */
    public static URL buildUrl(String type){
        Uri uri = Uri.parse(Constant.MOVIES_BASE_URL)
                     .buildUpon()
                     .appendEncodedPath(type)
                     .appendQueryParameter("api_key",Constant.API_KEY)
                     .build();

        /*
            Uri.Builder builder = new Uri.Builder();
            Uri uri1=builder.scheme("https")
                    .encodedAuthority("api.themoviedb.org/3")
                    .appendEncodedPath(Constant.POPULAR_MOVIES_URL)
                    .appendQueryParameter("api_key",Constant.API_KEY).build();
        */

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildDetailUrl(long id){
        Uri uri = Uri.parse(Constant.MOVIES_BASE_URL)
                .buildUpon()
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendQueryParameter("append_to_response","trailers%2reviews")
                .appendQueryParameter("api_key",Constant.API_KEY)
                .build();
        String urlFormat = "http://api.themoviedb.org/3/movie/%s?append_to_response=trailers,reviews&api_key=%s";
        String stringUrl = String.format(urlFormat, id, Constant.API_KEY);

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @return url
     */
    public static URL buildPosterUrl(JsonModel.MovieModel model){
        Uri uri = Uri.parse(Constant.POSTER_BASE_URL+Constant.POSTER_SIZE_W185)
                .buildUpon()
                .appendEncodedPath(model.poster_path)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildPosterUrl(JsonDetailModel model){
        Uri uri = Uri.parse(Constant.POSTER_BASE_URL+Constant.POSTER_SIZE_W185)
                .buildUpon()
                .appendEncodedPath(model.poster_path)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(6000);
        /*
            try {
                InputStream in = urlConnection.getInputStream();


                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        */
        return streamToString(urlConnection);
    }

    private static String streamToString(HttpURLConnection urlConnection) throws IOException {
        InputStream in = urlConnection.getInputStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = in.read(bytes))!=-1){
            baos.write(bytes,0,length);
        }
        return baos.toString("UTF-8");
    }

    public static boolean isNetworkConnected(Context context){
        if (context!=null){
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=manager.getActiveNetworkInfo();
            if (info!=null){
                return info.isAvailable();
            }
        }
        return false;
    }

    public static JsonModel parseJson(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<JsonModel>(){}.getType();
        JsonModel model= gson.fromJson(response, type);
        Log.d("model",model.toString());
        return model;
    }

    public static JsonDetailModel parseJson1(String response){
        Gson gson = new Gson();
        Type type = new TypeToken<JsonDetailModel>(){}.getType();
        JsonDetailModel model = gson.fromJson(response,type);
        Log.e("detailModel",model.reviews.results.size()+"");
        return model;
    }

    public static Uri buildYoutubeUri(String v) {
        return Uri.parse(Constant.YOUTUBE_URL)
                .buildUpon()
                .appendEncodedPath(Constant.YOUTUBE_PATH)
                .appendQueryParameter("v",v)
                .build();

    }
}
