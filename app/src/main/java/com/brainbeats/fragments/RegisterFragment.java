package com.brainbeats.fragments;

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
import com.brainbeats.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "RegisterFragment";


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

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
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Constants.buildInfoDialog(getActivity(), "", "There was an issue creating that account");
        }
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
