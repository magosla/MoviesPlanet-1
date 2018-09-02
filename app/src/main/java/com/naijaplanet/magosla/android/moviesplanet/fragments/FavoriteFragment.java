package com.naijaplanet.magosla.android.moviesplanet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class FavoriteFragment extends Fragment {
    public static FavoriteFragment newInstance(int movieId){
        FavoriteFragment f = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt("movie_id", movieId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
