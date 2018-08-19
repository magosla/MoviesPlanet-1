package com.naijaplanet.magosla.android.moviesplanet.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


@SuppressWarnings("ALL")
public class Movie implements Parcelable {

    // This is to de-serialize the object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private String title;
    private String backdropPath;
    private String posterPath;
    private Double popularity;
    private int voteCount;
    private Double voteAverage;
    private boolean video;
    private boolean adult;
    private int[] genreIds;

    public Movie() {

    }

    @SuppressLint("ParcelClassLoader")
    private Movie(Parcel parcel) {
        //this();

        id = parcel.readInt();
        overview = parcel.readString();
        releaseDate = parcel.readString();
        originalTitle = parcel.readString();
        title = parcel.readString();
        backdropPath = parcel.readString();
        posterPath = parcel.readString();
        popularity = parcel.readDouble();
        voteCount = parcel.readInt();
        voteAverage = parcel.readDouble();
        video = (Boolean) parcel.readValue(null);
        adult = (Boolean) parcel.readValue(null);
        // TODO keep close look at this if it is well implemented
        //genreIds = (int[]) parcel.readValue(null);
//        parcel.readIntArray(genreIds);
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(boolean video) {
        this.adult = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeValue(video);
        dest.writeValue(adult);
        dest.writeIntArray(genreIds);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }
}
