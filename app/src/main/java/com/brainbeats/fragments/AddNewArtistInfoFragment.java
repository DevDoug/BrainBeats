package com.brainbeats.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewArtistInfoFragment extends Fragment implements View.OnClickListener {

    private int PICK_IMAGE_REQUEST = 1;

    Uri mUploadedProfileImageUri;
    TextView mArtistName;
    ImageView mArtistProfile;
    TextView mArtistDescription;
    TextView mSkipText;
    Button mConfirmButton;

    public AddNewArtistInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_artist_info, container, false);
        mArtistName = (EditText) view.findViewById(R.id.artist_name);
        mArtistProfile = (ImageView) view.findViewById(R.id.profile_cover_image);
        mArtistDescription = (EditText) view.findViewById(R.id.artist_description);
        mConfirmButton = (Button) view.findViewById(R.id.confirm_save_artist_detail_button);
        mSkipText = (TextView) view.findViewById(R.id.skip_text);

        mArtistProfile.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
        mSkipText.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_save_artist_detail_button:
                saveArtistDetail();
                break;
            case R.id.profile_cover_image:
                uploadArtistCover();
                break;
            case R.id.skip_text:
                goToDashboard();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUploadedProfileImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mUploadedProfileImageUri);
                mArtistProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveArtistDetail() {
        String emailName = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(emailName);
        Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("artistName", mArtistName.getText().toString());
        userData.put("artistDescription", mArtistDescription.getText().toString());
        userData.put("profileImage", mUploadedProfileImageUri.toString());
        user.updateChildren(userData);

        goToDashboard();
    }

    public void uploadArtistCover(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void goToDashboard(){
        Intent dashboardIntent = new Intent(getContext(), MainActivity.class);
        startActivity(dashboardIntent);
    }
}