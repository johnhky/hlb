package com.hlb.haolaoban.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Button;

import com.hlb.haolaoban.R;

/**
 * Created by heky on 2017/11/3.
 */

public class TimeCountUtil extends CountDownTimer {
    private Context context;
    private Button btn;

    public TimeCountUtil(Context context, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.btn = btn;
    }

    @Override
    public void onFinish() {
        btn.setText("重新发送验证码");
        btn.setBackgroundResource(R.drawable.shape_health_bloodfat);
        btn.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
        btn.setText(millisUntilFinished / 1000 + "s");
        btn.setBackground(context.getResources().getDrawable(R.drawable.shape_uncheck));
        Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
        span.setSpan(R.color.white, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    }
}