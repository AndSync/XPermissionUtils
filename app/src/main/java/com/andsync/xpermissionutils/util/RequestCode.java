package com.andsync.xpermissionutils.util;

/**
 * Desc:请求码
 * Author：LiZhimin
 * Date：2016/12/7 18:10
 * Version V1.0
 * Copyright © 2016 LiZhimin All rights reserved.
 */
public interface RequestCode {
    int PHONE = 0x00;//电话
    int LOCATION = 0x01;//位置
    int CAMERA = 0x02;//相机
    int AUDIO = 0x04;//语音
    int EXTERNAL = 0x08;//存储
    int MORE = 0x10;//多个
}