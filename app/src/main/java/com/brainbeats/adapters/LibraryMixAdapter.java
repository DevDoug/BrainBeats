package com.brainbeats.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.data.BrainBeatsDbHelper;
import com.brainbeats.entity.Track;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by douglas on 5/20/2016.
 */
public class LibraryMixAdapter extends RecyclerView.Adapter<LibraryMixAdapter.ViewHolder> {

    Context mAdapterContext;
    ArrayList<Mix> mMixList;

    public LibraryMixAdapter(Context context, ArrayList<Mix> items) {
        mAdapterContext = context;
        mMixList = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mContainer;
        TextView mTitleText;
        ImageView mPlay;
        ImageView mMix;

        public ViewHolder(View view) {
            super(view);
            mContainer = (CardView) view.findViewById(R.id.card_view);
            mTitleText = (TextView) view.findViewById(R.id.album_title);
            mPlay = (ImageView) view.findViewById(R.id.play_mix);
            mMix = (ImageView) view.findViewById(R.id.mix_beat);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mAdapterContext).inflate(R.layout.library_beat_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mix mix = mMixList.get(position);
        holder.mTitleText.setText(mix.getMixTitle());

        holder.mContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Constants.buildActionDialog(mAdapterContext, "Delete Song", "Do you want to delete this song from your library", "confirm", new Constants.ConfirmDialogActionListener() {
                    @Override
                    public void PerformDialogAction() {
                        FirebaseDatabase Database = FirebaseDatabase.getInstance();
                        Query reference = Database
                                .getReference("mixes/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .orderByChild("artistId")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());


                        reference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Mix mixToDelete = childSnapshot.getValue(Mix.class);
                                    if(mixToDelete.getMixTitle().equals(mix.getMixTitle()))
                                        childSnapshot.getRef().removeValue();
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

        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mix.getStreamURL() == null || mix.getStreamURL().isEmpty()) { //coming from Brain Beats
                    String storageUrl = mix.getFirebaseStorageUrl();
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference mixStorage = storageReference.child("mixes/" + storageUrl);

                    try {
                        final long ONE_MEGABYTE = 1024 * 1024;
                        mixStorage.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                try{
                                    File outputFile = File.createTempFile(mix.getMixTitle(), ".3gp", mAdapterContext.getCacheDir());
                                    outputFile.deleteOnExit();
                                    FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                                    fileoutputstream.write(bytes);
                                    fileoutputstream.close();

                                    Track playTrack = new Track(mix);
                                    ((LibraryActivity) mAdapterContext).mCurrentSong = playTrack;
                                    ((LibraryActivity) mAdapterContext).mAudioService.setPlayingSong(playTrack);
                                    ((LibraryActivity) mAdapterContext).mAudioService.playBrainBeatsSong(Uri.parse(outputFile.getPath()));

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                exception.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Track playTrack = new Track(mix);
                    ((LibraryActivity) mAdapterContext).mCurrentSong = playTrack;
                    ((LibraryActivity) mAdapterContext).mAudioService.setPlayingSong(playTrack);
                    ((LibraryActivity) mAdapterContext).mAudioService.playSong(Uri.parse(playTrack.getStreamURL()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMixList.size();
    }
}