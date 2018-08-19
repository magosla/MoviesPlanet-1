package com.naijaplanet.magosla.android.moviesplanet.data;

import com.naijaplanet.magosla.android.moviesplanet.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MoviesResult {
    private String resultFilter;
    private List<Movie> movies;
    private int page;
    private int totalPages;
    private int totalResults;

    public MoviesResult(){
        movies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public String getResultFilter() {
        return resultFilter;
    }

    public void setResultFilter(String resultFilter) {
        this.resultFilter = resultFilter;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public static MoviesResult fromJson(JSONObject data){
        MoviesResult moviesResult = new MoviesResult();

            try {
                if(data.has("page")) {
                    moviesResult.setPage(data.getInt("page"));
                }
                if (data.has("total_results")){
                    moviesResult.setTotalResults(data.getInt("total_results"));
                }
                if (data.has("total_pages")){
                    moviesResult.setTotalPages(data.getInt("total_pages"));
                }
                if (data.has("results")){
                    JSONArray moviesObject = data.getJSONArray("results");
                    Movie[] movies = new Movie[moviesObject.length()];
                    for (int i = 0; i < moviesObject.length(); i++){
                        JSONObject movieObject = moviesObject.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setOverview(movieObject.getString("overview"));
                        movie.setId(movieObject.getInt("id"));
                        movie.setReleaseDate(movieObject.getString("release_date"));
                        movie.setOriginalTitle(movieObject.getString("original_title"));
                        movie.setTitle(movieObject.getString("title"));
                        movie.setBackdropPath(movieObject.getString("backdrop_path"));
                        movie.setPopularity(movieObject.getDouble("popularity"));
                        movie.setVoteCount(movieObject.getInt("vote_count"));
                        movie.setVideo(movieObject.getBoolean("video"));
                        movie.setVoteAverage(movieObject.getDouble("vote_average"));
                        movie.setAdult(movieObject.getBoolean("adult"));
                        movie.setPosterPath(movieObject.getString("poster_path"));

                        // TODO IMPLEMENT genre_ids LATER
                        //movie.setGenreIds([]);

                        movies[i] = movie;
                    }
                    moviesResult.setMovies(Arrays.asList(movies));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return moviesResult;
    }
}
