package adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.SocialActivity;
import com.squareup.picasso.Picasso;

import data.BrainBeatsContract;
import utils.Constants;

/**
 * Created by douglas on 5/13/2016.
 */
public class SocialAdapter extends RecyclerViewCursorAdapter<SocialAdapter.ViewHolder> {

    Context mAdapterContext;

    public SocialAdapter(Context context, Cursor cursor) {
        super(context,cursor);
        mAdapterContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME));
        if(title != null)
            viewHolder.mUsername.setText(title);

        viewHolder.mArtistContainerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SocialActivity) mAdapterContext).switchToUserProfileFragment();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mArtistContainerCard;
        TextView mUsername;

        public ViewHolder(View view){
            super(view);
            mUsername = (TextView) view.findViewById(R.id.user_name);
            mArtistContainerCard = (CardView) view.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
