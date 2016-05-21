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

import java.util.List;

import model.Beat;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    Context mAdapterContext;
    List<Beat> mBeats;

    public LibraryAdapter(Context context, List<Beat> data) {
        mAdapterContext = context;
        mBeats = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumArtCover;
        TextView mAlbumTitle;

        public ViewHolder(View v) {
            super(v);
            mAlbumArtCover = (ImageView) v.findViewById(R.id.album_cover_art);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_title);

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
        holder.mAlbumTitle.setText(mBeats.get(position).getBeatTitle());
        holder.mAlbumArtCover.setImageBitmap(mBeats.get(position).getBeatAlbumCoverArt());
    }

    @Override
    public int getItemCount() {
        return mBeats.size();
    }
}
