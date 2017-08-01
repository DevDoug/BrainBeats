package com.brainbeats.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.LoginActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.brainbeats.architecture.AccountManager;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.id;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

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
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            Intent dashboardIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(dashboardIntent);
                        } else {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Constants.buildInfoDialog(getContext(), "Error", "Invalid login or password.");
                            } catch(Exception e) {
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

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= Constants.USERNAME_MINIMUM_LENGTH;
    }
}