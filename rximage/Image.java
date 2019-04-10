package com.josfloy.rximage;

import android.graphics.Bitmap;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public class Image {
    private String url;
    private Bitmap mBitmap;

    public Image(String url, Bitmap bitmap) {
        this.url = url;
        mBitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
