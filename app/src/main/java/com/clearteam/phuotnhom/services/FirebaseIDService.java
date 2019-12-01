package com.clearteam.phuotnhom.services;

import android.util.Log;

import com.clearteam.phuotnhom.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        HSSPreference.getInstance().putString(Const.TOKEN_FIREBASE, refreshedToken);
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
        super.onTokenRefresh();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refeshToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d("BBBB",refeshToken);
        if (firebaseUser != null){
            updateToken(refeshToken);
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
    private void updateToken(String refeshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(refeshToken);
        reference.child(firebaseUser.getUid()).setValue(token);

    }
}