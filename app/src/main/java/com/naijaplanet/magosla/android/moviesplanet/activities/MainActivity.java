package com.naijaplanet.magosla.android.moviesplanet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.naijaplanet.magosla.android.moviesplanet.Config;
import com.naijaplanet.magosla.android.moviesplanet.EndlessRecyclerOnScrollListener;
import com.naijaplanet.magosla.android.moviesplanet.R;
import com.naijaplanet.magosla.android.moviesplanet.adapters.MoviesAdapter;
import com.naijaplanet.magosla.android.moviesplanet.data.MoviesLoader;
import com.naijaplanet.magosla.android.moviesplanet.data.MoviesResult;
import com.naijaplanet.magosla.android.moviesplanet.databinding.ActivityMainBinding;
import com.naijaplanet.magosla.android.moviesplanet.fragments.SettingsFragment;
import com.naijaplanet.magosla.android.moviesplanet.models.Movie;
import com.naijaplanet.magosla.android.moviesplanet.models.MoviesRecord;
import com.naijaplanet.magosla.android.moviesplanet.util.GridItemSpacingDecoration;
import com.naijaplanet.magosla.android.moviesplanet.util.GridItemsSpanSpacing;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, MoviesAdapter.OnEventListener{
    private static final String BUNDLE_MOVIE_RECORD = "MOVIES_SAVE_KEY";
    private static final String BUNDLE_FILTER_SETTING_STATE ="filter_setting_state";
    private MoviesRecord mMoviesRecord;
    private MoviesLoader mMoviesLoader;
    private ActivityMainBinding mActivityMainBinding;
    private boolean movieFilterSettingActive;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private Toast mToast;

    private boolean mMoviesLoading; // to keep track of when movie loading is in process


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // used to inspect network request in chrome inspector
        Stetho.initializeWithDefaults(this);
        // register the sharedpreference change listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);


        checkForSavedMoviesInstance(savedInstanceState);

        setupView(savedInstanceState);

        if (mMoviesRecord.isEmpty()) {
            loadMovies(1, null);
        }
    }

    private void setupView(Bundle savedInstanceState) {
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        checkForFilterOpenState(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setSmoothScrollbarEnabled(true);

        final MoviesAdapter moviesAdapter = new MoviesAdapter(this, mMoviesRecord, this);
        mActivityMainBinding.rvMovieList.setLayoutManager(layoutManager);
        mActivityMainBinding.rvMovieList.setAdapter(moviesAdapter);

        GridItemsSpanSpacing gridItemsSpanSpacing = new GridItemsSpanSpacing( mActivityMainBinding.rvMovieList,
                R.dimen.movie_item_width, 0, layoutManager.getOrientation(), true);

        layoutManager.setSpanCount(gridItemsSpanSpacing.getSpan());

        mActivityMainBinding.rvMovieList.addItemDecoration(new GridItemSpacingDecoration(gridItemsSpanSpacing));

        initializeMovieLoader(moviesAdapter);
    }

    /**
     * Check is the movies filter was opened in the Application's previous state
     * @param savedInstanceState the instance {@link Bundle}
     */
    private void checkForFilterOpenState(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_FILTER_SETTING_STATE)){
            movieFilterSettingActive = savedInstanceState.getBoolean(BUNDLE_FILTER_SETTING_STATE);
            if(movieFilterSettingActive){
                movieFilterSettingActive = false; // to allow the settings open since we are toggling the setting
                toggleMovieFilterSetting();
            }
        }
    }

    /**
     * Check is there is {@link MoviesRecord} from the Application's previous state
     * @param savedInstanceState the saved instance {@link Bundle}
     */
    private void checkForSavedMoviesInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_MOVIE_RECORD)) {
            mMoviesRecord = savedInstanceState.getParcelable(BUNDLE_MOVIE_RECORD);
        }

        if(mMoviesRecord != null){
            updateTitle(mMoviesRecord.getMovieDbFilter());
        }else {
            mMoviesRecord = new MoviesRecord();
        }
    }

    private void updateTitle(CharSequence title) {
        setTitle(getString(R.string.title_movies, title)
                .toUpperCase().replace("_"," "));
    }

    @Override
    public void onBackPressed() {
        if(movieFilterSettingActive){
            toggleMovieFilterSetting();
            return;
        }
        super.onBackPressed();
    }

    /**
     * Show or hide the setting to filter movies
     */
    private void toggleMovieFilterSetting(){

        if(movieFilterSettingActive){
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager()
                            .findFragmentById(mActivityMainBinding.flSettingsHolder.getId())
                    ).commit();
            movieFilterSettingActive = false;
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(mActivityMainBinding.flSettingsHolder.getId(),SettingsFragment.create()).commit();
            movieFilterSettingActive = true;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        unInitializeEndlessScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeEndlessScroll();
    }

    /**
     * Initialize the endless scroll facility
     */
    private void initializeEndlessScroll() {
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                Log.d("Loading more called", "status is "+ String.valueOf(mMoviesLoading));
                if (!mMoviesLoading) {
                    loadMovies(mMoviesRecord.getCurrentPage() + 1, mMoviesRecord.getMovieDbFilter());
                }
            }
        };
        mActivityMainBinding.rvMovieList.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    /**
     * Un-initialize the endless scroll
     */
    private void unInitializeEndlessScroll() {
        mActivityMainBinding.rvMovieList.removeOnScrollListener(mEndlessRecyclerOnScrollListener);
        mEndlessRecyclerOnScrollListener = null;
    }

    /**
     * Method to load movies
     * @param page {{int}} page numbers
     * @param filter {{String}} filter for the movies to load
     */
    private void loadMovies(int page, String filter) {
        if (filter == null || filter.isEmpty()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            filter = sharedPreferences.getString(getString(R.string.pref_filter_key), getString(R.string.pref_filter_default));
            updateTitle(filter);

        }
        mMoviesLoader.load(page, filter);
    }

    /**
     * Initialize the movie loader
     * @param moviesAdapter a {@link MoviesAdapter} instance
     */
    private void initializeMovieLoader(final MoviesAdapter moviesAdapter) {
        mMoviesLoader = new MoviesLoader(this, getSupportLoaderManager(), new MoviesLoader.MovieLoaderCallback() {
            @Override
            public void loadingMovies() {
                mMoviesLoading = true;
                mActivityMainBinding.pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadFinished(MoviesResult moviesResult) {
                mMoviesRecord.addMovies(moviesResult);
                if (!moviesResult.getMovies().isEmpty()) {
                    moviesAdapter.notifyDataSetChanged();
                }
                mMoviesLoading = false;

                mActivityMainBinding.pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                mMoviesLoading = false;
                mActivityMainBinding.pbLoading.setVisibility(View.GONE);
                showMessage(errorMessage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                toggleMovieFilterSetting();
                /*
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                 */
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_filter_key))) {
            loadMovies(1, null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(movieFilterSettingActive){
            //noinspection ConstantConditions
            outState.putBoolean(BUNDLE_FILTER_SETTING_STATE, movieFilterSettingActive);
        }
        if (!mMoviesRecord.isEmpty()) {
            outState.putParcelable(BUNDLE_MOVIE_RECORD, mMoviesRecord);
        } else {
            // try to remove movie if any already save since we have an empty one
            outState.remove(BUNDLE_MOVIE_RECORD);
        }

    }

    @Override
    public void onMovieItemClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(Config.EXTRA_MOVIE_KEY, movie);
        startActivity(intent);
    }

    /**
     * Shows a message about the application status, in the event of error
     * @param message the message {@link String} to display
     */
    private void showMessage(String message){
        if(mToast != null ){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}

