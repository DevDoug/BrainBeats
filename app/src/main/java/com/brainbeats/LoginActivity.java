package com.brainbeats;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import architecture.AccountManager;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import entity.SoundCloudUser;
import utils.Constants;
import web.WebApiManager;

/**
 * Login screen
 * Should allow the user to login with both sound cloud and with Brain Beats
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, Constants.ConfirmDialogActionListener, View.OnClickListener {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    public CoordinatorLayout mCoordinatorLayout;
    public Button mLoginButton;
    public Button mSoundCloudLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //UI components.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_text_input);
        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordView = (EditText) findViewById(R.id.password_text_input);
        mSoundCloudLogin = (Button) findViewById(R.id.sound_cloud_sign_in_button);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content_coordinator_layout);

        getLoaderManager().initLoader(0, null, this);
        mLoginButton.setOnClickListener(this);
        mSoundCloudLogin.setOnClickListener(this);

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

        final Button signInButton = mLoginButton;
        mEmailView.addTextChangedListener(new TextWatcher() { //if the user exists
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= Constants.PASSWORD_MINIMUM_LENGTH) {
                    Cursor userCursor = getContentResolver().query(
                            BrainBeatsContract.UserEntry.CONTENT_URI, //Get users
                            null,  //return everything
                            BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, //where the username is  equal
                            new String[]{mEmailView.getText().toString()},
                            null
                    );

                    String message = getString(R.string.register_text);
                    if (userCursor != null && userCursor.getCount() >= 1) {
                        userCursor.moveToFirst();
                        while (!userCursor.isAfterLast()) {
                            String userName = userCursor.getString(userCursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME));
                            if (userName.equals(mEmailView.getText().toString())) { //user exists
                                message = getString(R.string.login_text);
                                break;
                            } else
                                message = getString(R.string.register_text);

                            userCursor.moveToNext();
                        }
                        userCursor.close();
                    }
                    signInButton.setText(message);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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

    public void attemptSoundCloudLogin() {
        if (Constants.isNetworkAvailable(LoginActivity.this)) {
            String authSoundCloudURL = WebApiManager.API_CONNECT_URL + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&redirect_uri=" + Constants.CALLBACK_URL + "&response_type=token";
            Intent loginIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authSoundCloudURL));
            loginIntent.addCategory(Intent.CATEGORY_DEFAULT);
            loginIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(loginIntent);
        } else {
            Constants.buildActionDialog(LoginActivity.this, getString(R.string.connect_to_network_message), getString(R.string.enable_wifi_in_settings_message), getString(R.string.go_to_settings_message), this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri returnData = intent.getData();
        String uriFrag = returnData.getFragment();
        HashMap<String, String> map = Constants.mapQueryParams(uriFrag);

        AccountManager.getInstance(this).setAccessToken(map.get(Constants.HASH_KEY_ACCESS_TOKEN));
        WebApiManager.getSoundCloudUser(this, map.get(Constants.HASH_KEY_ACCESS_TOKEN), new WebApiManager.OnObjectResponseListener() {
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
                    values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_SOUND_CLOUD_ID, soundCloudUser.getId());
                    Uri returnRow = getContentResolver().insert(BrainBeatsContract.UserEntry.CONTENT_URI, values);

                    long returnRowId = ContentUris.parseId(returnRow);
                    if (returnRowId != -1) { //New user create with sound cloud if success login.
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
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > Constants.PASSWORD_MINIMUM_LENGTH;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                BrainBeatsContract.UserEntry.CONTENT_URI, // Get users.
                null,
                null,
                null,
                BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + BrainBeatsDbHelper.DB_SORT_TYPE_DESC
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(cursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME)));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.select_dialog_item, emailAddressCollection);
        mEmailView.setAdapter(adapter);
    }

    @Override
    public void PerformDialogAction() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
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
                    BrainBeatsContract.UserEntry.CONTENT_URI, //Get users.
                    null,  //Return everything.
                    BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                    new String[]{mEmail},
                    null
            );

            if (userCursor != null) {
                userCursor.moveToFirst();

                while (!userCursor.isAfterLast()) {
                    String userName = userCursor.getString(userCursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME));
                    if (userName.equals(mEmail)) {
                        String userPassword = userCursor.getString(userCursor.getColumnIndexOrThrow(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD));
                        return userPassword.equals(mPassword);
                    }
                    userCursor.moveToNext();
                }
                userCursor.close();
            }

            //If we have reached this point and not returned a false this user username does not exist so create a new account.
            ContentValues values = new ContentValues();
            values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_NAME, mEmail);
            values.put(BrainBeatsContract.UserEntry.COLUMN_NAME_USER_PASSWORD, mPassword);
            Uri returnRow = getContentResolver().insert(BrainBeatsContract.UserEntry.CONTENT_URI, values);
            long returnRowId = ContentUris.parseId(returnRow);

            if (returnRowId != -1) {
                return true;
            } else {
                Toast.makeText(mContext, "Account Creation Failed", Toast.LENGTH_LONG).show();
                return false; //Specify account creation failed here.
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
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
        }
    }
}

