package com.hlb.haolaoban.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by heky on 2017/11/13.
 */

public class AudioRecordUtils {

    //文件路径
    private String filePath;

    private String folderPath;
    private MediaRecorder mMediaRecorder;

    public static final int MAX_LENGTH = 1000 * 60;// 最大录音时长1000*60;

    public static final int MIX_LENGTH = 1500;

    private OnAudioStatusUpdateListener audioUpdateListtener;

    private long startTime;
    private long endTime;

    private Context context;

    public AudioRecordUtils(Context context) {
        //默认保存路径为/sdcard/hlb/record/下
        this(Environment.getExternalStorageDirectory() + "/hlb/record/", context);
    }

    public AudioRecordUtils(String filePath, Context context) {
        this.folderPath = filePath;
        this.context = context;
    }

    /*开始录音*/
    public void startRecord() {
        // 开始录音
        /* 1. 实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        try {
            mMediaRecorder.reset();
               /* 2. 设置麦克风*/
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /*设置录音文件编码*/
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        /*设置录音文件格式*/
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            filePath = folderPath + getCurrentTime() + ".amr";
        /*3. 准备录音*/
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            startTime = System.currentTimeMillis();
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mMediaRecorder.release();
        }
        try {
                /*4. 开始录音*/
            mMediaRecorder.start();
            updateMicStatus();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /*停止录音*/
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            audioUpdateListtener.onStop(filePath);
            filePath = "";
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            filePath = "";

        }
        long time = endTime - startTime;
        if (time < MIX_LENGTH) {
            Toast.makeText(context, "录音时间过短!", Toast.LENGTH_SHORT).show();
            cancelRecord();
        } else if (time > MAX_LENGTH) {
            Toast.makeText(context, "录音时间不能超过一分钟!", Toast.LENGTH_SHORT).show();
            audioUpdateListtener.onStop(filePath);
            cancelRecord();
        }
        return time;
    }

    /*取消录音*/
    public void cancelRecord() {
        if (mMediaRecorder == null) {
            return;
        }
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

        } catch (IllegalStateException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";
    }


    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioUpdateListtener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioUpdateListtener) {
                    audioUpdateListtener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }


    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        void onStop(String filePath);

    }


    public String getCurrentTime() {
        String currentTime;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH);//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        currentTime = year + "" + month + "" + day + "" + hour + "" + minute + "" + second;
        return currentTime;
    }

}
