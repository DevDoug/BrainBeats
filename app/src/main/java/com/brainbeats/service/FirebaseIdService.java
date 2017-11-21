package com.brainbeats.service;

import android.util.Log;

import com.brainbeats.architecture.AccountManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*
 * Created by Doug on 8/13/2017.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {

        private static final String TAG = "FirebaseIDService";

        @Override
        public void onTokenRefresh() {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        }

        /**
         * Persist token to third-party servers.
         *
         * Modify this method to associate the user's FCM InstanceID token with any server-side account
         * maintained by your application.
         *
         * @param token The new token.
         */
        private void sendRegistrationToServer(String token) {
            // Add custom implementation, as needed.
            AccountManager.getInstance(getApplicationContext()).setFirebaseToken(token);
        }
}