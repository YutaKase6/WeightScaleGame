package com.example.yutakase.weightscalegame;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import org.json.JSONObject;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    private TextView userNameView;
    private Button leftButton;
    private Button centerButton;
    private Button rightButton;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //ユーザー名の取得
        NCMBUser ncmb = NCMBUser.getCurrentUser();
        userName = ncmb.getUserName();

        userNameView = (TextView)findViewById(R.id.userText);
        userNameView.setText(userName+"さんようこそ");

        Button leftButton = (Button)findViewById(R.id.leftButton);
        Button centerButton = (Button)findViewById(R.id.centerButton);
        Button rightButton = (Button)findViewById(R.id.rightButton);
    }

    //ユーザ名を取得
    public String getUserName() {
        return "hogehoge";
    }

}
