package org.iflab.ibistubydreamfactory.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证工具类，用于验证各种输入
 */
public class RegexConfirmUtils {
    /**
     * Email正则表达式
     */
    public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 手机号码正则表达式
     */
    public static final String MOBILE = "^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";
    /**
     * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
     */
    public static final String ENG_NUM = "^[A-Za-z0-9]+$";
    /**
     * 过滤特殊字符串正则
     * regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
     */

    public static final String NO_SPECIAL_STR = "[^~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]{1,}";

    /**
     * 判断字段不为空 符合返回true
     *
     * @return boolean
     */
    public static synchronized boolean isNotNull(String str) {
        return null != str && str.trim().length() > 0;
    }


    /**
     * 判断字段是否为Email 符合返回true
     *
     * @return boolean
     */
    public static boolean isEmail(String str) {
        return Regular(str, EMAIL);
    }


    /**
     * 判断是否为手机号码 符合返回true
     *
     * @return boolean
     */
    public static boolean isMobile(String str) {
        return Regular(str, MOBILE);
    }


    /**
     * 判断字符串长度是否正确
     *
     * @param str 字符串
     * @param min 最短长度
     * @param max 最长长度
     */
    public static boolean isLengthRight(String str, int min, int max) {
        return str.trim().length() > min && str.trim().length() < max;
    }

    /**
     * 判断字符串是不是全部是英文字母和数字
     *
     * @return boolean
     */
    public static boolean isEnglishOrNumber(String str) {
        return Regular(str, ENG_NUM);
    }


    /**
     * 不包含包含特殊字符
     */
    public static boolean isNotContainsSpecial(String str) {
        return Regular(str, NO_SPECIAL_STR);
    }

    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     *
     * @param str 匹配的字符串
     * @param pattern 匹配模式
     * @return boolean
     */
    private static boolean Regular(String str, String pattern) {
        if (null == str || str.trim().length() <= 0) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
