package com.example.mrg20.menuing_android.global_activities;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.widget.Toast;

import com.example.mrg20.menuing_android.LoginActivity;
import com.example.mrg20.menuing_android.MainPageActivity;
import com.example.mrg20.menuing_android.R;
import com.example.mrg20.menuing_android.other_classes.Recipe;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GlobalActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String userToken;
    public static final String PREFS_NAME = "MyPrefsFile";
    public SharedPreferences settings;

    public List<Recipe> recipeList;

    //Recordar de posar el port
    protected String ipserver = "4b4c08c0.ngrok.io";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(PREFS_NAME, 0);

        recipeList = new ArrayList<>();

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
        logout();
    }

    public void logout(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

    public void register(String mail, String password){
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(GlobalActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(GlobalActivity.this, getString(R.string.err_register), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(GlobalActivity.this, getString(R.string.user_registered), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(new Intent(GlobalActivity.this, MainPageActivity.class)));
                            finish();
                        }
                    }
                });
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
