package com.josfloy.rximage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public class DiskCacheObservable extends CacheObservable {
    private DiskLruCache mDiskLruCache;
    private Context mContext;
    private int maxSize = 20 * 1024 * 1024;

    public DiskCacheObservable(Context context) {
        mContext = context;
        initDiskLruCache();
    }

    @Override
    public Image getDataFromCache(String url) {
        Bitmap bitmap = getDataFromDiskLruCache(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }

        return null;
    }

    @Override
    public void putDataToCache(final Image image) {
        Observable.create(new Observable.OnSubscribe<Image>() {
            @Override
            public void call(Subscriber<? super Image> subscriber) {
                putDataToDiskLruCache(image);
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }

    private void initDiskLruCache() {
        try {
            File cacheDir = DiskCacheUtil
                    .getDiskCacheDir(this.mContext, "image_cache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            int versionCode = DiskCacheUtil.getAppVersionCode(mContext);
            mDiskLruCache = DiskLruCache.open(cacheDir, versionCode, 1, maxSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getDataFromDiskLruCache(String url) {
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapshot = null;

        try {
            //生成图片URL对应的key
            final String key = DiskCacheUtil.getMd5String(url);
            //查找key对应的缓存
            snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            //将缓存数据解析成Bitmap对象
            Bitmap bitmap = null;
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void putDataToDiskLruCache(Image image) {
        try {
            //第一步：获取将要缓存的图片的对应唯一key值
            String key = DiskCacheUtil.getMd5String(image.getUrl());
            //第二步：获取DiskLruCache的Editor
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                //第三步：从Editor中获取OutputStream
                OutputStream outputStream = editor.newOutputStream(0);
                //第四步：下载网络图片且保存至DiskLruCache图片缓存中
                boolean isSuccessful = download(image.getUrl(), outputStream);
                if (isSuccessful) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean download(String urlString, OutputStream outputStream) {
        // TODO: 2019/2/15 0015  1和2
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
