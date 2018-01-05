package com.brainbeats.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.R;
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

public class MixerAdapter extends RecyclerView.Adapter<MixerAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Mix> mMixList;

    public MixerAdapter(Context context, ArrayList<Mix> items) {
        mAdapterContext = context;
        mMixList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.mixer_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mix mix = mMixList.get(position);
        holder.mTitleText.setText(mix.getMixTitle());

        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Constants.buildActionDialog(mAdapterContext, "Delete Mix", "Do you want to delete this mix", "confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        FirebaseDatabase Database = FirebaseDatabase.getInstance();
                        Query reference = Database.getReference("mixes/");
                        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() { // delete mix
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Mix mixToDelete = childSnapshot.getValue(Mix.class);

                                    if(mixToDelete.getMixTitle().equals(mix.getMixTitle())) {

                                        Query reference =  Database
                                                .getReference("artist_mix/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .orderByChild("artistId")
                                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() { // delete artist mix
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                    Mix artistMixToDelete = childSnapshot.getValue(Mix.class);

                                                    if(artistMixToDelete.getMixTitle().equals(mix.getMixTitle())) {
                                                        childSnapshot.getRef().removeValue();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {}
                                        });

                                        childSnapshot.getRef().removeValue();
                                    }
                                }
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
        return mMixList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;

        public ViewHolder(View view){
            super(view);
            mContainer = view.findViewById(R.id.card_view);
            mTitleText = view.findViewById(R.id.album_title);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}