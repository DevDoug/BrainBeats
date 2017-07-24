package com.brainbeats.fragments;

import android.content.Intent;
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

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
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
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mRegisterButton;

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

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            mFirebaseAuth.signInWithEmailAndPassword(mUsername.getText().toString(), mPassword.getText().toString())
                                    .addOnCompleteListener(getActivity(), signInTask -> {
                                        addNewUser();
                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                            Toast.makeText(getActivity(), "Auth Failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Constants.buildInfoDialog(getActivity(), "", "There was an issue creating that account");
        }
    }

    public void addNewUser(){
        DatabaseReference usersRef = mUserReference.child("users");
        String key = mUsername.getText().toString().split("@")[0];

        Map<String, Object> brainBeatsUser = new HashMap<String, Object>();
        brainBeatsUser.put(mFirebaseAuth.getCurrentUser().getUid(), new BrainBeatsUser(mUsername.getText().toString()));

        usersRef.updateChildren(brainBeatsUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null) { //if there was an error tell the user
                    Constants.buildInfoDialog(getActivity(), "Error", "There was an issue saving that user to the database");
                } else {
                    Intent dashboardIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(dashboardIntent);
                }
            }
        });
    }

    public boolean isValid() {
        if (TextUtils.isEmpty(mUsername.getText())
                || mUsername.getText().length() < Constants.USERNAME_MINIMUM_LENGTH
                || !mUsername.getText().toString().contains("@")) {
            return false;
        }

        if (TextUtils.isEmpty(mPassword.getText()) || mPassword.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
            return false;
        }

        if (TextUtils.isEmpty(mConfirmPassword.getText()) || mConfirmPassword.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
            return false;
        }

        return true;
    }
}