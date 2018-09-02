package com.naijaplanet.magosla.android.moviesplanet.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.naijaplanet.magosla.android.moviesplanet.Config;
import com.naijaplanet.magosla.android.moviesplanet.databinding.MovieItemBinding;
import com.naijaplanet.magosla.android.moviesplanet.models.Movie;
import com.naijaplanet.magosla.android.moviesplanet.util.MovieDbUtil;

public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final ViewDataBinding mBinding;
    private final MoviesAdapter mMoviesAdapter;
    private final MoviesAdapter.OnEventListener mEventListener;

    public static MoviesViewHolder create(LayoutInflater inflater, ViewGroup parent, final MoviesAdapter adapter, MoviesAdapter.OnEventListener eventListener){
        ViewDataBinding binding = MovieItemBinding.inflate(inflater, parent, false);

        return new MoviesViewHolder(binding, adapter, eventListener);

       // ViewDataBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.movie_item,parent,false);

    }

    private MoviesViewHolder(ViewDataBinding viewDataBinding, final MoviesAdapter adapter, MoviesAdapter.OnEventListener eventListener) {
        super(viewDataBinding.getRoot());
        mBinding = viewDataBinding;
        mMoviesAdapter=adapter;
        mEventListener=eventListener;
    }

    public void bind(Movie movie){
        itemView.setTag(movie.getId());
        String photoUrl = MovieDbUtil.getPhotoUrl(movie.getPosterPath(), Config.MOVIEDB_THUMBNAIL_SIZE);

        itemView.setOnClickListener(this);
        mBinding.setVariable(BR.movie, movie);
        mBinding.setVariable(BR.photo_url, photoUrl);
        mBinding.executePendingBindings();
    }

    @Override
    public void onClick(View v) {
        mEventListener.onItemClick(mMoviesAdapter.getMoviesRecord().getMovieAt(getAdapterPosition()));
    }
}