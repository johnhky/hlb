package com.hlb.haolaoban.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.LoginActivity;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/1.
 */

public class DialogUtils {
    static Dialog dialog;

    public static void showExitDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null);
        TextView tv_exit = (TextView) view.findViewById(R.id.tv_exit);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(view);
        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(i);
                ((Activity) v.getContext()).finish();
                dialog.cancel();
                Hawk.deleteAll();
            }
        });
    }

    public static void showLoading(Context context, String msg) {
        dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.3); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(view);
        dialog.show();
        tv_msg.setText(msg);
    }

    public static void hideLoading() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
