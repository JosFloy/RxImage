package com.josfloy.rximage;

import android.content.Context;
import android.util.Log;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public class RequestCreator {
    private MemoryCacheObservable mMemoryCacheObservable;
    private DiskCacheObservable mDiskCacheObservable;
    private NetWorkCacheObservable mNetWorkCacheObservable;

    public RequestCreator(Context context) {
        mMemoryCacheObservable = new MemoryCacheObservable();
        mDiskCacheObservable = new DiskCacheObservable(context);
        mNetWorkCacheObservable = new NetWorkCacheObservable();
    }

    public Observable<Image> getImageFromMemory(String url) {
        return mMemoryCacheObservable.getImage(url)
                .filter(new Func1<Image, Boolean>() {
                    @Override
                    public Boolean call(Image image) {
                        return image != null;
                    }
                })
                .doOnNext(new Action1<Image>() {
                    @Override
                    public void call(Image image) {
                        Log.d("RequestCreator", "call: get data from memory");
                    }
                });
    }

    public Observable<Image> getImageFromDisk(String url) {
        return mDiskCacheObservable.getImage(url)
                .filter(new Func1<Image, Boolean>() {
                    @Override
                    public Boolean call(Image image) {
                        return image != null;
                    }
                })
                .doOnNext(new Action1<Image>() {
                    @Override
                    public void call(Image image) {
                        mMemoryCacheObservable.putDataToCache(image);
                    }
                });
    }

    public Observable<Image> getImageFromNetWork(String url) {
        return mNetWorkCacheObservable.getImage(url)
                .filter(new Func1<Image, Boolean>() {
                    @Override
                    public Boolean call(Image image) {
                        return image != null;
                    }
                })
                .doOnNext(new Action1<Image>() {
                    @Override
                    public void call(Image image) {
                        mMemoryCacheObservable.putDataToCache(image);
                        mDiskCacheObservable.putDataToCache(image);
                    }
                });
    }
}
