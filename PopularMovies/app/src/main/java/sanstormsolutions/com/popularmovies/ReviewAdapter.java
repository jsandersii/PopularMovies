package sanstormsolutions.com.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jsandersii on 1/11/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    //Vars
    private int rowLayout;
    private Context context;
    private ArrayList<ReviewModel> reviews;

    // Default Constructor

    public ReviewAdapter (Context context, int rowLayout, ArrayList<ReviewModel> reviews){
        this.context = context;
        this.rowLayout = rowLayout;
        this.reviews = reviews;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        final ReviewModel reviewData = reviews.get(position);

        holder.mAuthor.setText(reviewData.getAuthor());
        holder.mContent.setText(reviewData.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Vars
        private TextView mAuthor = null;
        private TextView mContent = null;

        public ViewHolder(View itemView) {
            super(itemView);
            // Find Views
            mAuthor = (TextView) itemView.findViewById(R.id.listitem_movie_reviews_txtvAuthor);
            mContent = (TextView) itemView.findViewById(R.id.listitem_movie_reviews_txtvReview);
        }
    }
}
