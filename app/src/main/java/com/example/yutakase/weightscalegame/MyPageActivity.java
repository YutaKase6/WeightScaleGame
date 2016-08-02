package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自分の情報を表示する画面
 */
public class MyPageActivity extends AppCompatActivity implements FindCallback<NCMBObject> {

    String userName;
    NCMBObject myData;

    // UI reference
    @Bind(R.id.goal_text_view)
    TextView goalTextView;
    @Bind(R.id.running_text_view)
    TextView runningTextView;
    @Bind(R.id.progress)
    View progressView;
    @Bind(R.id.my_page_layout)
    View myPageLayout;

    // DataBase関係
    @BindString(R.string.open_user_data_class)
    String openUserData;
    @BindString(R.string.user_name_key)
    String userNameKey;
    @BindString(R.string.offset_weight_key)
    String offsetWeightKey;
    @BindString(R.string.running_days_key)
    String runningDaysKey;

    // 表示関係
    @BindString(R.string.goal_text)
    String goalText;
    @BindString(R.string.kg)
    String kgText;
    @BindString(R.string.running_before_text)
    String runningBeforeText;
    @BindString(R.string.running_after_text)
    String runningAfterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toolbarに名前を表示
        NCMBUser user = NCMBUser.getCurrentUser();
        userName = user.getUserName();
        setTitle(userName + "さんの部屋");

        // 自分のデータを取得
        // TODO: 2016/08/03 onCreateの度に通信走るのはあかんかも、結局どんだけデータベースと一貫性とるか
        NCMBQuery<NCMBObject> query = new NCMBQuery<>(this.openUserData);
        query.whereEqualTo(this.userNameKey, this.userName);
        this.showProgress(true);
        query.findInBackground(this);
    }

    // レイアウト成形
    private void buildLayout() {
        // 各々データを使って表示
        String goalText = this.goalText + this.myData.getDouble(this.offsetWeightKey) + this.kgText;
        this.goalTextView.setText(goalText);
        String runningText = this.runningBeforeText + this.myData.getInt(this.runningDaysKey) + this.runningAfterText;
        this.runningTextView.setText(runningText);

        this.showProgress(false);
    }

    @OnClick(R.id.record_button)
    void onClickRecordButton() {
        startActivity(new Intent(this, MyRecordActivity.class));
    }

    // データ受信終了時に呼ばれる
    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        if (e != null) {
            //errorだったらActivity閉じちゃう
            finish();
        } else {
            // 合致データは一つしか無いので0番めを取得
            this.myData = list.get(0);
            this.buildLayout();
        }
    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, this.myPageLayout, this.progressView, show);
    }
}
