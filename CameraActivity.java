package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    Button allowButton, denyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        allowButton = findViewById(R.id.allow);
        denyButton = findViewById(R.id.deny);

        allowButton.setOnClickListener(v -> {
            Toast.makeText(this, "Camera Access Allowed", Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
        });

        denyButton.setOnClickListener(v -> {
            Toast.makeText(this, "Camera Access Denied", Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
        });
    }

    // Navigate back to MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
