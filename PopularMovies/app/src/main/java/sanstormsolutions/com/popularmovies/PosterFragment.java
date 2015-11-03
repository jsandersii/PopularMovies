package sanstormsolutions.com.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class PosterFragment extends Fragment {
    private static final String TAG = PosterFragment.class.getSimpleName() + ".TAG";
    // Vars
    private RecyclerView mRecyclerView = null;
    private MovieAdapter mMovieAdapter = null;
    private Context mCtx;
    private ArrayList<MovieModel> movies  = new ArrayList<>();
    private String mSortString = null;

    public PosterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        mCtx = getActivity();
        mSortString = "popularity.desc";

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_poster_rvMainRV);
        GridLayoutManager glm;
        glm = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Run Data Fetch
        new getMoviePosters().execute(mSortString);



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.posterfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            mSortString = "popularity.desc";
            // Run Data Fetch
            new getMoviePosters().execute(mSortString);
            mMovieAdapter.notifyDataSetChanged();
            return true;
        }else if(id == R.id.action_highest){
            mSortString = "vote_average.desc";
            // Run Data Fetch
            new getMoviePosters().execute(mSortString);
            mMovieAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class getMoviePosters extends AsyncTask<String, Void, ArrayList<MovieModel>> {
        private final String LOG_TAG = getMoviePosters.class.getSimpleName();
        public ArrayList<MovieModel> mMovieResults = new ArrayList<>();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(String... params) {

            if (params.length == 0){
                return null;
            }
            String movieSort = params[0];
            String popularMoviesJson = null;
            ArrayList<MovieModel> aryResults = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                final String API_KEY = mCtx.getString(R.string.poster_movies_API_KEY);

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, movieSort)
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
                popularMoviesJson = buffer.toString();

                /*Log.i(LOG_TAG, "API OUTPUT: " + popularMoviesJson);*/

                // Parse the data into a MovieModel object
                JSONObject jsonObject = new JSONObject(popularMoviesJson);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++ ){
                    JSONObject object = jsonArray.getJSONObject(i);

                    MovieModel movieData;

                    String movieTitle = object.getString("title");
                    String posterPath = object.getString("poster_path");
                    String movieOverview = object.getString("overview");
                    String release_date = object.getString("release_date");
                    double vote_average = object.getDouble("vote_average");

                    movieData = new MovieModel(posterPath,movieTitle,movieOverview,vote_average,release_date);
                    aryResults.add(movieData);
                }

                Log.i(LOG_TAG, "Array Results: " + aryResults);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally

            {
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
        protected void onPostExecute(ArrayList<MovieModel> movieResults){
           if (movieResults != null) {
               movies = movieResults;
               mMovieAdapter = new MovieAdapter(mCtx, R.layout.listitem_main_activity, movies);//pass in params
               mRecyclerView.setAdapter(mMovieAdapter);
           }


        }





    }


}
