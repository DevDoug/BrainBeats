package com.brainbeats.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.model.BrainBeatsUser;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "LoginFragment";

    //Data
    private FirebaseAuth mFirebaseAuth;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mSignUpText;
    public CoordinatorLayout mCoordinatorLayout;
    public Button mLoginButton;
    public Button mSoundCloudLogin;
    private LoginFragment.OnFragmentInteractionListener mListener;


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email_text_input);
        mSignUpText = (TextView) view.findViewById(R.id.register_text);
        mLoginButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mPasswordView = (EditText) view.findViewById(R.id.password_text_input);
        mSoundCloudLogin = (Button) view.findViewById(R.id.sound_cloud_sign_in_button);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.main_content_coordinator_layout);

        mLoginButton.setOnClickListener(this);
        mSoundCloudLogin.setOnClickListener(this);

        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterUser();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.sound_cloud_sign_in_button:
                attemptSoundCloudLogin();
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (LoginFragment.OnFragmentInteractionListener) context;
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

    public void goToRegisterUser() {
        mListener.onFragmentInteraction(Constants.GO_TO_REGISTER_NEW_USER_URI);
    }

    private void attemptLogin() {
        if (isValid()) {
            mFirebaseAuth.signInWithEmailAndPassword(mEmailView.getText().toString(), mPasswordView.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            String emailName = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
                            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("users").child(emailName);
                            user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    BrainBeatsUser currentUser = dataSnapshot.getValue(BrainBeatsUser.class);
                                    ((com.brainbeats.architecture.Application) getActivity().getApplication()).setUserDetails(currentUser);
                                    Intent dashboardIntent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(dashboardIntent);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Constants.buildInfoDialog(getContext(), "Error", "Invalid login or password.");
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
        }
    }

    public void attemptSoundCloudLogin() {
        if (Constants.isNetworkAvailable(getActivity())) {
            String authSoundCloudURL = WebApiManager.API_CONNECT_URL + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL + "&response_type=token";
            Intent loginIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authSoundCloudURL));
            loginIntent.addCategory(Intent.CATEGORY_DEFAULT);
            loginIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(loginIntent);
        } else {
            //Constants.buildActionDialog(getActivity(), getString(R.string.connect_to_network_message), getString(R.string.enable_wifi_in_settings_message), getString(R.string.go_to_settings_message), getActivity());
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(mEmailView.getText())) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mPasswordView.getText()) || mPasswordView.getText().length() < Constants.PASSWORD_MINIMUM_LENGTH) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return false;
        }

        return true;
    }
}