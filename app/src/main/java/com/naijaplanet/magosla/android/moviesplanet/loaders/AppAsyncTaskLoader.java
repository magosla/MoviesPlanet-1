package com.naijaplanet.magosla.android.moviesplanet.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

abstract class AppAsyncTaskLoader<D> extends AsyncTaskLoader<D> {
    private final Bundle mArgs;
    private final AppLoader.LoaderCallback<D> mLoaderCallback;

    public Bundle getArgs(){
        return mArgs;
    }

    public AppLoader.LoaderCallback<D> getLoaderCallback(){
        return mLoaderCallback;
    }

    AppAsyncTaskLoader(Context context, AppLoader.LoaderCallback<D> loaderCallback, @Nullable final Bundle args) {
        super(context);
        mArgs = args;
        mLoaderCallback = loaderCallback;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mArgs == null) {
            return;
        }
        if (takeContentChanged()) {
            forceLoad();
            mLoaderCallback.loadingItems();
        }
    }


}
