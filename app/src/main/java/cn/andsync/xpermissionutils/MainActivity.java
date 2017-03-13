package cn.andsync.xpermissionutils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.andsync.xpermissionutils.permission.XPermissionUtils;
import cn.andsync.xpermissionutils.util.DialogUtil;
import cn.andsync.xpermissionutils.util.LocationUtils;
import cn.andsync.xpermissionutils.util.PermissionHelper;
import cn.andsync.xpermissionutils.util.RequestCode;

/**
 * Desc:演示页面
 * Author：LiZhimin
 * Date：2016/12/7 18:51
 * Version V1.0
 * Copyright © 2016 LiZhimin All rights reserved.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_call;
    private Button bt_call2;
    private Button bt_record;
    private Button bt_camera;
    private Button bt_location;
    Context context;
    private Button bt_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        bt_call = (Button) findViewById(R.id.bt_call);
        bt_call2 = (Button) findViewById(R.id.bt_call2);
        bt_record = (Button) findViewById(R.id.bt_record);
        bt_camera = (Button) findViewById(R.id.bt_camera);
        bt_location = (Button) findViewById(R.id.bt_location);
        bt_more = (Button) findViewById(R.id.bt_more);
        setListener();
    }

    private void setListener() {
        bt_call.setOnClickListener(this);
        bt_call2.setOnClickListener(this);
        bt_record.setOnClickListener(this);
        bt_camera.setOnClickListener(this);
        bt_location.setOnClickListener(this);
        bt_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_call:
                doCallPhone();
                break;
            case R.id.bt_call2:
                doCallPhone2();
                break;
            case R.id.bt_record:
                doRecord();
                break;
            case R.id.bt_camera:
                doOpenCamera();
                break;
            case R.id.bt_location:
                doStartLocation();
                break;
            case R.id.bt_more:
                doMorePermission();
                break;
            default:
                break;
        }
    }

    private void doMorePermission() {
        XPermissionUtils.requestPermissions(this, RequestCode.MORE, new String[]{Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_SMS}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "获取联系人,短信权限成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                StringBuilder sBuider = new StringBuilder();
                for (String deniedPermission : deniedPermissions) {
                    if (deniedPermission.equals(Manifest.permission.WRITE_CONTACTS)) {
                        sBuider.append("联系人");
                        sBuider.append(",");
                    }
                    if (deniedPermission.equals(Manifest.permission.READ_SMS)) {
                        sBuider.append("短信");
                        sBuider.append(",");
                    }
                }
                if (sBuider.length() > 0) sBuider.deleteCharAt(sBuider.length() - 1);
                Toast.makeText(context, "获取" + sBuider.toString() + "权限失败", Toast.LENGTH_SHORT).show();
                if (XPermissionUtils.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                    DialogUtil.showAlertDialog(MainActivity.this, sBuider.toString());
                }
            }
        });
    }

    /**
     * 拨打电话
     */
    private void doCallPhone() {
        XPermissionUtils.requestPermissions(this, RequestCode.PHONE, new String[]{Manifest.permission
                .CALL_PHONE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10010"));
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                Toast.makeText(context, "获取拨打电话权限失败", Toast.LENGTH_SHORT).show();
                if (XPermissionUtils.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                    DialogUtil.showAlertDialog(MainActivity.this, "拨打电话");
                }
            }
        });
    }

    /**
     * 跳转拨号页面
     */
    private void doCallPhone2() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:10010");
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 录音
     */
    private void doRecord() {
        XPermissionUtils.requestPermissions(this, RequestCode.AUDIO, new
                        String[]{Manifest.permission.RECORD_AUDIO}
                , new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (PermissionHelper.isAudioEnable()) {
                            Toast.makeText(MainActivity.this, "开始录音操作", Toast.LENGTH_LONG).show();

                        } else {
                            DialogUtil.showAlertDialog(MainActivity.this, "录音或麦克风");
                        }
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(context, "获取录音或麦克风权限失败", Toast.LENGTH_SHORT).show();
                        if (XPermissionUtils.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                            DialogUtil.showAlertDialog(MainActivity.this, "录音或麦克风");
                        }
                    }
                });
    }

    /**
     * 相机
     */
    private void doOpenCamera() {
        XPermissionUtils.requestPermissions(this, RequestCode.CAMERA, new String[]{Manifest
                        .permission.CAMERA}
                , new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (PermissionHelper.isCameraEnable()) {
                            Toast.makeText(MainActivity.this, "打开相机操作", Toast.LENGTH_LONG).show();
                        } else {
                            DialogUtil.showAlertDialog(MainActivity.this, "相机");
                        }
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(context, "获取相机权限失败", Toast.LENGTH_SHORT).show();
                        if (XPermissionUtils.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                            DialogUtil.showAlertDialog(MainActivity.this, "相机");
                        }
                    }
                }, new XPermissionUtils.RationaleHandler() {

                    @Override
                    protected void showRationale() {
                        new AlertDialog.Builder(context)
                                .setTitle("温馨提示")
                                .setMessage("我们需要相机权限才能正常使用该功能")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("验证权限", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissionsAgain();
                                    }
                                }).show();
                    }
                });
    }

    /**
     * 获取位置
     */
    private void doStartLocation() {
        if (!PermissionHelper.isLocServiceEnable(this)) {
            DialogUtil.showLocServiceDialog(this);
            return;
        }
        LocationUtils.requestLocation(this);
    }


}
