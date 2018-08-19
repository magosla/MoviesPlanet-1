package com.naijaplanet.magosla.android.moviesplanet.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.naijaplanet.magosla.android.moviesplanet.Config;
import com.naijaplanet.magosla.android.moviesplanet.R;
import com.naijaplanet.magosla.android.moviesplanet.data.MoviesResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MovieDbUtil {
    private static OkHttpClient mHttpClient;

    /**
     * Get the photo url
     *
     * @param photoPath the photo path
     * @param width the width of the photo
     * @return the as {@link String} the photo url or an empty {@link String} if photoPath is empty
     */
    public static String getPhotoUrl(String photoPath, String width){
        if(TextUtils.isEmpty(photoPath)){
            return "";
        }
        Uri uri = Uri.parse(Config.MOVIEDB_IMAGE_BASE_URL).buildUpon()
                .appendPath(width).appendPath(photoPath.replace("/","")).build();

        return uri.toString();
    }

    /**
     * Get the OkHttpClient
     *
     * @return an instance of OkHttpClient
     */
    private static OkHttpClient getHttpClient() {
        // should be a singleton
        //OkHttpClient client = new OkHttpClient();
        if (mHttpClient == null) {
            mHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
        }
        return mHttpClient;
    }

    /**
     * The URL to use for the request
     *
     * @param page   the page number
     * @param filter the filter for the results
     * @return the url string
     */
    private static String getUrl(int page, String filter) {
        HttpUrl url = HttpUrl.parse(Config.MOVIEDB_BASE_URL);
        if(url != null) //noinspection DanglingJavadoc
        {
            HttpUrl.Builder urlBuilder = url.newBuilder();
            if (filter != null) {
                urlBuilder.addPathSegment(filter);
            }
            urlBuilder.addQueryParameter("page", String.valueOf(((page < 1) ? 1 : page)));

            /**
             * The Movie database API key defined in {@link Config.MOVIEDB_API_KEY}
             */
            urlBuilder.addQueryParameter("api_key", Config.MOVIEDB_API_KEY);
            return urlBuilder.build().toString();
        }
        return "";

    }

    /**
     * Get the Movies record with the provided criteria
     *
     * @param page     the page number
     * @param filter   criteria to filter the result
     * @param callback the callback if an error was encountered
     * @return an instance of {{@link MoviesResult}}
     */
    public static MoviesResult getRecord(final Context context, int page, String filter, final Callback callback) {

        Request request = new Request.Builder()
                .url(getUrl(page, filter))
                .build();

        MoviesResult moviesResult = null;

        try {
            Response response = getHttpClient().newCall(request).execute();
            try {
                ResponseBody body = response.body();
                //noinspection StatementWithEmptyBody
                if (body != null) {
                    String responseData = body.string();
                    JSONObject moviesObject = new JSONObject(responseData);

                    if (moviesObject.has("results")) {
                        moviesResult = MoviesResult.fromJson(moviesObject);
                        // keep track of the filter used (if any) for this result
                        moviesResult.setResultFilter(filter);
                    }
                    // TODO what to do when the response has no results field
                } else {
                    // TODO what to do when the response body is empty
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // TODO action to perform if there is a JSONException
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO action to perform if there is an IOException error
            new InternetCheck(new InternetCheck.Consumer() {
                @Override
                public void accept(Boolean internet) {
                    if(!internet){
                        if(callback!= null) {
                            callback.error(context.getString(R.string.message_no_network_connection));
                        }
                    }
                }
            });
        }
        return moviesResult;
    }

    /**
     * Callback to provide further information if an error was encountered
     */
    public interface Callback {
        // TODO complete the callback interface methods
        void error(String msg);
    }
}
