package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.start_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,LoginActivity.class));
    }
}
