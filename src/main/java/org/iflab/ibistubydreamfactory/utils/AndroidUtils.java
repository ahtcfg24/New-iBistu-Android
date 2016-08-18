package org.iflab.ibistubydreamfactory.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * 管理安卓本身的一些组件
 */
public class AndroidUtils {
    /**
     * 隐藏输入法键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (null == activity) {
            return;
        }
        if (null != activity.getCurrentFocus() && null != activity.getCurrentFocus()
                                                                  .getWindowToken()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
}
