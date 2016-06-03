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

/**
 * Created by douglas on 5/31/2016.
 */
public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {

    Context mAdapterContext;
    List<Track> mTracks;

    public SearchMusicAdapter(Context context, List<Track> data) {
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

            mAlbumArtCover.setOnClickListener(new View.OnClickListener() { //Mix selected load detail screen
                @Override
                public void onClick(View v) {
                    ((MainActivity) mAdapterContext).loadRefinedBeatList(mTracks.get(getAdapterPosition()).getTitle());
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
        holder.mAlbumArtCover.setImageDrawable(mAdapterContext.getDrawable(R.drawable.placeholder));

        //Picasso.with(mAdapterContext).load(mCollection.get(position).getArtworkURL()).into(holder.mAlbumArtCover);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }
}
