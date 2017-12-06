package com.hlb.haolaoban.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static void showToastLong(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
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
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
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

    public static String getTime(long time) {
        long minute = 0;
        long second = 0;
        second = time / 1000;
        if (second > 60) {
            minute = second / 60;
        }
        return getTwoLength(minute) + ":" + getTwoLength(second);
    }

    private static String getTwoLength(long data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return data + "";
        }
    }

    public static void mkDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /*
   * 将时间戳转换为年月日
   */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
        /*将时间戳转化为月日*/
    public static String stampToDates(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为月日
     */
    public static String stampToMonth(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*将时间戳转换为日期格式的字符串*/
    public static String formatData(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = format.format(new Date(timeStamp));
        return result;
    }

    public static long timeToStamp(String timeString) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = sdf.parse(timeString);
            timeStamp = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}