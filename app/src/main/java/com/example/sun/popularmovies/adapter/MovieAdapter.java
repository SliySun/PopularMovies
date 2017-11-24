package com.example.sun.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sun.popularmovies.R;
import com.example.sun.popularmovies.model.JsonModel;
import com.example.sun.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sun on 2017/11/23.
 *
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Context mContext;
    private List<JsonModel.MovieModel> mMovieModels = new ArrayList<>();
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Context context,MovieAdapterOnClickHandler handler){
        mContext = context;
        mClickHandler = handler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        JsonModel.MovieModel model = mMovieModels.get(position);
        Picasso.with(mContext)
                .load(NetworkUtil.buildPosterUrl(model).toString())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mMovieModels.size();
    }

    public void setMoviesData(List<JsonModel.MovieModel> list){
        mMovieModels = list;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler{
        void onItemOnClick(JsonModel.MovieModel model);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.imageView_movie_poster) ImageView imageView;

        private MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            JsonModel.MovieModel model =mMovieModels.get(position);
            mClickHandler.onItemOnClick(model);
        }
    }
}
