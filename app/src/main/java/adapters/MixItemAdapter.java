package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.R;
import java.util.List;
import model.MixerItem;

/**
 * Created by douglas on 5/16/2016.
 */
public class MixItemAdapter extends RecyclerView.Adapter<MixItemAdapter.ViewHolder> {

    Context mAdapterContext;
    List<MixerItem> mMixerItems;

    public MixItemAdapter(Context context, List<MixerItem> data) {
        mAdapterContext = context;
        mMixerItems = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMixerItemTitle;
        ImageView mAddItemIcon;

        public ViewHolder(View v) {
            super(v);
            mMixerItemTitle = (TextView) v.findViewById(R.id.mix_item_title);
            mAddItemIcon = (ImageView) v.findViewById(R.id.plus_icon);

            mAddItemIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAddBeatMixItemDialog();
                }
            });
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
        holder.mMixerItemTitle.setText(mMixerItems.get(position).getMixItemTitle());
        if(position == mMixerItems.size() - 1) // set the last row to be an add new item
            holder.mAddItemIcon.setImageDrawable((Drawable) mAdapterContext.getResources().getDrawable(R.drawable.ic_add_circle_grey));
    }

    @Override
    public int getItemCount() {
        return mMixerItems.size();
    }

    public void createAddBeatMixItemDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mAdapterContext, android.R.style.Theme_Material_Light_Dialog_Alert);
        View dialogView = LayoutInflater.from(mAdapterContext).inflate(R.layout.custom_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(mAdapterContext.getString(R.string.new_beat_title));


        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }
}