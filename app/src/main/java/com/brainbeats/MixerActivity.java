package com.brainbeats;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;

import com.brainbeats.architecture.AccountManager;
import com.brainbeats.architecture.BaseActivity;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.fragments.ConfirmCreateMixFragment;
import com.brainbeats.fragments.CreateMixFragment;
import com.brainbeats.fragments.ExplainMaestroFragment;
import com.brainbeats.fragments.MixerDetailFragment;
import com.brainbeats.fragments.MixerFragment;
import com.brainbeats.model.Mix;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MixerActivity extends BaseActivity implements View.OnClickListener,
        MixerFragment.OnFragmentInteractionListener,
        CreateMixFragment.OnFragmentInteractionListener,
        ConfirmCreateMixFragment.OnFragmentInteractionListener,
        ExplainMaestroFragment.OnFragmentInteractionListener {

    private FirebaseDatabase mDatabase;
    StorageReference mStorageRef;

    Fragment mMixerFragment;
    Fragment mNewMixFragment;
    Fragment mConfirmNewMixFragment;
    Fragment mExplainMaestroFragment;
    Fragment mMixerDetailFragment;

    Bundle mUserSelections;
    public FloatingActionButton mMainActionFab;
    public Mix mNewMix;
    public CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (savedInstanceState == null) {
            mMixerFragment = new MixerFragment();
            mNewMixFragment = new CreateMixFragment();
            mConfirmNewMixFragment = new ConfirmCreateMixFragment();
            mExplainMaestroFragment = new ExplainMaestroFragment();
            mMixerDetailFragment = new MixerDetailFragment();
            switchToMixerFragment();
        }

        if (mUserSelections == null) {
            mUserSelections = new Bundle();
        }

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String intentAction = intent.getAction();
            if (intentAction.equalsIgnoreCase(Constants.INTENT_ACTION_GO_TO_DETAIL_FRAGMENT)) {
                Mix mix = (Mix) intent.getExtras().get(Constants.KEY_EXTRA_SELECTED_MIX);
                loadMixerDetailFragment(mix);
            }
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);
        mMainActionFab = (FloatingActionButton) findViewById(R.id.main_action_fob);

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_add_white));
        mMainActionFab.setOnClickListener(this);
    }

    public void switchToMixerFragment() {
        replaceFragment(mMixerFragment, mMixerFragment.getTag());
    }

    public void switchToNewMixFragment() {
        toggleNavDrawerIcon();

        mMainActionFab.setImageDrawable(getDrawable(R.drawable.ic_m));

        if(mAudioService.getIsPlaying() || mAudioService.getIsPaused()) {
            mAudioService.stopSong();
            hideCurrentSongView();
        }

        mNewMix = new Mix();
        replaceFragment(mNewMixFragment, mNewMixFragment.getTag());
    }

    public void switchToMaestroShown(){
        replaceFragment(mExplainMaestroFragment, mExplainMaestroFragment.getTag());
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        navigateUpOrBack(this, fm);
    }

    public void switchToConfirmCreateMixFragment() {
        toggleNavDrawerIcon();
        replaceFragment(mConfirmNewMixFragment, mConfirmNewMixFragment.getTag());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_action_fob:
                if (mMixerFragment.isVisible()) {
                    if(AccountManager.getInstance(this).getMaestroShown())
                        switchToNewMixFragment();
                    else
                        switchToMaestroShown();
                } else if (mNewMixFragment.isVisible()) {
                    ((CreateMixFragment) mNewMixFragment).summonMaestro();
                }
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.compareTo(Constants.NEW_MIX_HIDE_FAB) == 0) {
            hideMainFAB();
        } else if (uri.compareTo(Constants.MIX_SHOW_FAB) == 0) {
            showMainFAB();
        } else if (uri.compareTo(Constants.MIX_SHOW_MIX_LIST) == 0) {
            Intent mixerIntent = new Intent(getApplicationContext(), MixerActivity.class);
            createBackStack(mixerIntent);
        } else if (uri.compareTo(Constants.STOP_SONG_URI) == 0){
            mAudioService.mIsRecordingTest = true;
            mAudioService.stopSong();
        } else if (uri.compareTo(Constants.NEW_MIX_CREATE) == 0) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack();
            AccountManager.getInstance(this).setMaestroShown(true);
            switchToNewMixFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String source) {
        if (uri.compareTo(Constants.LOAD_SONG_URI) == 0) {
            mAudioService.mIsRecordingTest = true;
            mAudioService.playSong(Uri.fromFile(new File(source)));
        } else if (uri.compareTo(Constants.NEW_MIX_LOAD_CONFIRM_FRAG) == 0) {
            switchToConfirmCreateMixFragment();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, String title, String imageUrl) {
        if (uri.compareTo(Constants.MIX_ADD_NEW) == 0) {
            File tempFile = new File(getExternalCacheDir().getAbsolutePath() + "/" + "temp" + ".3gp");
            File mixFile =  new File(getExternalCacheDir().getAbsolutePath() + "/" + title + ".3gp");
            boolean success = tempFile.renameTo(mixFile);

            mNewMix.setMixTitle(title);
            mNewMix.setFirebaseStorageUrl(title + ".3gp");
            mNewMix.setArtistId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            uploadArtistMixToCloudStorage(mixFile);

            Query mixRef = mDatabase.getReference("mixes/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            mixRef.getRef().push().setValue(mNewMix);
        }
    }

    public void loadMixerDetailFragment(Mix mix) {
        if (mDrawerToggle != null)
            toggleNavDrawerIcon();

        mUserSelections.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX, mix);
        mMixerDetailFragment.setArguments(mUserSelections);
        replaceFragment(mMixerDetailFragment, mMixerDetailFragment.getTag());
    }

    public void uploadArtistMixToCloudStorage(File mix){
        try {
            if (mix != null) {
                Uri uri = Uri.fromFile(mix);
                StorageReference riversRef = mStorageRef.child("mixes/" + uri.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(uri);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Snackbar errorSnack;
                    errorSnack = Snackbar.make(mCoordinatorLayout, getString(R.string.error_processing_request), Snackbar.LENGTH_LONG);
                    errorSnack.show();
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
