package sanstormsolutions.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jsandersii on 11/2/15.
 */
public class MovieModel implements Parcelable{
    private String posterPath = null;
    private String movieTitle = null;
    private String movieOverview = null;
    private double user_rating;
    private String release_date = null;
    private String movieId  = null;

    public MovieModel(String posterPath, String movieTitle, String movieOverview, double user_rating, String release_date, String movieId){

        this.posterPath = posterPath;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.user_rating = user_rating;
        this.release_date = release_date;
        this.movieId = movieId;
    }


    protected MovieModel(Parcel in) {
        posterPath = in.readString();
        movieTitle = in.readString();
        movieOverview = in.readString();
        user_rating = in.readDouble();
        release_date = in.readString();
        movieId = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public double getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(double user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeDouble(user_rating);
        dest.writeString(release_date);
        dest.writeString(movieId);
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
