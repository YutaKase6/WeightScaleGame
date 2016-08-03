package com.example.yutakase.weightscalegame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
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
     * 増減体重に応じて画像を返すメソッド
     * 画像のリソースIDを返却
     * Created by takeshishimizu on 2016/08/03.
     */
    public static int getImageByWeight(double currentWeight, int avatarId, Context context) throws FileNotFoundException {
        NCMBUser user = NCMBUser.getCurrentUser();
        double startWeight = user.getDouble("startWeight");
        double goalWeight = user.getDouble("goalWeight");
        double standardWeight = (startWeight - goalWeight) / 3;
        double marginWeight = currentWeight - goalWeight;

        String imageName = "chara" + avatarId;
        if(marginWeight > standardWeight) {
            //デブ
            imageName += "pudgy";
        }else if(marginWeight <= standardWeight && marginWeight >= 0) {
            //ふつう
            imageName += "normal";
        }else if(0 >= marginWeight && marginWeight >= -standardWeight) {
            //ちょいやせ
            imageName += "ideal";
        }else {
            //やせ
            imageName += "gaunt";
        }

        int resourceId = context.getResources().getIdentifier(imageName, "Drawable", context.getPackageName());;
        //エラー処理
        if(resourceId == 0) {
            throw new FileNotFoundException("アバターを読み込めませんでした");
        }
        return resourceId;
    }
}
