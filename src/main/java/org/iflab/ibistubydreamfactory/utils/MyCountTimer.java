package org.iflab.ibistubydreamfactory.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import cn.edu.bistu.mission.R;

/**
 * 计时器类，用于显示验证码倒计时
 */
public class MyCountTimer extends CountDownTimer {
    private TextView button;//Button是TextView的子类，所以可以不声明成Button button
    private Context context;

    /**
     * @param millisInFuture 计时总时间
     * @param countDownInterval 倒计时变化间隔
     * @param button 传入的按钮对象
     */
    public MyCountTimer(long millisInFuture, long countDownInterval, TextView button, Context context) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        this.context = context;
    }


    /**
     * 倒计时开始
     */
    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);//设置不能点击
        button.setText(millisUntilFinished / 1000 + "秒后可重新发送");//设置显示倒计时时间
        button.setBackgroundResource(R.drawable.bg_button_gray);//设置背景为暗灰色
        button.setTextColor(context.getResources().getColor(R.color.colorSixSixBlack));//设置文字颜色为暗灰色
    }


    @Override
    public void onFinish() {
        button.setText("重新获取验证码");
        button.setBackgroundResource(R.drawable.bg_button);//设置背景为原来背景
        button.setTextColor(context.getResources().getColor(R.color.colorPureWhite));//设置文字颜色为原来的纯白色
        button.setClickable(true);//重新获得点击


    }


}