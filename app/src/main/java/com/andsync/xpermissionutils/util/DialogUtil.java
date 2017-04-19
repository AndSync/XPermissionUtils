package com.andsync.xpermissionutils.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.andsync.xpermissionutils.R;

/**
 * Desc:
 * Author：LiZhimin
 * Date：2016/12/7 20:16
 * Version V1.0
 * Copyright © 2016 LiZhimin All rights reserved.
 */
public class DialogUtil {

    public static void showPermissionManagerDialog(final Context context, String str) {
        new AlertDialog.Builder(context).setTitle("获取" + str + "权限被禁用")
            .setMessage(
                "请在 设置-应用管理-" + context.getString(R.string.app_name) + "-权限管理 (将" + str + "权限打开)")
            .setNegativeButton("取消", null)
            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
            })
            .show();
    }

    public static void showLocServiceDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("手机未开启位置服务")
            .setMessage("请在 设置-系统安全-位置信息 (将位置服务打开))")
            .setNegativeButton("取消", null)
            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        intent.setAction(Settings.ACTION_SETTINGS);
                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            })
            .show();
    }
}
