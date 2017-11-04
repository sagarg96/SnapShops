package com.example.sagar.popupshops_buyerside;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginSubmit;
    EditText email;
    EditText password;

    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //no login needed if already logged in
            Intent intent = new Intent(LoginActivity.this, SelectActionActivity.class);
            startActivity(intent);
        }
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginSubmit = (Button) findViewById(R.id.loginSubmit);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);

        loginSubmit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(email.getText().toString(), password.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d("here in", "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d("here out", "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };

//        Button buyerButton = (Button)findViewById(R.id.buyer_button);
//        Button vendorButton = (Button)findViewById(R.id.vendor_button);
//
//        buyerButton.setOnClickListener(new Button.OnClickListener(){
//            public void onClick(View view){
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        vendorButton.setOnClickListener(new Button.OnClickListener(){
//            public void onClick(View view){
//                Intent intent = new Intent(LoginActivity.this, view.class);
//                startActivity(intent);
//            }
//        });
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Here4", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, SelectActionActivity.class);
                            startActivity(intent);
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("here4", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Login Failed. Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}