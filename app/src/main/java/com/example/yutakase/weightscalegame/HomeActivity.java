package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
import com.nifty.cloud.mb.core.NCMBUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ホーム画面
 */
public class HomeActivity extends AppCompatActivity {

    private String userName;
    private String leftFriendName;
    private String rightFriendName;

    private int ascyncCount = 0;

    @Bind(R.id.progress)
    View progressView;
    @Bind(R.id.home_layout)
    View homeLayout;
    @Bind(R.id.avatar_text_view)
    TextView avatarTextView;
    @Bind(R.id.right_friend_text_view)
    TextView rightFriendTextView;
    @Bind(R.id.left_friend_text_view)
    TextView leftFriendTextView;
    @Bind(R.id.avatar)
    ImageView avatarImageView;
    @Bind(R.id.left_friend)
    ImageView leftFriendImageView;
    @Bind(R.id.right_friend)
    ImageView rightFriendsImageView;

    @BindString(R.string.user_name_key)
    String userNameKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        this.showProgress(true);

        //ユーザー名の取得
        NCMBUser ncmb = NCMBUser.getCurrentUser();
        userName = ncmb.getUserName();

        this.avatarTextView.setText(this.userName);

        try {
            JSONArray friendsNameList = ncmb.getJSONObject("friends").getJSONArray("friends");
            this.leftFriendName = friendsNameList.getString(0);
            this.rightFriendName = friendsNameList.getString(1);
            this.leftFriendTextView.setText(this.leftFriendName);
            this.rightFriendTextView.setText(this.rightFriendName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.setAvator(this.userName, this.avatarImageView);
        this.setAvator(this.leftFriendName, this.leftFriendImageView);
        this.setAvator(this.rightFriendName, this.rightFriendsImageView);
    }

    @OnClick(R.id.avatar_touch_view)
    public void onTapAvatar(View v) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(userNameKey, userName);
        startActivity(intent);
    }

    @OnClick(R.id.left_friend_touch_view)
    public void onTapLeftFriend(View v) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(userNameKey, leftFriendName);
        startActivity(intent);
    }

    @OnClick(R.id.right_friend_touch_view)
    public void onTapRightFriend(View v) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(userNameKey, rightFriendName);
        startActivity(intent);
    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, this.homeLayout, this.progressView, show);
    }

    private void setAvator(final String userName, final ImageView imageView) {
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("openUserData");
        query.whereEqualTo("userName", userName);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> list, NCMBException e) {
                if (e != null && list == null) {
                    finish();
                } else if (list.size() == 0) {
                    finish();
                } else {
                    final int avatarId = list.get(0).getInt("resourceId");
                    final double startWeight = list.get(0).getDouble("startWeight");
                    final double goalWeight = list.get(0).getDouble("goalWeight");
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
                                int imageId = ViewUtil.getImageByWeight(list.get(0).getDouble("weight"), startWeight, goalWeight, avatarId);
                                imageView.setImageResource(imageId);
                                if (ascyncCount == 2) {
                                    showProgress(false);
                                } else {
                                    ascyncCount++;
                                }
                            }
                        }
                    });

                }
            }
        });
    }
}
