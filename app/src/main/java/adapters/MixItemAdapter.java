package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brainbeats.R;
import java.util.List;
import model.MixItem;
import utils.Constants;

/**
 * Created by douglas on 5/16/2016.
 */
public class MixItemAdapter extends RecyclerView.Adapter<MixItemAdapter.ViewHolder> {

    Context mAdapterContext;
    List<MixItem> mMixItems;

    public MixItemAdapter(Context context, List<MixItem> data) {
        mAdapterContext = context;
        mMixItems = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mMixerItemTitle;
        ImageView mAddItemIcon;
        ImageView mIncreaseBeatLevelIcon;
        ImageView mSubtractBeatLevelIcon;
        ImageView mRemoveBeatItemIcon;
        ProgressBar mProgressBar;

        public ViewHolder(View v) {
            super(v);
            mMixerItemTitle = (TextView) v.findViewById(R.id.mix_item_title);
            mAddItemIcon = (ImageView) v.findViewById(R.id.plus_icon);
            mIncreaseBeatLevelIcon = (ImageView) v.findViewById(R.id.increase_icon);
            mSubtractBeatLevelIcon = (ImageView) v.findViewById(R.id.minus_icon);
            mRemoveBeatItemIcon = (ImageView) v.findViewById(R.id.clear_icon);
            mProgressBar = (ProgressBar) v.findViewById(R.id.beat_level_bar);

            mAddItemIcon.setOnClickListener(this);
            mIncreaseBeatLevelIcon.setOnClickListener(this);
            mSubtractBeatLevelIcon.setOnClickListener(this);
            mRemoveBeatItemIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.plus_icon:
                    Constants.buildImageListDialogue(mAdapterContext,mAdapterContext.getResources().getString(R.string.add_sound_item_to_current_beat));
                    break;
                case R.id.increase_icon:
                    mMixItems.get(getAdapterPosition()).setMixItemLevel(mMixItems.get(getAdapterPosition()).getMixItemLevel() + Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    mProgressBar.incrementProgressBy(Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    break;
                case R.id.minus_icon:
                    mMixItems.get(getAdapterPosition()).setMixItemLevel(mProgressBar.getProgress() - Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    mProgressBar.setProgress(mProgressBar.getProgress() - Constants.BEAT_LEVEL_INCREASE_DIFFERENCE);
                    break;
                case R.id.clear_icon:
                    removeItem(getAdapterPosition());
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mAdapterContext).inflate(R.layout.beat_mixer_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMixerItemTitle.setText(mMixItems.get(position).getMixItemTitle());
        holder.mProgressBar.setProgress(mMixItems.get(position).getMixItemLevel());

        if(position == mMixItems.size() - 1) { // set the last row to be an add new item
            holder.mRemoveBeatItemIcon.setVisibility(View.INVISIBLE);
            holder.mSubtractBeatLevelIcon.setVisibility(View.INVISIBLE);
            holder.mIncreaseBeatLevelIcon.setVisibility(View.GONE);
            holder.mAddItemIcon.setVisibility(View.VISIBLE);
            holder.mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMixItems.size();
    }

    public void removeItem(int position) {
        mMixItems.remove(position);
        notifyItemRemoved(position);
    }
}