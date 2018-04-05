package com.mrg.mynotbook.presenter.impi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.mrg.mynotbook.R;
import com.mrg.mynotbook.config.MyNoteBookConfig;
import com.mrg.mynotbook.model.DiaryModel;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.impi.ImpiDiaryModel;
import com.mrg.mynotbook.presenter.EditDiaryListenter;
import com.mrg.mynotbook.presenter.EditDiaryPresenter;
import com.mrg.mynotbook.ui.EditDiaryView;
import com.mrg.mynotbook.utils.AudioRecoderUtils;
import com.mrg.mynotbook.utils.DataTimeUtils;

import com.mrg.mynotbook.utils.FileUtils;

import com.mrg.mynotbook.utils.LocationUtils;

import java.io.File;
import java.text.ParseException;
import java.util.List;

/**
 * Created by MrG on 2017-05-10.
 */
public class ImpiEditDiaryPresenter extends BroadcastReceiver implements EditDiaryListenter, EditDiaryPresenter {
    public static final String TAG = "mrg";
    private EditDiaryView editDiaryView;
    private DiaryModel diaryModel;
    private Context context;
    private final LocationUtils locationUtils;
    private Activity activity;
    private final AudioRecoderUtils audioRecoderUtils;
    private String filepath;

    private final FileUtils fileUtils;


    public ImpiEditDiaryPresenter(EditDiaryView editDiaryView, Context context, Activity activity) {
        this.activity = activity;
        this.editDiaryView = editDiaryView;
        diaryModel = new ImpiDiaryModel(context);
        this.context = context;
        locationUtils = new LocationUtils();
        audioRecoderUtils = new AudioRecoderUtils();

        fileUtils = new FileUtils();

    }

    @Override
    public void onClickListener(View view) {

        switch (view.getId()) {

            case R.id.btn_editDiary_collection://收藏按钮
                editDiaryView.updataCollection();
                break;
            case R.id.btn_editDiary_mood://心情按钮

                editDiaryView.showChooseMood();
                break;
            case R.id.btn_editDiary_weather://天气
                editDiaryView.showChooseWeather();
                break;
            case R.id.ib_editDiary_location://定位
                editDiaryView.checkLocation();

                break;
            case R.id.ib_editDiary_more://更多
                editDiaryView.showMore();
                break;
            case R.id.ib_editDiary_phone://加入图片

                editDiaryView.showChooseGetImageDialog();

                break;
            case R.id.ib_editDiary_voice://加入语音
                editDiaryView.addVoice();
                break;
            case R.id.ppw_mood_bad://选择坏心情
                editDiaryView.setMood(202);
                break;
            case R.id.ppw_mood_commonly://选择一般心情
                editDiaryView.setMood(201);
                break;
            case R.id.ppw_mood_happy://选择好心情
                editDiaryView.setMood(200);
                break;
            case R.id.ppw_weather_sunny://选择晴
                editDiaryView.setWeather(300);
                break;
            case R.id.ppw_weather_cloudy://选择阴
                editDiaryView.setWeather(301);
                break;
            case R.id.ppw_weather_rain://选择雨
                editDiaryView.setWeather(302);
                break;
            case R.id.ppw_weather_snow://选择雪
                editDiaryView.setWeather(303);
                break;
            case R.id.ed_editDiary_title://标题栏
                editDiaryView.closeViewUtils();
                break;
            default:
                break;

        }

    }

    @Override
    public void startRecordAudio() {
        filepath = audioRecoderUtils.startRecord();
    }

    @Override
    public void saveRecordAudio() {
        long time = audioRecoderUtils.stopRecord();
        if (filepath != null) {
            try {

                String s = DataTimeUtils.longToString(time, "mm:ss");

                Log.i(TAG, "saveRecordAudio: 录音时长"+s);
                filepath=filepath+"#"+s;
            } catch (ParseException e) {
                filepath=filepath+"#"+"未知时长";
            }
            editDiaryView.addVoiceInEditText(filepath);
        }

    }

    @Override
    public void cancelRucordAudio() {
        audioRecoderUtils.cancelRecord();
    }

