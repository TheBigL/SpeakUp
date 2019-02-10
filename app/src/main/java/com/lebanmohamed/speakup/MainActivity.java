package com.lebanmohamed.speakup;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    private EditText phoneNumber, code;
    private Button submitVerification;
    private String verificationID;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        userIsLoggedIn();

        phoneNumber = findViewById(R.id.phonenumber);
        code = findViewById(R.id.code);

        submitVerification = findViewById(R.id.verifybutton);

        submitVerification.setOnClickListener((v) ->
        {
            if(verificationID != null)
            {
                VerifyNumberUsingCode(verificationID, code.getText().toString());
            }

            else
            {
                startNumberVerification();
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verification, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(verification, forceResendingToken);

                verificationID = verification;
                submitVerification.setText("Verify Code");

            }
        };

    }

    private void VerifyNumberUsingCode(String verifyID, String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyID, code);
        signInWithPhoneAuthCredential(credential);

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }

    }

    private void startNumberVerification()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
        );
    }


}
