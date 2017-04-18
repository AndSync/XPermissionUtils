package com.andsync.xpermissionutils;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.andsync.xpermissionutils.permission.XPermissionUtils;

/**
 * Desc:Activity基类
 * Author：LiZhimin
 * Date：2016/12/7 18:51
 * Version V1.0
 * Copyright © 2016 LiZhimin All rights reserved.
 */
public class BaseActivity extends AppCompatActivity {

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
