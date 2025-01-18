package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button login_btn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textview, fingerprintText;

    int loginAttempts = 0; // Counter for login attempts
    private static final int MAX_LOGIN_ATTEMPTS = 5; // Maximum allowed attempts
    private static final int LOCKOUT_DURATION = 30000; // 30 seconds lockout duration

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        editTextPassword = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);
        textview = findViewById(R.id.registerNow);
        fingerprintText = findViewById(R.id.fingerprint);

        // Handle registration click
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
                finish();
            }
        });

        // Handle email/password login
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if login attempts exceeded
                if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    Toast.makeText(Login.this, "Too many failed attempts. Please wait 30 seconds.", Toast.LENGTH_SHORT).show();
                    disableLoginButton();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && !user.isEmailVerified()) {
                                        // Email not verified
                                        Toast.makeText(Login.this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut(); // Sign out the user
                                        return;
                                    }

                                    // Login successful and email verified
                                    loginAttempts = 0; // Reset attempts on successful login
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    loginAttempts++; // Increment failed attempts
                                    Toast.makeText(Login.this, "Authentication failed. Attempt " + loginAttempts + " of " + MAX_LOGIN_ATTEMPTS, Toast.LENGTH_SHORT).show();

                                    // Disable the login button if max attempts reached
                                    if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                                        disableLoginButton();
                                    }
                                }
                            }
                        });
            }
        });

        // Initialize biometric authentication
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {

                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use App Password")
                .build();

        // Check if biometric is available
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS)  {
            fingerprintText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    biometricPrompt.authenticate(promptInfo);
                }
            });
        } else {
            fingerprintText.setVisibility(View.GONE);
            Toast.makeText(this, "Biometric authentication not supported", Toast.LENGTH_SHORT).show();
        }
    }

    // Disable the login button and re-enable it after a delay
    private void disableLoginButton() {
        login_btn.setEnabled(false);
        Toast.makeText(this, "Too many failed attempts. Login disabled for 30 seconds.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            login_btn.setEnabled(true); // Re-enable the login button
            loginAttempts = 0; // Reset attempts
            Toast.makeText(this, "You can now try logging in again.", Toast.LENGTH_SHORT).show();
        }, LOCKOUT_DURATION);
    }
}
