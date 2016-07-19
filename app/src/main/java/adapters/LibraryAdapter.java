package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import entity.Track;
import model.Mix;
import utils.MixManager;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Track> tracks;

    public LibraryAdapter(Context context,  ArrayList<Track> data) {
        mAdapterContext = context;
        tracks = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbumTitle;
        ImageView mMixTrack;

        public ViewHolder(View v) {
            super(v);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_title);
            mMixTrack = (ImageView) v.findViewById(R.id.mix_beat);

            mMixTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MixManager.getInstance(mAdapterContext).createMixFromExistingTrack(tracks.get(getAdapterPosition()));
                    Toast.makeText(mAdapterContext,"Mixing Track",Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAlbumTitle.setText(tracks.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
