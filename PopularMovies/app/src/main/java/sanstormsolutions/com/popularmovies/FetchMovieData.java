package sanstormsolutions.com.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;

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
 * Created by jsandersii on 11/1/15.
 */
public class FetchMovieData {

    public static class getMoviePosters extends AsyncTask<Void, Void, ArrayList<MovieModel>> {
        public final static String LOG_TAG = FetchMovieData.class.getSimpleName();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(Void... params) {
            String movieSort = "popularity.desc";
            String popularMoviesJson = null;
            ArrayList<MovieModel> aryResults = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                final String API_KEY = "91d4dcdc362990be81b6f1f669f460ef";

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
                    String movieId = object.getString("id");
                    double vote_average = object.getDouble("vote_average");

                    movieData = new MovieModel(posterPath,movieTitle,movieOverview,vote_average,release_date,movieId);
                    aryResults.add(movieData);
                }

                /*Log.i(LOG_TAG, "Array Results: " + aryResults);*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return aryResults;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieResults){
            super.onPostExecute(movieResults);


        }





    }
}
