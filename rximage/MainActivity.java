package com.josfloy.rximage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.btn_get);
        mImageView = findViewById(R.id.iv_show);

        // /        RxView.clicks(mButton).subscribe(new Action1<Void>() {
        //            @Override
        //            public void call(Void aVoid) {
        //   /*             RxImageLoader
        //                        .with(MainActivity.this)
        //                        .load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1498134779,1894016748&fm=173&app=25&f=JPEG?w=600&h=337&s=F13A30D44069510723821458030080B8")
        //                        .into(mImageView);*/
        //
        //                Picasso.with(MainActivity.this)
        //                        .load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1498134779,1894016748&fm=173&app=25&f=JPEG?w=600&h=337&s=F13A30D44069510723821458030080B8")
        //                        .into(mImageView);
        //            }
        //        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Picasso.with(MainActivity.this)
                        .load("https://ss2.baidu.==lkljkljl/6ONYsjip0QIZ8tyhnq/it/u=1498134779,1894016748&fm=173&app=25&f=JPEG?w=600&h=337&s=F13A30D44069510723821458030080B8")
                        .into(mImageView);*/
                RxImageLoader
                        .with(MainActivity.this)
                        .load("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1498134779,1894016748&fm=173&app=25&f=JPEG?w=600&h=337&s=F13A30D44069510723821458030080B8")
                        .into(mImageView);
            }
        });
    }
}