    @Override
    public void closeViewUtils() {
        editDiaryView.closeViewUtils();
    }

    @Override
    public void initView(Intent intent) {
        //打开一篇日记
        if (intent.getStringExtra("Type").equals("show")) {

            String id = intent.getStringExtra("Id");
            //根据id获得日记
            Diary diary = diaryModel.getDiary(id);
            //设置星期
            editDiaryView.setWeek(diary.getWeek());
            //设置几号
            editDiaryView.setDay(diary.getDay());
            //设置月份
            editDiaryView.setMonth(diary.getMonth());
            //设置内容
            editDiaryView.setDiaryContent(diary.getNumber());
            //设置标题
            editDiaryView.setDiaryTitle(diary.getTitle());
            //设置收藏
            editDiaryView.setCollection(diary.getCollection());
            //设置心情
            editDiaryView.setMood(diary.getMood());
            //设置天气
            editDiaryView.setWeather(diary.getWeather());
            //设置定位
            editDiaryView.setLocation(diary.getLocation());

        } else {
            //新建一篇日记
            String nowDate = DataTimeUtils.getNowDate();
            String date = nowDate.replace("年", "/");
            //设置星期
            editDiaryView.setWeek(DataTimeUtils.getWeek(nowDate));
            //设置几号
            editDiaryView.setDay(nowDate.substring(nowDate.indexOf("月") + 1, nowDate.indexOf("日")));
            //设置月份
            editDiaryView.setMonth(date.substring(0, date.indexOf("月")));
        }
    }

    @Override
    public void saveDiary(final String id, final String title, final String number, final String month, final String day, final String week, final int collection, final int mood, final int weather, final String position, final String who, final String location) {

        new Thread() {
            @Override
            public void run() {
                Diary diary = new Diary();
                diary.setId(id);
                diary.setTitle(title);
                diary.setNumber(number);
                diary.setMonth(month);
                diary.setDay(day);
                diary.setWeek(week);
                diary.setCollection(collection);
                diary.setMood(mood);
                diary.setWeather(weather);
                diary.setPhoto("");
                diary.setLocation(location);
                String[] split = number.split("☆");
                for (String s : split) {
                    if (s.contains("★")) {
                        String replace = s.replace("★", "");
                        diary.setPhoto(replace);
                        break;
                    }

                }
                Intent intent = new Intent();

                if (id.equals("new")) {
//                    Log.i(TAG, "run: 保存新建日记");
                    diaryModel.addDiary(diary, who);
                    intent.setAction("com.mrg.myNoteBook.UI.ADD_DIARY_DATA");
                    intent.putExtra("Who", who);
                    context.sendBroadcast(intent);
                } else {
                    diaryModel.setDiary(diary, id);
                    intent.setAction("com.mrg.myNoteBook.UI.UP_DIARY_DATA");
                    intent.putExtra("Id", id);
                    intent.putExtra("Position", position);
                    context.sendBroadcast(intent);
                }


            }
        }.start();

    }

    @Override
    public Uri saveImage(String fileName, Context context) {

       return fileUtils.saveImage(fileName,context);

    }

    @Override
    public void addImage(String uri) {
        editDiaryView.addPhone(uri);
    }

    @Override

    public void loaction() {
        editDiaryView.showInfo("正在定位...");

        locationUtils.startLoaction(context);

    }

    @Override

    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            //定位成功
            case "com.mrg.myNoteBook.LOCATION.LOCATION_SUCCESS":
                String location = intent.getStringExtra("Location");
                editDiaryView.setLocation(location);
                locationUtils.stopLocation();
                editDiaryView.showInfo("定位成功！");
                break;
            //定位失败
            case "com.mrg.myNoteBook.LOCATION.LOCATION_LOSE":
                String info = intent.getStringExtra("Location");
                editDiaryView.showInfo(info);
                locationUtils.stopLocation();
                break;
            case "com.mrg.myNoteBook.VIEW.CLOSE_VIEW_UTILS"://关闭工具
                editDiaryView.closeViewUtils();
                break;

        }

    }
}
