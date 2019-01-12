package com.lebanmohamed.speakup;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    EditText phoneNumber, code;
    Button submitVerification;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        phoneNumber = findViewById(R.id.phonenumber);
        code = findViewById(R.id.code);

        submitVerification = findViewById(R.id.verifybutton);

        submitVerification.setOnClickListener((v) -> startNumberVerification());

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential)
    {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    userIsLoggedIn();
                }
            }
        });

    }

    private void userIsLoggedIn()
    {

    }

    private void startNumberVerification()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber.getText().toString(), 120, TimeUnit.SECONDS, this, callbacks);



    }


}
