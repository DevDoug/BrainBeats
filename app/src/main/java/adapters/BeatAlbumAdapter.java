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
 * Created by douglas on 4/29/2016.
 */
public class BeatAlbumAdapter extends RecyclerView.Adapter<BeatAlbumAdapter.ViewHolder>  {

    Context mAdapterContext;
    List<Mix> mMices;

    public BeatAlbumAdapter(Context context, List<Mix> data) {
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
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_item_album_list_item, parent, false);
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
