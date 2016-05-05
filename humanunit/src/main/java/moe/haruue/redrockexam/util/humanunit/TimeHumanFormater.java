package moe.haruue.redrockexam.util.humanunit;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class TimeHumanFormater {

    /**
     * 将时间差格式化成人类可读格式
     * @param context {@link android.app.Activity} 的 this 引用
     * @param interval 时间差，秒数，正数将被格式化成“???后”，负数将会被格式化成“???前”
     * @return 格式化好的时间差字符串
     */
    public static String formatTimeInterval(Context context, long interval) {
        Resources resources = context.getResources();
        //前还是后
        String format;
        if (interval < 0) {
            format = resources.getString(R.string.times_ago_format);
            interval = -interval;
        } else {
            format = resources.getString(R.string.times_after_format);
        }
        //时间显示
        String timeString;
        if (interval < 10) {
            if (format.equals(resources.getString(R.string.times_ago_format))) {
                return resources.getString(R.string.just_now);
            } else {
                return resources.getString(R.string.soon);
            }
        } else if (interval < 60) {
            timeString = interval + resources.getString(R.string.seconds);
        } else if (interval < 3600) {
            timeString = interval / 60 + 1 + resources.getString(R.string.minutes);
        } else if (interval < 86400) {
            timeString = interval / 3600 + resources.getString(R.string.hours);
        } else if (interval < 604800) {
            timeString = interval / 86400 + resources.getString(R.string.days);
        } else if (interval < 2592000) {
            timeString = interval / 608400 + resources.getString(R.string.weeks);
        } else if (interval < 31536000) {
            timeString = interval / 2592000 + resources.getString(R.string.months);
        } else {
            timeString = interval / 31536000 + resources.getString(R.string.years);
        }
        return String.format(format, timeString);

    }

}
