package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.io.FileNotFoundException;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    private TextView userNameView;
    private ImageView background;
    private ImageView image;
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

        userNameView = (TextView) findViewById(R.id.userText);
        userNameView.setText(userName + "さんようこそ");

        Display disp = getWindowManager().getDefaultDisplay();

        //背景の読み込み
        background = (ImageView) findViewById(R.id.background);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, disp.getWidth(), disp.getHeight(), false);
        background.setImageBitmap(bitmap2);
//        background.setImageResource(R.drawable.background);

        //画像の読み込み
        image = (ImageView) findViewById(R.id.avator);
        int imageId = 0;
        try {
            imageId = ViewUtil.getImageByWeight(70, 2, this);
        }catch(FileNotFoundException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        image.setImageResource(imageId);

        Button leftButton = (Button) findViewById(R.id.leftButton);
        Button centerButton = (Button) findViewById(R.id.centerButton);
        Button rightButton = (Button) findViewById(R.id.rightButton);

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
