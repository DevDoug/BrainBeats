package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by douglas on 8/11/2016.
 */
public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    Context mContext;
    Cursor mCursor;


    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);


    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }


    public Cursor getCursor() {
        return mCursor;
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (mCursor == null) {
            throw new IllegalStateException("Cursor is not valid.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Cursor could not move to position " + position);
        }
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if(mCursor != null){
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mCursor.getColumnIndex("_id"));
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }
    /**
     * To use with a Loader, call swapCursor(cursorFromLoader) at onLoadFinished().
     * @param newCursor
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;

    }
}
