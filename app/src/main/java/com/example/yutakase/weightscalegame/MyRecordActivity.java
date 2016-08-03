package com.example.yutakase.weightscalegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 自分の過去のデータを表示する画面
 */
public class MyRecordActivity extends AppCompatActivity {

    @Bind(R.id.goal_weight)
    TextView goalWeight;
    @Bind(R.id.today_weight)
    TextView todayWeight;
    @Bind(R.id.last_weight)
    TextView lastWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_record);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NCMBUser user = NCMBUser.getCurrentUser();//ユーザ情報取得
        Double goalWeightValue = user.getDouble("goalWeight");//ユーザ目標体重取得
        goalWeight.setText(goalWeightValue.toString());

        NCMBQuery<NCMBObject> query = new NCMBQuery<>("WeightLog");
        query.addOrderByDescending("createDate");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                Double todayWeightValue = list.get(0).getDouble("weight");
                String stringFormat = String.format("%1$.1f", todayWeightValue);
                todayWeight.setText(stringFormat);
                Double lastWeightValue = list.get(1).getDouble("weight");
                stringFormat = String.format("%1$.1f", lastWeightValue);
                lastWeight.setText(stringFormat);
            }
        });
    }

}
