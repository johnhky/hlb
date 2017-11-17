package com.hlb.haolaoban.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hlb.haolaoban.MyApplication;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.account.LoginActivity;
import com.hlb.haolaoban.databinding.DialogConsactClubBinding;
import com.hlb.haolaoban.databinding.DialogPayBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.module.ApiModule;
import com.orhanobut.hawk.Hawk;

/**
 * Created by heky on 2017/11/1.
 */

public class DialogUtils {
    static Dialog dialog;

    public interface OnDialogItemClickListener {
        void onItemClick(int which);
    }

    public static void showPickPhotoDialog(View mView, final Activity context, final OnDialogItemClickListener onItemClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pick_picture, null);
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(1);
                popupWindow.dismiss();
            }
        });
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(2);
                popupWindow.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

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
            public void onClick(final View v) {
                Intent i = new Intent();
                i.setClass(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(i);
                ((Activity) v.getContext()).finish();
                dialog.cancel();
                Hawk.delete(Constants.TOKEN);
                Hawk.delete(Constants.USER_PROFILE);
                Hawk.delete(Constants.MID);
            }
        });
    }

    /*显示加载框*/
    public static void showLoading(String msg) {
        dialog = new Dialog(MyApplication.mContext, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(MyApplication.mContext).inflate(R.layout.dialog_loading, null);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) MyApplication.mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.3); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(view);
        dialog.show();
        tv_msg.setText(msg);
    }

    public static void showRemindMsg(Context context, String msg, final OnDialogItemClickListener onDialogItemClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        DialogPayBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_pay, null, false);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.tvTitle.setText(msg);
        binding.tvContent.setText(msg);
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogItemClickListener.onItemClick(1);
                dialog.dismiss();
            }
        });
    }

    public static void showConsactClub(Context context, String msg, final OnDialogItemClickListener itemClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        DialogConsactClubBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_consact_club, null, false);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.tvMsg.setText("联系俱乐部:" + msg);
        binding.etCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.etSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(1);
                dialog.dismiss();
            }
        });
    }

    public static void showPayDialog(final Activity context, View mView) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_topay, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });

    }

    public static void hideLoading() {
        if (null != dialog) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }
}
