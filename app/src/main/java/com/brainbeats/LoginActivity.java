package com.brainbeats;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import architecture.AccountManager;
import data.MixContract;
import data.MixDbHelper;
import utils.Constants;
import web.WebApiManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mLoginButton;
    private Button mSoundCloudLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
        mSoundCloudLogin = (Button) findViewById(R.id.sound_cloud_sign_in_button);

        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_ACTION_NEXT || id == EditorInfo.IME_ACTION_GO || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mSoundCloudLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSoundCloudLogin();
            }
        });

        final Button signInButton = mLoginButton;
        mEmailView.addTextChangedListener(new TextWatcher() { //if the user exists
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    Cursor userCursor = getContentResolver().query(
                            MixContract.UserEntry.CONTENT_URI, //Get users
                            null,  //return everything
                            MixContract.UserEntry.COLUMN_NAME_USER_NAME + MixDbHelper.WHERE_CLAUSE_EQUAL, //where the username is  equal
                            new String[]{mEmailView.getText().toString()},
                            null
                    );

                    String message = getString(R.string.register_text);
                    assert userCursor != null;
                    if (userCursor.getCount() >= 1) {
                        userCursor.moveToFirst();
                        while (!userCursor.isAfterLast()) {
                            String userName = userCursor.getString(userCursor.getColumnIndexOrThrow(MixContract.UserEntry.COLUMN_NAME_USER_NAME));
                            if (userName.equals(mEmailView.getText().toString())) { //user exists
                                message = getString(R.string.login_text);
                                break;
                            } else
                                message = getString(R.string.register_text);

                            userCursor.moveToNext();
                        }
                    }
                    signInButton.setText(message);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(this, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    public void attemptSoundCloudLogin(){
        String authSoundCloudURL = WebApiManager.API_CONNECT_URL + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&redirect_uri=" + "com.brainbeats.FROM_BROWSER" + "&response_type=token";
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authSoundCloudURL));
        if (viewIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(viewIntent);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Get all users
        return new CursorLoader(
                this,
                MixContract.UserEntry.CONTENT_URI,
                null,
                null,
                null,
                MixContract.UserEntry.COLUMN_NAME_USER_NAME + MixDbHelper.DB_SORT_TYPE_DESC
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(cursor.getColumnIndexOrThrow(MixContract.UserEntry.COLUMN_NAME_USER_NAME)));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.select_dialog_item, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final Context mContext;
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor userCursor = getContentResolver().query(
                    MixContract.UserEntry.CONTENT_URI, //Get users
                    null,  //return everything
                    MixContract.UserEntry.COLUMN_NAME_USER_NAME + MixDbHelper.WHERE_CLAUSE_EQUAL,
                    new String[]{mEmail},
                    null
            );

            userCursor.moveToFirst();
            while (!userCursor.isAfterLast()) {
                String userName = userCursor.getString(userCursor.getColumnIndexOrThrow(MixContract.UserEntry.COLUMN_NAME_USER_NAME));
                if (userName.equals(mEmail)) {
                    String userPassword = userCursor.getString(userCursor.getColumnIndexOrThrow(MixContract.UserEntry.COLUMN_NAME_USER_PASSWORD));
                    return userPassword.equals(mPassword);
                }
                userCursor.moveToNext();
            }

            // TODO: register the new account here.
            //if we have reached this point and not returned a false this user username does not exist so create a new account
            ContentValues values = new ContentValues();
            values.put(MixContract.UserEntry.COLUMN_NAME_USER_NAME, mEmail);
            values.put(MixContract.UserEntry.COLUMN_NAME_USER_PASSWORD, mPassword);
            Uri returnRow = getContentResolver().insert(MixContract.UserEntry.CONTENT_URI, values);
            long returnRowId = ContentUris.parseId(returnRow);

            if (returnRowId != -1) {
                return true;
            } else {
                Toast.makeText(mContext, "Account Creation Failed", Toast.LENGTH_LONG).show();
                return false; //specify account creation failed here.
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);
            if (success) {
                finish();
                Intent dashboardIntent = new Intent(mContext, MainActivity.class);
                AccountManager.getInstance(mContext).setUserId(mEmail);
                startActivity(dashboardIntent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }
}

