package com.josfloy.rximage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public class NetWorkCacheObservable extends CacheObservable {
    @Override
    public Image getDataFromCache(String url) {
        Bitmap bitmap = downloadImage(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(Image image) {

    }

    private Bitmap downloadImage(String url) {
        // TODO: 2019/2/15 0015  从网络下载的部分重复了，最好能在结构上优化而不是重用
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            final URLConnection con = new URL(url).openConnection();
            inputStream = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
