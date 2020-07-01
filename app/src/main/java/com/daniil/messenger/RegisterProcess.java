package com.daniil.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.daniil.messenger.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterProcess extends AppCompatActivity {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.nickname)
    EditText nickname;
    private FirebaseAuth mAuth;
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_process);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

    }

    @OnClick(R.id.enter)
    public void enter() {
        String e = email.getText().toString();
        String p = password.getText().toString();
        String n = nickname.getText().toString();
        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fireuserID = FirebaseAuth.getInstance().getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("email", "createUserWithEmail:success");
                            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putBoolean("authstate", true);
                            editor.commit();

                            User user = new User(e,"default",fireuserID.getUid() ,n,null);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("");
                            //String id = myRef.child("users").push().getKey();
                            myRef.child("users").child(fireuserID.getUid()).setValue(user);

                            editor.putString("id",fireuserID.getUid());
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("email", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterProcess.this, "Authentication failed.",              //
                                    Toast.LENGTH_SHORT).show();                                                      //Create new blank activity "About App", and make an icon for the app FlatIcon.com
                                                                                                                     //Finish adding currencies for the currency exchange app and put it out on github
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}

