package com.brainbeats.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.brainbeats.R;
import com.brainbeats.SettingsActivity;
import com.brainbeats.architecture.Application;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.utils.Constants;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ArtistProfileFragment extends Fragment implements View.OnClickListener {

    private int PICK_IMAGE_REQUEST = 1;

    Uri mUploadedProfileImageUri;
    DatabaseReference mArtistReference;
    StorageReference mStorageRef;


    private EditText mArtistName;
    private ImageView mProfileImage;
    private EditText mArtistDescription;
    private Button mSaveButton;
    private OnFragmentInteractionListener mListener;

    public ArtistProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtistReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artist_profile, container, false);
        mArtistName = (EditText) v.findViewById(R.id.artist_name);
        mProfileImage = (ImageView) v.findViewById(R.id.profile_cover_image);
        mArtistDescription = (EditText) v.findViewById(R.id.artist_description);
        mSaveButton = (Button) v.findViewById(R.id.confirm_save_artist_detail_button);

        mProfileImage.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ((SettingsActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mArtistReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BrainBeatsUser user = dataSnapshot.getValue(BrainBeatsUser.class);
                setUserDetails(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_save_artist_detail_button:
                saveArtistDetails();
                break;
            case R.id.profile_cover_image:
                pickArtistCoverImage();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void pickArtistCoverImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUploadedProfileImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mUploadedProfileImageUri);
                mProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadArtistCoverImageToCloudStorage(){
        try {
            if (mUploadedProfileImageUri != null) {
                StorageReference riversRef = mStorageRef.child("images/" + mUploadedProfileImageUri.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(mUploadedProfileImageUri);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveArtistDetails(){
        Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("artistName", mArtistName.getText().toString());
        userData.put("artistDescription", mArtistDescription.getText().toString());
        //userData.put("artistProfileImage", mUploadedProfileImageUri.getPath());

        mArtistReference.updateChildren(userData).addOnCompleteListener(task -> {
            if(task.isSuccessful())
                Constants.buildInfoDialog(getContext(), "Saved", "Artist details saved successfully");
            else
                Constants.buildInfoDialog(getContext(), "Error", "There was an issue saving artist details please try again later");
        });
    }

    public void setUserDetails(BrainBeatsUser user) {
        mArtistName.setText(user.getArtistName());
        mArtistDescription.setText(user.getArtistDescription());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageStorage = storageReference.child("images/" + user.getArtistProfileImage());

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageStorage)
                .into(mProfileImage);
    }
}
