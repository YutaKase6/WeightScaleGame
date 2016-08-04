package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindString(R.string.NCMB_app_key)
    String APP_KEY;
    @BindString(R.string.NCMB_client_key)
    String CLIENT_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "assets/keifont.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf

        // NCMB 初期設定
        NCMB.initialize(this.getApplicationContext(), this.APP_KEY, this.CLIENT_KEY);
    }

    @OnClick(R.id.continue_text)
    public void onTap() {
        // ログインしていればホームへ
        if (NCMBUser.getCurrentUser().isAuthenticated()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
