package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import java.util.List;
import model.Beat;

/**
 * Created by douglas on 5/13/2016.
 */
public class MixerAdapter extends RecyclerView.Adapter<MixerAdapter.ViewHolder> {

    Context mAdapterContext;
    List<Beat> mBeats;

    public MixerAdapter(Context context, List<Beat> data) {
        mAdapterContext = context;
        mBeats = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mContainer;
        TextView mAlbumTitle;


        public ViewHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.mixer_item_container);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_title);

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MixerActivity) mAdapterContext).loadMixerDetailFragment();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.mixer_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAlbumTitle.setText(mBeats.get(position).getBeatTitle());
    }

    @Override
    public int getItemCount() {
        return mBeats.size();
    }
}