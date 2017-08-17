package com.brainbeats.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import com.brainbeats.data.BrainBeatsContract;
import com.brainbeats.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmCreateMixFragment extends Fragment implements View.OnClickListener  {

    public int PICK_IMAGE = 1;

    public TextView mMixTitle;
    public Button mSaveMixButton;
    public ImageView mAlbumCoverImage;
    public Bitmap albumImage;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Uri uri, String title, String imageUrl);
    }

    public ConfirmCreateMixFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_create_mix, container, false);
        mSaveMixButton = (Button) v.findViewById(R.id.confirm_save_mix_button);
        mMixTitle = (TextView) v.findViewById(R.id.mix_name);
        mAlbumCoverImage = (ImageView) v.findViewById(R.id.album_cover_art);

        mAlbumCoverImage.setOnClickListener(this);
        mSaveMixButton.setOnClickListener(this);
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
                ((MixerActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
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
            case R.id.album_cover_art:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.confirm_save_mix_button:
                String title = String.valueOf(mMixTitle.getText());
                if(!title.isEmpty()) { //require title
                    mListener.onFragmentInteraction(Constants.MIX_ADD_NEW, title, "");
                    mListener.onFragmentInteraction(Constants.MIX_SHOW_MIX_LIST);
                } else {
                    Constants.buildInfoDialog(getContext(), "", "Please enter a title");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    Constants.buildInfoDialog(getContext(), "", "There was an issue uploading that image");
                    return;
                }
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                albumImage = BitmapFactory.decodeStream(inputStream);
                mAlbumCoverImage.setImageBitmap(albumImage);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
