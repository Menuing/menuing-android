package com.example.mrg20.menuing_android.global_activities;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.mrg20.menuing_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class GlobalActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //nothing
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //USER FUNCTIONS
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void loggOff() {
        mAuth.signOut();
    }

    @NonNull
    public static String getUserId() {
        if (mAuth.getCurrentUser() != null)
            return mAuth.getCurrentUser().getUid();
        else
            return "";
    }

    public void signedIn(String mail, String password, final Handler h){
        final Message msg = new Message();
        final boolean[] obj = new boolean[1];

        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        obj[0] = true;
                        if (!task.isSuccessful()) {
                            obj[0] = false;
                        }
                        msg.what = 0;
                        msg.obj = obj[0];
                        h.sendMessage(msg);
                    }
                });
    }

    public void isAdmin(final Handler h) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Admin");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap json = (HashMap) dataSnapshot.getValue();
                if(json != null && !json.keySet().isEmpty()) {
                    for (Object key : json.keySet() ) {
                        if(getUserId().equals(json.get(key))){
                            h.sendEmptyMessage(0);
                            return; //Sortir de la funció
                        }
                    }
                }
                h.sendEmptyMessage(-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                h.sendEmptyMessage(-1);
            }
        };

        myRef.addValueEventListener(listener);
    }

    public void subscribeToFCM() {
        Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.testTopic));
                        break;
                    case -1:
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.testTopic));
                        break;
                }
                userToken = FirebaseInstanceId.getInstance().getToken();

            }
        };

        isAdmin(h);
    }

    public String getUserToken() {
        return userToken;
    }
}
