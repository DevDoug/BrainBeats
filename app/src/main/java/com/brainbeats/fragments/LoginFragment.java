package com.brainbeats.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainbeats.LoginActivity;
import com.brainbeats.R;
import com.brainbeats.utils.Constants;
import com.brainbeats.web.WebApiManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.id;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    //Data
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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

    /*@Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri returnData = intent.getData();
        String uriFrag = returnData.getFragment();
        HashMap<String, String> map = Constants.mapQueryParams(uriFrag);

        AccountManager.getInstance(this).setAccessToken(map.get(Constants.HASH_KEY_ACCESS_TOKEN));
        WebApiManager.getSoundCloudSelf(this, map.get(Constants.HASH_KEY_ACCESS_TOKEN), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<SoundCloudUser>() {
                }.getType();
                try {
                    SoundCloudUser soundCloudUser = gson.fromJson(object.toString(), token);

                    Cursor userCursor = getContentResolver().query(
                            BrainBeatsContract.UserEntry.CONTENT_URI, //Get users
                            null,  //return everything
                            BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                            new String[]{soundCloudUser.getUsername()},
                            null
                    );

                    if (userCursor != null) {
                        userCursor.moveToFirst();

                        while (!userCursor.isAfterLast()) {
                            String userName = userCursor.getString(userCursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME));
                            if (userName.equals(soundCloudUser.getUsername())) { // this sound cloud user already exists in the Brain Beats system
                                finish();
                                Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
                                AccountManager.getInstance(LoginActivity.this).setUserId(String.valueOf(soundCloudUser.getId()));
                                startActivity(dashboardIntent);
                            }
                            userCursor.moveToNext();
                        }
                        userCursor.close();
                    }

                    //If we have reached this point and not returned a false this user username does not exist so create a new account.
                    ContentValues values = new ContentValues();
                    values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME, soundCloudUser.getUsername());
                    values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD, Constants.generateEncryptedPass());
                    values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PROFILE_IMG, soundCloudUser.getAvatarUrl());
                    values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID, soundCloudUser.getId());
                    Uri returnRow = getContentResolver().insert(BrainBeatsContract.UserEntry.CONTENT_URI, values);

                    long returnRowId = ContentUris.parseId(returnRow);
                    if (returnRowId != -1) { //New user create with sound cloud if success login.
                        AccountManager.getInstance(LoginActivity.this).setSyncToSoundCloud(true); // logged in with sound cloud enable com.brainbeats.sync by default
                        finish();
                        Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
                        AccountManager.getInstance(LoginActivity.this).setUserId(String.valueOf(soundCloudUser.getId()));
                        startActivity(dashboardIntent);
                    } else {
                        Constants.buildInfoDialog(LoginActivity.this, getString(R.string.login_failed_error_message), getString(R.string.issue_creating_account_error_message));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }*/

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > Constants.USERNAME_MINIMUM_LENGTH;
    }
}