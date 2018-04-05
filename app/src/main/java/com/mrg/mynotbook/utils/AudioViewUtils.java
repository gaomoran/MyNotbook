package com.mrg.mynotbook.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mrg.mynotbook.R;

/**
 * Created by MrG on 2017-11-19.
 */

public class AudioViewUtils implements View.OnTouchListener {
    private Button button;
    private float oldY;
    private float newY;
    private View thisView;
    private Context context;
    private PopupWindow popupWindow;
    private Runnable runnable;
    private int audioTimeS = 0;
    private int audioTimeM = 0;
    private TextView tv_audio_time;
    private TextView showInfo;
    private View view;
    private Handler handler;
    private AudioListener audioListener;

    public AudioViewUtils(Context context, Button button, View contentview) {
        this.context = context;
        this.button=button;

        thisView = contentview;
        initView();
        initPopuWindow();
        initTime();

    }

    private void initPopuWindow() {

        popupWindow = new PopupWindow(context);
        popupWindow.setTouchable(false);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private void initView() {
        view = View.inflate(context, R.layout.pupowindow_audio, null);
        tv_audio_time = (TextView) view.findViewById(R.id.tv_pop_audio_time);
        showInfo = (TextView) view.findViewById(R.id.tv_pop_audio_showInfo);
        this.button.setOnTouchListener(this);
        handler = new Handler();
    }

    private void initTime() {
        runnable = new Runnable() {
            @Override
            public void run() {

                audioTimeS++;
                if (audioTimeS < 60) {

                    if (audioTimeM < 10 && audioTimeS < 10)
                        tv_audio_time.setText("0" + audioTimeM + ":0" + audioTimeS);
                    else if (audioTimeM < 10 && audioTimeS >= 10)
                        tv_audio_time.setText("0" + audioTimeM + ":" + audioTimeS);
                    else if (audioTimeM >= 10 && audioTimeS < 10)
                        tv_audio_time.setText(audioTimeM + ":0" + audioTimeS);
                    else
                        tv_audio_time.setText(audioTimeM + ":" + audioTimeS);

                } else {
                    audioTimeM++;
                    audioTimeS = 0;
                    if (audioTimeM < 10 && audioTimeS < 10)
                        tv_audio_time.setText("0" + audioTimeM + ":0" + audioTimeS);
                    else if (audioTimeM < 10 && audioTimeS >= 10)
                        tv_audio_time.setText("0" + audioTimeM + ":" + audioTimeS);
                    else if (audioTimeM >= 10 && audioTimeS < 10)
                        tv_audio_time.setText(audioTimeM + ":0" + audioTimeS);
                    else
                        tv_audio_time.setText(audioTimeM + ":" + audioTimeS);

                }

                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN://录音按钮按下时
                //记录坐标
                oldY = event.getY();
                showInfo.setText("松开保存，向上滑动取消");
                //开始录音
                audioListener.startRecordAudio();
                //弹出正在录音小窗口
                popupWindow.showAtLocation(thisView, Gravity.CENTER, 0, 0);
                //计时器启动
                handler.postDelayed(runnable, 1000);
                break;

            case MotionEvent.ACTION_UP:
                if (Math.abs(oldY - newY) < 50) {
                    audioListener.saveRecordAudio();
                    audioTimeS = 0;
                    audioTimeM = 0;
                    colseAudioTime();
                } else if (Math.abs(oldY - newY) > 300) {
                    audioListener.cancelRecordAudio();
                    audioTimeS = 0;
                    audioTimeM = 0;
                    colseAudioTime();
                }
                popupWindow.dismiss();
                break;
            case MotionEvent.ACTION_MOVE:
                newY = event.getY();
                if (Math.abs(oldY - newY) > 300) {
                 showInfo.setText("放开手指取消录音");
                }else if (Math.abs(oldY - newY) <50){
                    showInfo.setText("松开保存，向上滑动取消");
                }
                break;

        }


        return true;

    }

    private void colseAudioTime() {
        handler.removeCallbacks(runnable);
        tv_audio_time.setText("00:00");
    }

    public void setAudioListener(AudioListener audioListener){
        this.audioListener=audioListener;
    }

    public interface AudioListener{
        void startRecordAudio();
        void saveRecordAudio();
        void cancelRecordAudio();
    }
}
