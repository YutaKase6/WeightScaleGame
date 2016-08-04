package com.example.yutakase.weightscalegame;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;

/**
 * Created by yutakase on 2016/08/04.
 */
public class Util {

    // Dateを日付(M/D)に変換
    private static Time sTime = new Time();
    public static String getDate(String time, Context context){
        sTime.parse3339(time);
        long mills = sTime.toMillis(false);
        return DateUtils.formatDateTime(context,mills,DateUtils.FORMAT_NUMERIC_DATE);
    }
}
