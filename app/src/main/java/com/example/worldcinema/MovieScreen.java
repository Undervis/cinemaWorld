package com.example.worldcinema;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MovieScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_screen);

        Intent intent = getIntent();

        TextView textView = findViewById(R.id.tvTag);
        textView.setText(intent.getStringExtra("tag"));
    }
}