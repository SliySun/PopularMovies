package com.example.sun.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.example.sun.popularmovies.config.Constant;
import com.example.sun.popularmovies.model.JsonModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
}
