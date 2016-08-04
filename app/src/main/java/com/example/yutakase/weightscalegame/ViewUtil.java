package com.example.yutakase.weightscalegame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.nifty.cloud.mb.core.NCMBUser;

import java.io.FileNotFoundException;

/**
 * View関係の共通処理の関数群
 * <p/>
 * Created by yutakase on 2016/08/03.
 */
public final class ViewUtil {

    private ViewUtil() {
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(Context context, final View HideView, final View progressView, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            HideView.setVisibility(show ? View.GONE : View.VISIBLE);
            HideView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    HideView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            HideView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 増減体重に応じて画像のリソースIDを返すメソッド
     * Created by takeshishimizu on 2016/08/03.
     */
    public static int getImageByWeight(double currentWeight, int avatarId) throws FileNotFoundException {
        int[][] resources = {{R.drawable.chara1pudgy, R.drawable.chara1normal, R.drawable.chara1ideal, R.drawable.chara1gaunt},
                {R.drawable.chara2pudgy, R.drawable.chara2normal, R.drawable.chara2ideal, R.drawable.chara2gaunt},
                {R.drawable.chara3pudgy, R.drawable.chara3normal, R.drawable.chara3ideal, R.drawable.chara3gaunt},
                {R.drawable.chara4pudgy, R.drawable.chara4normal, R.drawable.chara4ideal, R.drawable.chara4gaunt},
                {R.drawable.chara5pudgy, R.drawable.chara5normal, R.drawable.chara5ideal, R.drawable.chara5gaunt}};

        NCMBUser user = NCMBUser.getCurrentUser();
        double startWeight = user.getDouble("startWeight");
        double goalWeight = user.getDouble("goalWeight");
        double standardWeight = (startWeight - goalWeight) / 3;
        double marginWeight = currentWeight - goalWeight;
//        double standardWeight = 3;
//        double marginWeight = -2;
//        avatarId = 4;

        int index = 0;
        if(marginWeight > standardWeight) {
            //デブ
            index = 0;
        }else if(marginWeight <= standardWeight && marginWeight >= 0) {
            //ふつう
            index = 1;
        }else if(0 >= marginWeight && marginWeight >= -standardWeight) {
            //ちょいやせ
            index = 2;
        }else {
            //やせ
            index = 3;
        }

        //int resourceId = context.getResources().getIdentifier(imageName, "Drawable", context.getPackageName());;
        int resourceId = resources[avatarId-1][index];
        //エラー処理
        if(resourceId == 0) {
            throw new FileNotFoundException("アバターを読み込めませんでした");
        }
        return resourceId;
    }
}
