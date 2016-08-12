package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.brainbeats.R;

import data.BrainBeatsContract;

/**
 * Created by douglas on 7/28/2016.
 */
public class LibraryPlaylistAdapter extends RecyclerViewCursorAdapter<LibraryPlaylistAdapter.ViewHolder> {

    Context mAdapterContext;

    public LibraryPlaylistAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.MixPlaylistEntry.COLUMN_NAME_PLAYLIST_TITLE));
        if(title != null)
            viewHolder.mTitleText.setText(title);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
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
