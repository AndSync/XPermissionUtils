# XPermissionUtils
可能是最精简的Android6.0运行时权限处理方式
#使用方式
以拨打电话为例
1、首先`AndroidManifest`中配置必要的权限
`<uses-permission android:name="android.permission.CALL_PHONE"/>`
2、在基类中加上回调方法
```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        XPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```
3、调用方法
  ```
XPermissionUtils.requestPermissions(Context context, int requestCode, String[] permissions, OnPermissionListener listener)
```
这里主要注意这个Context必需是一个Activity
如果在Activity中可以传`this`;
如果在Fragment中传`getActivity()`;
如果在View中传`getContext()`;
等等.....
```
    private void doCallPhone() {
        XPermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission
                .CALL_PHONE}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10010"));
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied() {
                //弹出权限被禁用的提示框
            }
        });
    }
```

#特别鸣谢
[MPermissionUtils ](https://github.com/Airsaid/MPermissionUtils )
[PermissionGen](https://github.com/lovedise/PermissionGen )
