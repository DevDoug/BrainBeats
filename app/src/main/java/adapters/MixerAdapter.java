package adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import data.BrainBeatsContract;
import model.Mix;
import utils.Constants;
import web.OfflineSyncManager;

/**
 * Created by douglas on 5/13/2016.
 */
public class MixerAdapter extends RecyclerViewCursorAdapter<MixerAdapter.ViewHolder>{

    Context mAdapterContext;

    public MixerAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE));
        if(title != null)
            viewHolder.mTitleText.setText(title);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.mixer_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleText;

        public ViewHolder(View view){
            super(view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}