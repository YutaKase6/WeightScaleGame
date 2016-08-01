package com.example.yutakase.weightscalegame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import org.json.JSONObject;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    private TextView userNameView;
    private ImageView image;
    private Button leftButton;
    private Button centerButton;
    private Button rightButton;

    private String userName;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Context c = this;

        //ユーザー名の取得
        NCMBUser ncmb = NCMBUser.getCurrentUser();
        userName = ncmb.getUserName();

        userNameView = (TextView) findViewById(R.id.userText);
        userNameView.setText(userName + "さんようこそ");

        //画像の読み込み
        image = (ImageView) findViewById(R.id.avator);
        image.setImageResource(R.drawable.chara1);

        Button leftButton = (Button) findViewById(R.id.leftButton);
        Button centerButton = (Button) findViewById(R.id.centerButton);
        Button rightButton = (Button) findViewById(R.id.rightButton);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //ユーザ名を取得
    public String getUserName() {
        return "hogehoge";
    }

    //アバターをクリックするとMyPageActivityに飛びます
    public void ImageClick(View v) {
        Intent intent = new Intent(this, MyPageActivity.class);
        startActivity(intent);
    }
}
