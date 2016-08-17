package org.iflab.ibistubydreamfactory.utils;

import java.text.DecimalFormat;

/**
 *
 */
public class StringUtil {
    /**
     * 把秒转换成时间格式
     *
     * @param time 秒
     */
    public static String transToTime(int time) {
        DecimalFormat decimalFormat = new DecimalFormat("00");

        int h = time / 3600;
        int m = (time - h * 3600) / 60;
        int s = (time - h * 3600) % 60;
        String hh = decimalFormat.format(h);
        String mm = decimalFormat.format(m);
        String ss = decimalFormat.format(s);

        return hh + ":" + mm + ":" + ss;
    }
}
