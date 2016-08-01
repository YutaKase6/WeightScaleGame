package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nifty.cloud.mb.core.NCMBUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自分の情報を表示する画面
 */
public class MyPageActivity extends AppCompatActivity {

    String userName;
    double goalWeight=0;
    double currentWeight=60;

    @Bind(R.id.goal_text_view)
    TextView goalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NCMBUser user=NCMBUser.getCurrentUser();
        userName=user.getUserName();
        setTitle(userName+"さんの部屋");
        goalWeight=user.getDouble("goalWeight");
        double offsetWeight = currentWeight-goalWeight;
        goalTextView.setText("目標まであと"+offsetWeight+"kg");
    }

    @OnClick(R.id.record_button)
    void onClickRecordButton(){
        startActivity(new Intent(this, MyRecordActivity.class));
    }
}
