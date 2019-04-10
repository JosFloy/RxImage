package com.josfloy.rximage;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jos on 2019/2/14 0014.
 */
public abstract class CacheObservable {

    public Observable<Image> getImage(final String url) {
        return Observable.create(new Observable.OnSubscribe<Image>() {
            @Override
            public void call(Subscriber<? super Image> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Image image = getDataFromCache(url);
                    subscriber.onNext(image);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public abstract Image getDataFromCache(String url);

    public abstract void putDataToCache(Image image);
}
