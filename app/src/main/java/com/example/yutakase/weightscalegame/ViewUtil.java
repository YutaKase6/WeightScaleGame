package com.example.yutakase.weightscalegame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.nifty.cloud.mb.core.NCMBUser;

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
    public static int getImageByWeight(double currentWeight, double startWeight, double goalWeight, int avatarId) {

        int[][] resources = {{R.drawable.c1_1, R.drawable.c1_2, R.drawable.c1_3, R.drawable.c1_4},
                {R.drawable.c2_1, R.drawable.c2_2, R.drawable.c2_3, R.drawable.c2_4},
                {R.drawable.c3_1, R.drawable.c3_2, R.drawable.c3_3, R.drawable.c3_4},
                {R.drawable.c4_1, R.drawable.c4_2, R.drawable.c4_3, R.drawable.c4_4},
                {R.drawable.c5_1, R.drawable.c5_2, R.drawable.c5_3, R.drawable.c5_4}
        };

        double marginWeight = startWeight - goalWeight;

        int index;
        if (startWeight - (marginWeight * 2) / 3 < currentWeight) {
            index = 3;
        } else if (goalWeight - marginWeight / 3 > currentWeight) {
            index = 0;
        } else if (goalWeight < currentWeight) {
            index = 2;
        } else {
            index = 1;
        }
        Log.e("", "" + index);
        //int resourceId = context.getResources().getIdentifier(imageName, "Drawable", context.getPackageName());;
        return resources[avatarId - 1][index];
    }
}
