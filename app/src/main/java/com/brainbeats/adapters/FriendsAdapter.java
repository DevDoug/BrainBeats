package com.brainbeats.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.R;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
 * Created by douglas on 5/13/2016.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<BrainBeatsUser> mUserList;

    public FriendsAdapter(Context context, ArrayList<BrainBeatsUser> userList) {
        this.mAdapterContext = context;
        this.mUserList = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mArtistContainerCard;
        TextView mUsername;
        TextView mDescription;
        ImageView mArtistThumbnail;

        public ViewHolder(View view){
            super(view);
            mUsername = view.findViewById(R.id.artist_name);
            mDescription = view.findViewById(R.id.artist_description);
            mArtistThumbnail = view.findViewById(R.id.artist_thumbnail);
            mArtistContainerCard = view.findViewById(R.id.artist_name_container);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BrainBeatsUser user = mUserList.get(position);
        holder.mUsername.setText(user.getUserName());

        holder.mArtistContainerCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Constants.buildActionDialog(mAdapterContext, "Remove Friend", "Do you want to remove this artist", "confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        FirebaseDatabase Database = FirebaseDatabase.getInstance();
                        Query reference = Database
                                .getReference("friends/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .orderByChild("userId")
                                .equalTo(user.getUserId());


                        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot != null)
                                    dataSnapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
