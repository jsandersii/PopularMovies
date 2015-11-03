package sanstormsolutions.com.popularmovies;

/**
 * Created by jsandersii on 11/2/15.
 */
public class MovieModel {
    private String posterPath = null;
    private String movieTitle = null;
    private String movieOverview = null;
    private double user_rating;
    private String release_date = null;

    public MovieModel(String posterPath, String movieTitle, String movieOverview, double user_rating, String release_date){

        this.posterPath = posterPath;
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.user_rating = user_rating;
        this.release_date = release_date;
    }


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
}
