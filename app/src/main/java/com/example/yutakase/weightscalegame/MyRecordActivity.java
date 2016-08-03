package com.example.yutakase.weightscalegame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 自分の過去のデータを表示する画面
 */
public class MyRecordActivity extends AppCompatActivity {
    @Bind(R.id.progress)
    View progress;
    @Bind(R.id.textViewLayout)
    View textViewLayout;
    @Bind(R.id.graph)
    BarGraph graph;
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

        showProgress(true);
        NCMBUser user = NCMBUser.getCurrentUser();//ユーザ情報取得
        setTitle(user.getUserName() + "さんの記録");
        double goalWeightValue = user.getDouble("goalWeight");//ユーザ目標体重取得
        goalWeight.setText("" + goalWeightValue);

        NCMBQuery<NCMBObject> query = new NCMBQuery<>("WeightLog");
        query.whereEqualTo("userName", user.getUserName());
        query.addOrderByDescending("createDate");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                if (e != null) {
                    setTitle(e.getMessage());
                }
                double todayWeightValue = list.get(0).getDouble("weight");
                String stringFormat = String.format("%1$.1f", todayWeightValue);
                todayWeight.setText(stringFormat);
                double lastWeightValue = list.get(1).getDouble("weight");
                stringFormat = String.format("%1$.1f", lastWeightValue);
                lastWeight.setText(stringFormat);

                ArrayList<Bar> bars = new ArrayList<>();
                for (int i = 6; i >= 0; i--) {
                    Bar bar = new Bar();
                    bar.setColor(Color.BLUE);
                    double weightValue = list.get(i).getDouble("weight");
                    bar.setValue((float) weightValue);
                    String date = list.get(i).getString("createDate");
                    bar.setName(date);
                    bar.setValueString(weightValue + "kg");
                    bars.add(bar);
                }
                graph.setBars(bars);
                showProgress(false);
            }
        });
    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, textViewLayout, progress, show);
    }
}
