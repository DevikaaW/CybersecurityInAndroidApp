package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LocationActivity extends AppCompatActivity {

    Button allowButton, denyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        allowButton = findViewById(R.id.allow1);
        denyButton = findViewById(R.id.deny1);

        allowButton.setOnClickListener(v -> {
            Toast.makeText(this, "Location Access Allowed", Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
        });

        denyButton.setOnClickListener(v -> {
            Toast.makeText(this, "Location Access Denied", Toast.LENGTH_SHORT).show();
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
