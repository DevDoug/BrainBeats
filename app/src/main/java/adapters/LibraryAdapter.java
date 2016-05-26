package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;

import java.util.List;

import model.Mix;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    Context mAdapterContext;
    List<Mix> mMices;

    public LibraryAdapter(Context context, List<Mix> data) {
        mAdapterContext = context;
        mMices = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbumTitle;

        public ViewHolder(View v) {
            super(v);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_title);
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
        holder.mAlbumTitle.setText(mMices.get(position).getBeatTitle());
    }

    @Override
    public int getItemCount() {
        return mMices.size();
    }
}
