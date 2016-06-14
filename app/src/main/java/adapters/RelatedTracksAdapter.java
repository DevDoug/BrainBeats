package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import java.util.List;

import entity.Collection;
import entity.Track;
import fragments.DashboardDetailFragment;

/**
 * Created by douglas on 4/29/2016.
 */
public class RelatedTracksAdapter extends RecyclerView.Adapter<RelatedTracksAdapter.ViewHolder>  {

    Context mAdapterContext;
    List<Collection> mCollection;
    OnRelatedTrackUpdateListener mListener;

    public interface OnRelatedTrackUpdateListener {
        void relatedTrackUpdated(Track track);
    }

    public RelatedTracksAdapter(Context context, List<Collection> data, OnRelatedTrackUpdateListener listener) {
        mAdapterContext = context;
        mCollection = data;
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbumTitle;

        public ViewHolder(View v) {
            super(v);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Track updatedTrack = new Track();
                    updatedTrack.setID(mCollection.get(getAdapterPosition()).getId());
                    updatedTrack.setTitle(mCollection.get(getAdapterPosition()).getTitle());
                    updatedTrack.setArtworkURL(mCollection.get(getAdapterPosition()).getArtworkUrl());
                    updatedTrack.setStreamURL(mCollection.get(getAdapterPosition()).getUri());
                    updatedTrack.setDuration(mCollection.get(getAdapterPosition()).getDuration());
                    mListener.relatedTrackUpdated(updatedTrack);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_item_album_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAlbumTitle.setText(mCollection.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mCollection.size();
    }
}
