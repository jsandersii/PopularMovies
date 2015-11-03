package sanstormsolutions.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    // vars
    private ImageView mPoster = null;
    private TextView mTitle = null;
    private TextView mOverView = null;
    private TextView mReleaseDate = null;
    private TextView mRating = null;

    private String strPoster = null;
    private String strTitle = null;
    private String strOverView = null;
    private String strReleaseDate = null;
    private String strRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        strTitle = intent.getStringExtra("title");
        strOverView = intent.getStringExtra("overView");
        strReleaseDate = intent.getStringExtra("releaseDate");
        strPoster = intent.getStringExtra("posterUrl");
        strRating = intent.getStringExtra("userRating");

        setupViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("title", strTitle);
        outState.putString("overView", strOverView);
        outState.putString("releaseDate", strReleaseDate);
        outState.putString("posterUrl", strPoster);
        outState.putString("userRating", strRating);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        strTitle = savedInstanceState.getString("title");
        strOverView = savedInstanceState.getString("overView");
        strReleaseDate = savedInstanceState.getString("releaseDate");
        strPoster = savedInstanceState.getString("posterUrl");
        strRating = savedInstanceState.getString("userRating");

        setupViews();
    }

    private void setupViews() {
        mPoster = (ImageView) findViewById(R.id.activity_movie_detail_imgvPoster);
        Picasso.with(this).load(strPoster).error(R.mipmap.ic_launcher).into(mPoster);

        mTitle = (TextView) findViewById(R.id.activity_movie_detail_txtvMovieTitle);
        mTitle.setText(strTitle);

        mOverView = (TextView) findViewById(R.id.activity_movie_detail_txtvMovieOverview);
        mOverView.setText(strOverView);

        mReleaseDate = (TextView) findViewById(R.id.activity_movie_detail_txtvReleaseDate);
        mReleaseDate.setText(strReleaseDate);

        mRating = (TextView) findViewById(R.id.activity_movie_detail_txtvUserRating);
        mRating.setText(strRating);
    }
}
