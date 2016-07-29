package com.example.yutakase.weightscalegame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    private TextView userName;
    private Button leftButton;
    private Button centerButton;
    private Button rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userName = (TextView)findViewById(R.id.userText);
        userName.setText(getUserName()+"さんようこそ");

        Button leftButton = (Button)findViewById(R.id.leftButton);
        Button centerButton = (Button)findViewById(R.id.centerButton);
        Button rightButton = (Button)findViewById(R.id.rightButton);
    }

    //ユーザ名を取得
    public String getUserName() {
        return "hogehoge";
    }

}
