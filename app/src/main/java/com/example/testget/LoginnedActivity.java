package com.example.testget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class LoginnedActivity extends AppCompatActivity {
    private static TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginned_layout);

        response = findViewById(R.id.tv_response_result);
        response.setText(getIntent().getStringExtra("data"));

    }
}
