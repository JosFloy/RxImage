package com.josfloy.rximage;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public class MemoryCacheObservable extends CacheObservable {
    private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int maxSize = maxMemory / 8;

    LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(maxSize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    };

    @Override
    public Image getDataFromCache(String url) {
        Bitmap bitmap = mLruCache.get(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(Image image) {
        mLruCache.put(image.getUrl(), image.getBitmap());
    }
}
