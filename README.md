# # XPermissionUtils
可能是最精简的Android6.0运行时权限处理方式，支持Rationale提示，只有一个类，100行代码，所有弹窗等操作由用户自行处理，在Demo中也有提供代码示例<br/>

[我的博客地址](http://www.jianshu.com/p/4a60b064a0ab)
## 流程图
![](https://github.com/AndSync/XPermissionUtils/blob/master/art/Flow%20Chart.png)
## 引入方式
1、gradle引入
```
dependencies {
    compile 'com.andsync.xpermission:XPermissionUtils:1.2'
}
```
2、Maven引入
```
<dependency>
  <groupId>com.andsync.xpermission</groupId>
  <artifactId>XPermissionUtils</artifactId>
  <version>1.2</version>
  <type>pom</type>
</dependency>
```
## 使用方式
以打开相机为例
#### 1、首先`AndroidManifest`中配置必要的权限
`<uses-permission android:name="android.permission.CAMERA"/>`
#### 2、在基类中加上回调方法
```java
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```
#### 3、调用工具类方法
**注意**
* requestPermissions第一个参数必须为activity
* XPermissionUtils.OnPermissionListener监听中有两个回调方法<br/>
&nbsp;&nbsp;&nbsp;&nbsp;`onPermissionGranted()`授权成功后的回调<br/>
&nbsp;&nbsp;&nbsp;&nbsp;`onPermissionDenied(final String[] deniedPermissions, boolean alwaysDenied)`授权失败后的回调<br/>
&nbsp;&nbsp;&nbsp;&nbsp;其中deniedPermissions标识被拒绝的权限alwaysDenied标识是否永远被拒绝
* 申请权限由String数组决定，如`new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}`

```java
private void doOpenCamera() {
        XPermissionUtils.requestPermissions(this, RequestCode.CAMERA, new String[] { Manifest.permission.CAMERA },
            new XPermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    if (PermissionHelper.isCameraEnable()) {
                        Toast.makeText(MainActivity.this, "打开相机操作", Toast.LENGTH_LONG).show();
                    } else {
                        DialogUtil.showPermissionManagerDialog(MainActivity.this, "相机");
                    }
                }

                @Override
                public void onPermissionDenied(final String[] deniedPermissions, boolean alwaysDenied) {
                    Toast.makeText(context, "获取相机权限失败", Toast.LENGTH_SHORT).show();
                    if (alwaysDenied) { // 拒绝后不再询问 -> 提示跳转到设置
                        DialogUtil.showPermissionManagerDialog(MainActivity.this, "相机");
                    } else {    // 拒绝 -> 提示此公告的意义，并可再次尝试获取权限
                        new AlertDialog.Builder(context).setTitle("温馨提示")
                            .setMessage("我们需要相机权限才能正常使用该功能")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("验证权限", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    XPermissionUtils.requestPermissionsAgain(context, deniedPermissions,
                                        RequestCode.CAMERA);
                                }
                            })
                            .show();
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
Copyright 2017 AndSync

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
