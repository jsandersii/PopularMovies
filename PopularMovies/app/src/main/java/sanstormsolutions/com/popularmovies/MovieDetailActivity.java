package sanstormsolutions.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    // vars
    private ImageView mPoster = null;
    private TextView mTrailerHeader = null;
    private TextView mOverView = null;
    private TextView mReleaseDate = null;
    private TextView mRating = null;
    private TextView mReview = null;

    private String strPoster = null;
    private String strTitle = null;
    private String strOverView = null;
    private String strReleaseDate = null;
    private String strRating;
    private String strMovieId = null;

    private ArrayList<TrailerModel> trailers = new ArrayList<>();
    private ArrayList<ReviewModel> reviews = new ArrayList<>();

    private LinearLayout mTrailerContainer = null;

    private RecyclerView mTrailerRecyclerView = null;
    private LinearLayoutManager mTrailerLayoutManager = null;
    private TrailerAdapter mTrailerAdapter;

    private Context context;

    private boolean isFavorite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        context = this;

        Intent intent = getIntent();
        strTitle = intent.getStringExtra("title");
        strOverView = intent.getStringExtra("overView");
        strReleaseDate = intent.getStringExtra("releaseDate");
        strPoster = intent.getStringExtra("posterUrl");
        strRating = intent.getStringExtra("userRating");
        strMovieId = intent.getStringExtra("movieId");

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(strTitle);
        ab.setDisplayHomeAsUpEnabled(true);
        setupViews();
        checkFavorites();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", strTitle);
        outState.putString("overView", strOverView);
        outState.putString("releaseDate", strReleaseDate);
        outState.putString("posterUrl", strPoster);
        outState.putString("userRating", strRating);
        outState.putString("movieId", strMovieId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        strTitle = savedInstanceState.getString("title");
        strOverView = savedInstanceState.getString("overView");
        strReleaseDate = savedInstanceState.getString("releaseDate");
        strPoster = savedInstanceState.getString("posterUrl");
        strRating = savedInstanceState.getString("userRating");
        strMovieId = savedInstanceState.getString("movieId");

        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater;
        inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_movie_detail, menu);

        if (checkFavorites()){
            menu.findItem(R.id.actionbar_add_favorite).setIcon(R.drawable.ic_favorite_white_36dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // What happens when you click on the toolbar menu items.
        switch (id) {
            case R.id.actionbar_add_favorite:
                if (!isFavorite) {
                    item.setIcon(R.drawable.ic_favorite_white_36dp);
                    addFavorite(strTitle);
                }else {
                    removeFavorite(strTitle);
                    item.setIcon(R.drawable.ic_favorite_border_white_36dp);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        mPoster = (ImageView) findViewById(R.id.content_movie_detail_imgvPoster);
        Picasso.with(this).load(strPoster).error(R.mipmap.ic_launcher).into(mPoster);

        mOverView = (TextView) findViewById(R.id.content_movie_detail_txtvMovieOverview);
        mOverView.setText(strOverView);

        mReleaseDate = (TextView) findViewById(R.id.content_movie_detail_txtvReleaseDate);
        mReleaseDate.setText(strReleaseDate);

        mRating = (TextView) findViewById(R.id.content_movie_detail_txtvUserRating);
        mRating.setText(strRating);

        mReview = (TextView) findViewById(R.id.content_movie_detail_txtvReviewNumber);

        mTrailerContainer = (LinearLayout) findViewById(R.id.content_movie_detail_llTrailers);
        mTrailerHeader = (TextView) findViewById(R.id.content_movie_detail_txtvTrailerHeader);

        mTrailerAdapter = new TrailerAdapter(context, R.layout.listitem_movie_trailer, trailers);//pass in params

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.content_movie_detail_rvVideos);
        mTrailerLayoutManager = new LinearLayoutManager(this);
        mTrailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setFocusable(false);
        mTrailerRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*mReviewRecyclerView = (RecyclerView) findViewById(R.id.activity_movie_detail_rvReviews);
        mReviewLayoutManager = new LinearLayoutManager(this);
        mReviewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewRecyclerView.setItemAnimator(new DefaultItemAnimator());*/

        //Check to see if there are any Movie Trailers for the selected Movie
        new getMovieTrailers().execute(strMovieId);

        //Check for Movie Reviews for the selected Movie
        new getMovieReviews().execute(strMovieId);


    }

    public class getMovieTrailers extends AsyncTask<String, Void, ArrayList<TrailerModel>> {
        private final String LOG_TAG = getMovieTrailers.class.getSimpleName();

        @Override
        protected ArrayList<TrailerModel> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String movieID = params[0];
            String JSONTrailer = null;
            ArrayList<TrailerModel> aryResults = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String SORT_PARAM = "id";
                final String API_PARAM = "api_key";
                final String API_KEY = MovieDetailActivity.this.getString(R.string.poster_movies_API_KEY);

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(movieID)
                        .appendPath("videos")
                        .appendQueryParameter(API_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                // Check the Server Responses
                /*String strReponseMsg = urlConnection.getResponseMessage();
                int iResponseCode = urlConnection.getResponseCode();*/

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JSONTrailer = buffer.toString();

                Log.i(LOG_TAG, "API OUTPUT: " + JSONTrailer);

                // Parse the data into a MovieModel object
                JSONObject jsonObject = new JSONObject(JSONTrailer);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    TrailerModel trailerData;

                    String strID = object.getString("id");
                    String strKey = object.getString("key");
                    String strName = object.getString("name");
                    String strSite = object.getString("site");
                    String strSize = object.getString("size");
                    String strType = object.getString("type");

                    trailerData = new TrailerModel(strID, strKey, strName, strSite, strSize, strType);
                    aryResults.add(trailerData);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return aryResults;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailerResults) {
            if (trailerResults != null && trailerResults.size() > 0) {
                trailers = trailerResults;
                mTrailerContainer.setVisibility(View.VISIBLE);
                mTrailerHeader.setVisibility(View.VISIBLE);
                mTrailerAdapter = new TrailerAdapter(context, R.layout.listitem_movie_trailer, trailers);//pass in params
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);
            }


        }
    }

    public class getMovieReviews extends AsyncTask<String, Void, ArrayList<ReviewModel>> {
        private final String LOG_TAG = getMovieReviews.class.getSimpleName();

        @Override
        protected ArrayList<ReviewModel> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieID = params[0];
            String movieReviews = null;
            ArrayList<ReviewModel> aryResults = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
                final String SORT_PARAM = "id";
                final String API_PARAM = "api_key";
                final String API_KEY = MovieDetailActivity.this.getString(R.string.poster_movies_API_KEY);

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(movieID)
                        .appendPath("reviews")
                        .appendQueryParameter(API_PARAM, API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                // Check the Server Responses
                /*String strReponseMsg = urlConnection.getResponseMessage();
                int iResponseCode = urlConnection.getResponseCode();*/

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieReviews = buffer.toString();

                Log.i(LOG_TAG, "API OUTPUT: " + movieReviews);

                // Parse the data into a MovieModel object
                JSONObject jsonObject = new JSONObject(movieReviews);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    ReviewModel reviewData;

                    String strID = object.getString("id");
                    String strAuthor = object.getString("author");
                    String strContent = object.getString("content");


                    reviewData = new ReviewModel(strID, strAuthor, strContent);
                    aryResults.add(reviewData);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return aryResults;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewModel> reviewResults) {
            if (reviewResults != null && reviewResults.size() > 0) {
                reviews = reviewResults;
                // Grab the Total number of Reviews
                Integer numReviews = reviews.size();
                mReview.setText(numReviews.toString());


                /*mReviewAdapter = new ReviewAdapter(context, R.layout.listitem_movie_reviews, reviews);//pass in params
                mReviewRecyclerView.setAdapter(mReviewAdapter);*/

            }


        }

    }

    private boolean checkFavorites() {
        SharedPreferences favoriteMovies = getSharedPreferences("movieDetails", 0);
        if (favoriteMovies.contains(strTitle)) {
            isFavorite = true;
        }
        return isFavorite;
    }

    private void addFavorite(String movieTitle) {
        SharedPreferences favoriteMovies = getSharedPreferences("movieDetails", 0);
        favoriteMovies.edit().putString(movieTitle, strMovieId).commit();
        /*Set<String> movieSet = new HashSet<>();
        movieSet.add(strPoster);
        movieSet.add(strTitle);
        movieSet.add(strOverView);
        movieSet.add(strRating);
        movieSet.add(strReleaseDate);
        movieSet.add(strMovieId);
        favoriteMovies.edit().putStringSet(movieTitle, movieSet).commit();*/
        isFavorite = true;

    }

    private void removeFavorite(String movieTitle) {
        SharedPreferences favoriteMovies = getSharedPreferences("movieDetails", 0);
        favoriteMovies.edit().remove(movieTitle).commit();
        isFavorite = false;
    }
}
