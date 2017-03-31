# XPermissionUtils
可能是最精简的Android6.0运行时权限处理方式
## 使用方式
以打开相机为例
#### 1、首先`AndroidManifest`中配置必要的权限
`<uses-permission android:name="android.permission.CAMERA"/>`
#### 2、在基类中加上回调方法
```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        XPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```
#### 3、调用工具类方法
(1) 拒绝后无提示
  ```
XPermissionUtils.requestPermissions(Context context, int requestCode, String[] permissions, OnPermissionListener listener)
```
(2) 拒绝后再次申请给出提示
  ```
XPermissionUtils.requestPermissions(Context context, int requestCode, String[] permissions, OnPermissionListener listener, RationaleHandler handler)
```
这里主要注意这个Context必需是一个Activity

如果在Activity中可以传`this`;

如果在Fragment中传`getActivity()`;

如果在View中传`getContext()`;

等等.....
```
private void doOpenCamera() {
        XPermissionUtils.requestPermissions(this, RequestCode.CAMERA, new String[]{Manifest
                        .permission.CAMERA}
                , new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (PermissionHelper.isCameraEnable()) {
                            Toast.makeText(MainActivity.this, "打开相机操作", Toast.LENGTH_LONG).show();
                        } else {
                            DialogUtil.showPermissionManagerDialog(MainActivity.this, "相机");
                        }
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(context, "获取相机权限失败", Toast.LENGTH_SHORT).show();
                        if (XPermissionUtils.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                            DialogUtil.showPermissionManagerDialog(MainActivity.this, "相机");
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
```
#### 4、一次申请多个权限
用户可能部分拒绝，因此在`onPermissionDenied(String[] deniedPermissions)`回调中返回了请求结果中所有被拒绝的权限，用户可用于比对判断出哪些权限被拒绝，给用户明确的提示
```
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
                    DialogUtil.showPermissionManagerDialog(MainActivity.this, sBuider.toString());
                }
            }
        });
    }
```

# 特别鸣谢
[MPermissionUtils](https://github.com/Airsaid/MPermissionUtils)

[PermissionGen](https://github.com/lovedise/PermissionGen)

[AndPermission](https://github.com/yanzhenjie/AndPermission)

# License
```text
Copyright 2017 Lizhimin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
