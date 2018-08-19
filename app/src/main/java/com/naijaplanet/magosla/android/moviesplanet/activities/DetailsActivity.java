package com.naijaplanet.magosla.android.moviesplanet.activities;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.naijaplanet.magosla.android.moviesplanet.Config;
import com.naijaplanet.magosla.android.moviesplanet.R;
import com.naijaplanet.magosla.android.moviesplanet.databinding.ActivityDetailsBinding;
import com.naijaplanet.magosla.android.moviesplanet.models.Movie;
import com.naijaplanet.magosla.android.moviesplanet.util.MovieDbUtil;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding mActivityDetailsBinding;
    private static final String BUNDLE_FULL_PHOTO = "full_photo_open";
    private boolean fullPhotoOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }
        Bundle extras = intent.getExtras();
        Movie movie = null;
        if (extras != null)
            movie = extras.getParcelable(Config.EXTRA_MOVIE_KEY);

        if (movie == null) {
            closeOnError();
            return;
        }

        setSupportActionBar(mActivityDetailsBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(movie.getTitle());
        }

        mActivityDetailsBinding.setVariable(BR.movie, movie);
        String photo_url = MovieDbUtil.getPhotoUrl(movie.getPosterPath(), Config.MOVIEDB_FULL_SIZE);
        mActivityDetailsBinding.setVariable(BR.photo_url, photo_url);

        mActivityDetailsBinding.postalIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullPhoto(true);
            }
        });

        mActivityDetailsBinding.ibActionClosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullPhoto(false);
            }
        });

        tryRestoreFullPhoto(savedInstanceState);
    }

    private void tryRestoreFullPhoto(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_FULL_PHOTO)) {
            fullPhotoOpen = savedInstanceState.getBoolean(BUNDLE_FULL_PHOTO);
            if (fullPhotoOpen) {
                toggleFullPhoto(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fullPhotoOpen) {
            toggleFullPhoto(false);
            return;
        }
        super.onBackPressed();
    }

    private void toggleFullPhoto(boolean open) {
        if (open) {
            mActivityDetailsBinding.appbar.setVisibility(View.GONE);
            mActivityDetailsBinding.flFullPostal.setVisibility(View.VISIBLE);
        } else {
            mActivityDetailsBinding.appbar.setVisibility(View.VISIBLE);
            mActivityDetailsBinding.flFullPostal.setVisibility(View.GONE);
        }
        fullPhotoOpen = open;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fullPhotoOpen) {
            outState.putBoolean(BUNDLE_FULL_PHOTO, fullPhotoOpen);
        }
        super.onSaveInstanceState(outState);
    }

    private void closeOnError() {
        //finish();
        Toast.makeText(this, R.string.message_movie_detail_error, Toast.LENGTH_SHORT).show();
    }


    @SuppressWarnings("unused")
    @BindingAdapter("android:text")
    public static void setText(TextView view, Double value) {
        view.setText(String.valueOf(value));
    }
}
