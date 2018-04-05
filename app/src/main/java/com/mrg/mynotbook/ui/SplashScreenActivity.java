package com.mrg.mynotbook.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.mrg.mynotbook.MainActivity;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.app.BaseActivity;

import com.xiaomi.ad.SplashAdListener;
import com.xiaomi.ad.adView.SplashAd;


import java.util.Timer;
import java.util.TimerTask;


public class SplashScreenActivity extends Activity {

    private static final String POSITION_ID = "667a3567ebd139e43872fcf533badf39";
    private ViewGroup viewGroup;
    private Timer timer;
    private TimerTask timerTask;
    private String TAG="mrg";
    private boolean isOpen=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewGroup = (ViewGroup) findViewById(R.id.splash_ad_container);
        timer = new Timer();
        timerTask = getTimerTask();
        SplashAd splashAd = new SplashAd(this, viewGroup, R.drawable.default_splash, new SplashAdListener() {
            @Override
            public void onAdPresent() {
                // 开屏广告展示
            }

            @Override
            public void onAdClick() {
                //用户点击了开屏广告
            }

            @Override
            public void onAdDismissed() {
                //这个方法被调用时，表示从开屏广告消失。
                Log.i(TAG, "onAdDismissed: 正常进入");
                isOpen=true;
                gotoActivity();
            }

            @Override
            public void onAdFailed(String s) {
                //调用失败
                Log.i(TAG, "onAdFailed: 打开广告失败后进入");
                timerStart();
            }
        }

        );
        splashAd.requestAd(POSITION_ID);


    }
    private void gotoActivity(){
        if (isOpen) {
            Intent view = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(view);
            finish();
        }
    }
    private TimerTask getTimerTask(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                isOpen=true;
                gotoActivity();
            }
        };
        return timerTask;
    }
    private void timerStart(){
        timerTask.cancel();
        timerTask = getTimerTask();
        timer.schedule(timerTask, 2350);

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: 后台运行");
        isOpen=false;
        timerTask.cancel();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (!isOpen){
            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
                    isOpen=true;
                    gotoActivity();
                }
            };
            timer.schedule(timerTask,2000);
        }

        super.onRestart();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            if (viewGroup.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


}
