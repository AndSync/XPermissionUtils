package com.andsync.xpermissionutils.util;

import android.content.Context;
import android.hardware.Camera;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Desc:可能会有用的一些方法
 * Author：LiZhimin
 * Date：2016/12/7 18:10
 * Version V1.0
 * Copyright © 2016 LiZhimin All rights reserved.
 */
public class PermissionHelper {
    /**
     * 6.0以下判断是否开启录音权限
     */
    public static boolean isAudioEnable() {
        boolean isValid = true;
        AudioRecord mRecorder;
        int bufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        short[] mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            mRecorder.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            isValid = false;
            return isValid;
        } finally {

        }
        int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
        if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
            // 做正常的录音处理
        } else {
            //录音可能被禁用了，做出适当的提示
            isValid = false;
        }
        // 停止录制
        try {
            // 防止某些手机崩溃，例如联想
            if (mRecorder != null) {
                // 停止
                mRecorder.stop();
                mRecorder.release();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    /**
     * 6.0以下判断是否开启相机权限
     */
    public static boolean isCameraEnable() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

}
