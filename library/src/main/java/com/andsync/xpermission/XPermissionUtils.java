/*
 * Copyright © 2017 AndSync All rights reserved.
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
package com.andsync.xpermission;

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
 * Android 6.0运行时权限处理 <a href="https://github.com/AndSync/XPermissionUtils">
 *
 * @author AndSync
 * @date 2017/10/30
 * Copyright © 2014-2017 AndSync All rights reserved.
 */
public class XPermissionUtils {

    private static int sRequestCode = -1;
    private static OnPermissionListener sListener;

    public interface OnPermissionListener {
        /**
         * 授权成功
         */
        void onPermissionGranted();

        /**
         * 请求权限失败
         *
         * @param deniedPermissions 被拒绝的权限集合
         * @param alwaysDenied 拒绝后是否提示
         */
        void onPermissionDenied(String[] deniedPermissions, boolean alwaysDenied);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionsAgain(@NonNull Context context, @NonNull String[] permissions,
        @NonNull int requestCode) {
        if (context instanceof Activity) {
            ((Activity) context).requestPermissions(permissions, requestCode);
        } else {
            throw new IllegalArgumentException("Context must be an Activity");
        }
    }

    public static void requestPermissions(@NonNull Context context, @NonNull int requestCode,
        @NonNull String[] permissions, OnPermissionListener listener) {
        sRequestCode = requestCode;
        sListener = listener;
        String[] deniedPermissions = getDeniedPermissions(context, permissions);
        if (deniedPermissions.length > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsAgain(context, permissions, requestCode);
        } else {
            if (sListener != null) {
                sListener.onPermissionGranted();
            }
        }
    }

    /**
     * 请求权限结果，对应Activity中onRequestPermissionsResult()方法。
     */
    public static void onRequestPermissionsResult(@NonNull Activity context, int requestCode,
        @NonNull String[] permissions, int[] grantResults) {
        if (sRequestCode != -1 && requestCode == sRequestCode) {
            if (sListener != null) {
                String[] deniedPermissions = getDeniedPermissions(context, permissions);
                if (deniedPermissions.length > 0) {
                    boolean alwaysDenied = hasAlwaysDeniedPermission(context, permissions);
                    sListener.onPermissionDenied(deniedPermissions, alwaysDenied);
                } else {
                    sListener.onPermissionGranted();
                }
            }
        }
    }

    /**
     * 获取请求权限中需要授权的权限
     */
    private static String[] getDeniedPermissions(@NonNull Context context, @NonNull String[] permissions) {
        List<String> deniedPermissions = new ArrayList();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    /**
     * 是否彻底拒绝了某项权限
     */
    private static boolean hasAlwaysDeniedPermission(@NonNull Context context, @NonNull String... deniedPermissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        boolean rationale;
        for (String permission : deniedPermissions) {
            rationale = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission);
            if (!rationale) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        sListener = null;
        sRequestCode = -1;
    }
}