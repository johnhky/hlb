package com.hlb.haolaoban.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by heky on 2017/10/30.
 */

public class Utils {
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /*将px值转换为dip或dp值，保证尺寸大小不变*/
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /*将dip或dp值转换为px值，保证尺寸大小不变*/
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean isMobile(String number) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /* * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /*手机号码中间四位转为*号*/
    public static String phoneToAsterisk(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7, 11);
    }

    /**
     * 将Bitmap转换成字符串,图像进行Base64编码 bitmap 原图
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    public static boolean serviceIsWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfo = myManager.getRunningServices(30);
        for (int i = 0; i < serviceInfo.size(); i++) {
            if (serviceInfo.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}