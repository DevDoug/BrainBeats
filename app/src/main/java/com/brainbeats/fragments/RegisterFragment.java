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
        mUsername = (EditText) view.findViewById(R.id.username_edit_text);
        mPassword = (EditText) view.findViewById(R.id.password_edit_text);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        mRegisterButton = (Button) view.findViewById(R.id.create_user_button);

        mRegisterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    public void registerUser() {
        if (isValid()) {
            mFirebaseAuth.createUserWithEmailAndPassword(mUsername.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            mFirebaseAuth.signInWithEmailAndPassword(mUsername.getText().toString(), mPassword.getText().toString())
                                    .addOnCompleteListener(getActivity(), signInTask -> {
                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                        addNewUser();

                                        if (!task.isSuccessful()) {
                                            try {
                                                throw task.getException();
                                            } catch (Exception e) {
                                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                                Toast.makeText(getActivity(), "Auth Failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Log.w(TAG, "Register:failed", task.getException());
                                Constants.buildInfoDialog(getContext(), "Unable To Create Account", e.getMessage());
                            }
                        }
                    });
        }
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

    public void addNewUser() {
        String Uid = mFirebaseAuth.getCurrentUser().getUid();
        DatabaseReference usersRef = mUserReference.child("users");

        Map<String, Object> brainBeatsUser = new HashMap<String, Object>();
        brainBeatsUser.put(Uid, new BrainBeatsUser(mFirebaseAuth.getCurrentUser().getUid(), mUsername.getText().toString()));

        usersRef.updateChildren(brainBeatsUser, (databaseError, databaseReference) -> {
            if (databaseError != null) { //if there was an error tell the user
                Constants.buildInfoDialog(getActivity(), "Unable To Login", "There was an issue saving that user to the database");
            } else {
                mListener.onFragmentInteraction(Constants.SHOW_NEW_ARTIST_INFO);
            }
        });
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
