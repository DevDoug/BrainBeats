package com.brainbeats.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brainbeats.R;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "RegisterFragment";

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUserReference;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegisterButton;

    private RegisterFragment.OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(Uri uri, String username, String password);
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUserReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mUsername = view.findViewById(R.id.username_edit_text);
        mPassword = view.findViewById(R.id.password_edit_text);
        mConfirmPassword = view.findViewById(R.id.confirm_password_edit_text);
        mRegisterButton = view.findViewById(R.id.create_user_button);

        mRegisterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    public void registerUser() {
        if (isValid())
            mListener.onFragmentInteraction(Constants.CREATE_NEW_USER_URI, mUsername.getText().toString(), mPassword.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
            mListener = (RegisterFragment.OnFragmentInteractionListener) context;
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

    public boolean isValid() {
        if (TextUtils.isEmpty(mUsername.getText())
                || mUsername.getText().length() < Constants.USERNAME_MINIMUM_LENGTH
                || !mUsername.getText().toString().contains("@")) {
            mUsername.setError(getString(R.string.error_invalid_email));
            mUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mPassword.getText()) || mPassword.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
            mPassword.setError(getString(R.string.error_invalid_password));
            mPassword.requestFocus();
            return false;
        }

        if (mConfirmPassword.getText().toString().compareTo(mPassword.getText().toString()) != 0) {
            mConfirmPassword.setError(getString(R.string.error_confirm_password_does_not_match));
            mConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}
