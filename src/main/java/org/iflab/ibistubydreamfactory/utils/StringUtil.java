package org.iflab.ibistubydreamfactory.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 */
public class StringUtil {
    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

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

    /**
     * 获得距离零点的时间
     */
    public static String getToZeroTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long zeroTimeCreate = sdf.parse(time).getTime();//创建当天的零点毫秒数
            long timeNow = System.currentTimeMillis() + 28800000;//现在的毫秒数（北京时间+8小时）
            long timeDistance = timeNow - zeroTimeCreate;
            if (timeDistance < 86400000) {//如果现在的毫秒数比创建当天的零点毫秒多24小时
                time = "今天" + time.substring(11, 16);//今天+时间
            } else if (timeDistance >= 86400000 && timeDistance <= 172800000) {//如果小于48小时，但是多于24小时
                time = "昨天" + time.substring(11, 16);//昨天+时间
            } else {
                time = time.substring(0, 10);//只显示日期
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 计算并获取云信短信CheckSum
     */
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    /**
     * 计算并获取md5值
     */
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    /**
     * 生成指定长度的随机a-z0-9的字符串
     *
     * @param length 指定长度
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);//[0,62)

            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获得当前UTC时间（秒）
     */
    public static String getUTCSecond() {
        Date date = new Date();
        return date.getTime() / 1000 + "";
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }
}
