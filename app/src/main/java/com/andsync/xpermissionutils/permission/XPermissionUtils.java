/*
 * Copyright © 2017 LiZhimin All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andsync.xpermissionutils.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:Android 6.0运行时权限处理工具类
 * Author：LiZhimin
 * Date：2017/3/13 18:10
 * Version V1.0
 * Link:<a href="https://github.com/AndSync/XPermissionUtils"></>
 * Copyright © 2017 LiZhimin All rights reserved.
 */
public class XPermissionUtils {

    private static final int ILLEGAL_REQUEST_CODE = -1;
    private static int mRequestCode = ILLEGAL_REQUEST_CODE;
    private static OnPermissionListener mOnPermissionListener;

    public interface OnPermissionListener {

        void onPermissionGranted();

        void onPermissionDenied(String[] deniedPermissions, boolean alwaysDenied);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(@NonNull Context context, @NonNull int requestCode,
        @NonNull String[] permissions, OnPermissionListener listener) {
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Context must be an Activity");
        }
        mRequestCode = requestCode;
        mOnPermissionListener = listener;
        String[] deniedPermissions = getDeniedPermissions(context, permissions);
        if (deniedPermissions.length > 0) {
            ((Activity) context).requestPermissions(deniedPermissions, requestCode);
        } else {
            if (mOnPermissionListener != null) mOnPermissionListener.onPermissionGranted();
        }
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(@NonNull Activity context, int requestCode,
        @NonNull String[] permissions, int[] grantResults) {
        if (mRequestCode == ILLEGAL_REQUEST_CODE) {
            throw new IllegalArgumentException("illegal request code");
        }
        if (requestCode == mRequestCode) {
            if (mOnPermissionListener != null) {
                String[] deniedPermissions = getDeniedPermissions(context, permissions);
                if (deniedPermissions.length > 0) {
                    boolean alwaysDenied = hasAlwaysDeniedPermission(context, permissions);
                    mOnPermissionListener.onPermissionDenied(deniedPermissions, alwaysDenied);
                } else {
                    mOnPermissionListener.onPermissionGranted();
                }
            }
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private static String[] getDeniedPermissions(@NonNull Context context,
        @NonNull String[] permissions) {
        List<String> deniedPermissions = new ArrayList();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否彻底拒绝了某项权限
     * 此方法单独调用无意义
     * 在Activity.onRequestPermissionsResult()判读有denied调用
     * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0916/3464.html
     */
    private static boolean hasAlwaysDeniedPermission(@NonNull Context context,
        @NonNull String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
            if (!rationale) return true;
        }
        return false;
    }
}