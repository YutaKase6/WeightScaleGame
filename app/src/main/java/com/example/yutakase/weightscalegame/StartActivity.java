package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        // NCMB 初期設定
        NCMB.initialize(this.getApplicationContext(), this.APP_KEY, this.CLIENT_KEY);
    }

    @OnClick(R.id.start_layout)
    public void onTap() {
        // ログインしていればホームへ
        if (NCMBUser.getCurrentUser().isAuthenticated()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
    @OnClick(R.id.logoutButton)
    void a (){
        NCMBUser.logoutInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {

            }
        });

    }
}
