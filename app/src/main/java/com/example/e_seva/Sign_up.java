package com.example.e_seva;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private  FirebaseUser user;
    EditText emailtxt, passwordtxt;
    CardView signupbuton;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signupbuton = findViewById(R.id.signupbutton);
        emailtxt = findViewById(R.id.signupemailtxt);
        passwordtxt = findViewById(R.id.signuppasswordtxt);
        progressBar =findViewById(R.id.signupprogress);
        firebaseAuth = FirebaseAuth.getInstance();
         user = FirebaseAuth.getInstance().getCurrentUser();



      signupbuton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String txt_email = emailtxt.getText().toString();
                    String txt_password = passwordtxt.getText().toString();
                     progressBar.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(Sign_up.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                    } else if (txt_password.length() < 6) {
                        Toast.makeText(Sign_up.this, "Password too short!", Toast.LENGTH_SHORT).show();
                    } else{
                        registerUser(txt_email, txt_password);
                    }
                }
            });





    }

    private void registerUser(final String emailtxt, String passwordtxt) {

        if (emailtxt.matches(emailPattern)) {


            firebaseAuth.createUserWithEmailAndPassword(emailtxt, passwordtxt)
                    .addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {


                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Sign_up.this, "Email Address Already Registered", Toast.LENGTH_LONG).show();
                            }
                            if (task.isSuccessful()) {
                                user = firebaseAuth.getInstance().getCurrentUser();
                                 user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sign_up.this, "Verification Link Send To Your Email Pls Check", Toast.LENGTH_LONG).show();

                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent1 = new Intent(getApplicationContext(),Sign_in.class);
                                        intent1.putExtra("username", emailtxt);
                                        startActivity(intent1);
                                      //  Toast.makeText(Sign_up.this, "Registration Successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Sign_up.this,Sign_in.class));



                                    }


                                 }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Sign_up.this, "Can Not verify Your Email Please Try Again", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });


                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Sign_up.this, "Login Error", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Sign_up.this,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(Sign_up.this, "Invailed Email , Please Enter Valid Email", Toast.LENGTH_LONG).show();
        }


    }
}

