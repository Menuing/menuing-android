package com.example.mrg20.menuing_android.global_activities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.activities.AllergiesActivity;
import com.example.mrg20.menuing_android.activities.UserProfile;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userToken;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String PREFS_PREMIUM = "PremiumFile";
    public SharedPreferences settings;
    public SharedPreferences premiumSettings;


    //Recordar de posar el port

    protected String ipserver = "36607c7a.ngrok.io";

    protected static final int BREAKFAST = 0;
    protected static final int LUNCH = 1;
    protected static final int DINNER = 2;

    protected static final int NO_PREFERENCES = 3;
    protected static final int THREE_INGREDIENTS= 4;
    protected static final int FAST_TO_DO = 5;
    protected static final int COCKTAIL = 6;
    protected static final int DESSERT = 7;

    protected static final int RECIPE = 8;
    protected static final int MEAL = 9;

    protected static ProgressDialog progress = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(PREFS_NAME, 0);
        premiumSettings = getSharedPreferences(PREFS_PREMIUM, 0);

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
    }

    public void pay(){
        SharedPreferences.Editor editor = premiumSettings.edit();
        editor.putString(settings.getString("UserMail",""), "");
        editor.commit();
    }

    public void logout(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("UserMail", "");
        editor.commit();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mAuth.signOut();
    }

    protected void delete() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete();
        mAuth.signOut();
    }

    //USER FUNCTIONS
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    @NonNull
    public static String getUserId() {
        if (mAuth.getCurrentUser() != null)
            return mAuth.getCurrentUser().getUid();
        else
            return "";
    }

    public void register(final String mail, final String password){
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(GlobalActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(GlobalActivity.this, getString(R.string.err_register), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(GlobalActivity.this, getString(R.string.user_registered), Toast.LENGTH_LONG).show();
                            Handler h = new Handler(){
                                @Override
                                public void handleMessage(Message msg){
                                    switch (msg.what){
                                        case 0:
                                            if ((Boolean) msg.obj) {
                                                startActivity(new Intent(new Intent(GlobalActivity.this, UserProfile.class)));
                                                finish();
                                            }else{
                                                Toast.makeText(getApplicationContext(), getString(R.string.err_login_fail), Toast.LENGTH_SHORT).show();
                                            }
                                    }
                                }
                            };
                            signedIn(mail, password, h);
                        }
                    }
                });
    }


    public void signedIn(final String mail, String password) {
        signedIn(mail, password, null);
    }


    public void signedIn(final String mail, String password, final Handler h){
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
                        else{
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("UserMail", mail);
                            editor.commit();
                        }
                        msg.what = 0;
                        msg.obj = obj[0];
                        if(h!=null) h.sendMessage(msg);
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

    public void vibrate(){
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    @Override
    public boolean onSupportNavigateUp() {
        vibrate();
        finish();
        return true;
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
