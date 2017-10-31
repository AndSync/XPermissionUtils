package com.andsync.xpermissionutils.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.andsync.xpermission.XPermissionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取位置工具类
 *
 * @author AndSync
 * @date 2017/10/30
 * Copyright © 2014-2017 AndSync All rights reserved.
 */
public class LocationUtils {
    private static final String TAG = "LocationUtil";

    public static void requestLocation(final Context context) {
        XPermissionUtils.requestPermissions(context, RequestCode.LOCATION, new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        }, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //6.0以下这个无法明确判断是否获取位置权限
                startLocation(context);
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions, boolean alwaysDenied) {
                Toast.makeText(context, "位置权限获取失败", Toast.LENGTH_SHORT).show();
                if (alwaysDenied) {
                    DialogUtil.showPermissionManagerDialog(context, "位置");
                }
            }
        });
    }

    private static void startLocation(Context context) {
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers == null) {
            return;
        }

        //获取Location
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //对提供者进行排序，gps、net、passive
        List<String> providerSortList = new ArrayList<>();
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS_PROVIDER");
            providerSortList.add(LocationManager.GPS_PROVIDER);
        }
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Log.d(TAG, "NETWORK_PROVIDER");
            providerSortList.add(LocationManager.NETWORK_PROVIDER);
        }
        if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            Log.d(TAG, "PASSIVE_PROVIDER");
            providerSortList.add(LocationManager.PASSIVE_PROVIDER);
        }
        String locationProvider = "";
        for (int i = 0; i < providerSortList.size(); i++) {
            String provider = providerSortList.get(i);
            Log.d(TAG, "正在尝试：" + provider);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                locationProvider = provider;
                Log.d(TAG, "定位成功：" + provider);
                saveLocation(location);
                break;
            } else {
                Log.d(TAG, "定位失败：" + provider);
            }
        }
        if (!TextUtils.isEmpty(locationProvider)) {
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        }
    }

    static LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            saveLocation(location);
        }
    };

    /**
     * 保存地理位置经度和纬度信息
     */
    private static void saveLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(TAG, "latitude:" + latitude);
            Log.d(TAG, "longitude:" + longitude);
        }
    }
}
