/*
 * Copyright (C) 2023-2023 The Gongliangyi Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jack.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

/**
 * @author gongliangyi 1465306392@qq.com
 * @since 2023-02-25 22:14
 */
public class PictureUtils {
    private static final String TAG = "PictureUtils";

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        if (activity == null) {
            Log.e(TAG, "activity is null!");
            return null;
        }
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        if ((destWidth == 0) || (destHeight == 0)) {
            Log.e(TAG, "destWidth or destHeight is 0!");
            return null;
        }
        // 读取图片位置
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // 计算压缩图片
        int inSampleSize = 1;
        if ((srcHeight > destHeight) || (srcWidth > destWidth)) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(Math.max(heightScale, widthScale));
        }

        Log.e(TAG, "inSampleSize is : " + inSampleSize);

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }
}
