package sanstormsolutions.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jsandersii on 11/1/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private int rowlayout;
    private Context context;
    private ArrayList<MovieModel> movies;
    private View.OnClickListener mOnClickListener = null;

    public MovieAdapter(Context context, int rowlayout, ArrayList<MovieModel> movies){
        this.rowlayout = rowlayout;
        this.context = context;
        this.movies = movies;

    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowlayout, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, final int position){
        final MovieModel movieData = movies.get(position);
        String url = getImageUrl(movieData.getPosterPath());
        Picasso.with(context).load(url).error(R.mipmap.ic_launcher).into(holder.mPoster);
        holder.mPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = movieData.getMovieTitle();
                String overView = movieData.getMovieOverview();
                String releaseDate = movieData.getRelease_date();
                String posterUrl = getImageUrl(movieData.getPosterPath());
                Double userRating = movieData.getUser_rating();
                String user_rating = userRating.toString();
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("overView", overView);
                intent.putExtra("releaseDate", releaseDate);
                intent.putExtra("posterUrl", posterUrl);
                intent.putExtra("userRating", user_rating);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount(){
        return movies == null ? 0 : movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Vars

        public ImageView mPoster = null;

        public ViewHolder(View itemView){
            super(itemView);

            // Find Views
            mPoster = (ImageView) itemView.findViewById(R.id.listitem_poster_fragment_imgvPoster);
        }

    }

    public String getImageUrl(String posterPath){
        String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500";
        return BASE_IMAGE_URL+posterPath;
    }

}
