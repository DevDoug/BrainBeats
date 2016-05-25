package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import entity.Track;
import model.Beat;


/**
 * Created by Douglas on 4/13/2016.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    Context mAdapterContext;
    List<Track> mTracks;

    public TrackAdapter(Context context, List<Track> data) {
        mAdapterContext = context;
        mTracks = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumArtCover;
        TextView mTrackTitle;

        public ViewHolder(View v) {
            super(v);
            mAlbumArtCover = (ImageView) v.findViewById(R.id.album_cover_art);
            mTrackTitle = (TextView) v.findViewById(R.id.album_title);

            mAlbumArtCover.setOnClickListener(new View.OnClickListener() { //Beat selected load detail screen
                @Override
                public void onClick(View v) {
                    ((MainActivity) mAdapterContext).loadBeatDetailFragment();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTrackTitle.setText(mTracks.get(position).getTitle());
        Picasso.with(mAdapterContext).load(mTracks.get(position).getArtworkURL()).into(holder.mAlbumArtCover);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}
