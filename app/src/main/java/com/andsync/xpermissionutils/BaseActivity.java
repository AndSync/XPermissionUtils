package com.andsync.xpermissionutils;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.andsync.xpermission.XPermissionUtils;

/**
 * Activity基类
 *
 * @author AndSync
 * @date 2017/10/30
 * Copyright © 2014-2017 AndSync All rights reserved.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XPermissionUtils.clear();
    }
}
