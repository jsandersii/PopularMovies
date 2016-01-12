package sanstormsolutions.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jsandersii on 1/10/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    // Vars
    private int rowLayout;
    private Context context;
    private ArrayList<TrailerModel> trailers;

    // Default Constructor
    public TrailerAdapter (Context context, int rowLayout, ArrayList<TrailerModel> trailers){
        this.context = context;
        this.rowLayout = rowLayout;
        this.trailers = trailers;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        final TrailerModel trailerData = trailers.get(position);

        holder.mTitle.setText(trailerData.getName());

        holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Play Trailer", Toast.LENGTH_SHORT).show();
                String strVideoUri;
                Intent viewIntent;
                String strYouTube;

                strYouTube = "http://www.youtube.com/watch?v=";
                strVideoUri = trailerData.getKey();
                viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strYouTube+strVideoUri));
                context.startActivity(viewIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //Vars
        public ImageView mPlayButton = null;
        public TextView mTitle = null;

        public ViewHolder(View itemView) {
            super(itemView);
            // Find Vars
            mPlayButton = (ImageView) itemView.findViewById(R.id.listitem_movie_trailer_imgvPlay);
            mTitle = (TextView) itemView.findViewById(R.id.listitem_movie_trailer_txtvTitle);
        }
    }
}
