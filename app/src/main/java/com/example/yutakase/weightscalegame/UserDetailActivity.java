package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
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
public class UserDetailActivity extends AppCompatActivity implements FindCallback<NCMBObject> {

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
    @Bind(R.id.imageView)
    ImageView image;
    @Bind(R.id.record_button)
    Button recordButton;

    // DataBase関係
    @BindString(R.string.open_user_data_class)
    String openUserData;
    @BindString(R.string.user_name_key)
    String userNameKey;
    @BindString(R.string.offset_weight_key)
    String offsetWeightKey;

    // 表示関係
    @BindString(R.string.goal_text)
    String goalText;
    @BindString(R.string.kg)
    String kgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = getIntent().getStringExtra(userNameKey);
        if (!this.userName.equals(NCMBUser.getCurrentUser().getUserName())) {
            this.recordButton.setVisibility(View.INVISIBLE);
        }

        // Toolbarに名前を表示
        setTitle(userName + "さんの部屋");
//        setTitle("てらだ" + "さんの部屋");

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
        TextAppearanceSpan Big = new TextAppearanceSpan(this, R.style.myTextAppearance);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(goalText);
        int start = sb.length();
        double offset = this.myData.getDouble(this.offsetWeightKey);
//        double offset = 3.1;
        String stringFormat = String.format("%.1f", offset);
        sb.append(stringFormat);
        sb.setSpan(Big, start, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append(this.kgText);

        String goalText = this.goalText + stringFormat + this.kgText;
        this.goalTextView.setText(sb);
        String runningText = "最後に測った日 " + Util.getDate(this.myData.getString("updateDate"), this);
//        String runningText = "最後に測った日  7/29 ";
        this.runningTextView.setText(runningText);

        this.showProgress(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.add(0, 0, 0, "Logout");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            NCMBUser.logoutInBackground(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    Snackbar.make(myPageLayout, "log out.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent intent = new Intent(getApplication(), StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.record_button)
    void onClickRecordButton() {
        startActivity(new Intent(this, MyRecordActivity.class));
    }

    // データ受信終了時に呼ばれる
    @Override
    public void done(List<NCMBObject> list, NCMBException e) {
        if (e != null && list == null) {
            //errorだったらActivity閉じちゃう
            finish();
        } else if (list.size() == 0) {
            finish();
        } else {
            // 合致データは一つしか無いので0番めを取得
            this.myData = list.get(0);
            final int avaterId = this.myData.getInt("resourceId");
            //取得したアバターIDを使ってアバターを表示
            NCMBQuery<NCMBObject> query = new NCMBQuery<>("WeightLog");
            query.whereEqualTo("userName", userName);
            query.addOrderByDescending("createDate");
            query.findInBackground(new FindCallback<NCMBObject>() {
                @Override
                public void done(List<NCMBObject> list, NCMBException e) {
                    if (e != null && list == null) {
                        finish();
                    } else if (list.size() == 0) {
                        finish();
                    } else {
                        int imageId = ViewUtil.getImageByWeight(list.get(0).getDouble("weight"), myData.getDouble("startWeight"), myData.getDouble("goalWeight"), avaterId);
                        image.setImageResource(imageId);
                        buildLayout();
                    }
                }
            });
        }
    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, this.myPageLayout, this.progressView, show);
    }
}
