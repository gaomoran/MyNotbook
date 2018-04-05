package com.mrg.mynotbook.ui;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrg.mynotbook.R;
import com.mrg.mynotbook.app.BaseActivity;

import com.mrg.mynotbook.app.OpenAlbumActivity;
import com.mrg.mynotbook.config.MyNoteBookConfig;
import com.mrg.mynotbook.presenter.impi.ImpiEditDiaryPresenter;
import com.mrg.mynotbook.utils.AudioViewUtils;
import com.mrg.mynotbook.utils.StringUtils;
import com.mrg.mynotbook.widget.RichEditorText;
import com.mrg.mynotbook.widget.SoftInputCoordinatorLayout;




public class EditDiaryActivity extends OpenAlbumActivity implements EditDiaryView, View.OnClickListener, View.OnFocusChangeListener, AudioViewUtils.AudioListener {

    private final static int PHOTO = 111;
    private final static int ALBUM = 222;
    public static final String TAG = "mrg";
    private int COLLECTION = 100;

    private int MOOD = 200;
    private int WEATHER = 300;

    private TextView tv_day;
    private TextView tv_month;
    private TextView tv_week;
    private Button btn_mood;
    private Button btn_weather;
    private Button btn_collection;
    private ImageButton ib_location;
    private ImageButton ib_more;
    private ImageButton ib_phone;
    private ImageButton ib_voice;
    private EditText ed_title;

    private RichEditorText ed_content;

    private ImpiEditDiaryPresenter impiEditDiaryPresenter;
    private StringUtils stringUtils;
    private String fileName;
    private View ppw_mood;
    private View ppw_weather;
    private Button btn_mood_bad;
    private Button btn_modd_commonly;
    private PopupWindow popupwindowMood;
    private PopupWindow popupWindowWeather;
    private Button btn_mood_happy;
    private Button btn_weather_sunny;
    private Button btn_weather_cloudy;
    private Button btn_weather_rain;
    private Button btn_weather_snow;
    private Button btn_back;
    private TextView tv_location;
    private RelativeLayout viewUtils;
    private boolean isOpenViewUtils = false;
    private RelativeLayout audioView;

    public Button btn_audio_recoder;
    private View ppw_audio;
    private SoftInputCoordinatorLayout thisView;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        //初始化控件
        initView();
        //设置点击事件
        initOnClickListener();
        stringUtils = new StringUtils();
        impiEditDiaryPresenter = new ImpiEditDiaryPresenter(this, this, this);
        impiEditDiaryPresenter.initView(getIntent());


        AudioViewUtils audioViewUtils = new AudioViewUtils(this, btn_audio_recoder, thisView);
        audioViewUtils.setAudioListener(this);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mrg.myNoteBook.LOCATION.LOCATION_SUCCESS");
        intentFilter.addAction("com.mrg.myNoteBook.LOCATION.LOCATION_LOSE");
        intentFilter.addAction("com.mrg.myNoteBook.VIEW.CLOSE_VIEW_UTILS");
        registerReceiver(impiEditDiaryPresenter, intentFilter);

    }

    private void initOnClickListener() {
        btn_collection.setOnClickListener(this);
        btn_mood.setOnClickListener(this);
        btn_weather.setOnClickListener(this);
        ib_location.setOnClickListener(this);
        ib_more.setOnClickListener(this);
        ib_phone.setOnClickListener(this);
        ib_voice.setOnClickListener(this);
        btn_modd_commonly.setOnClickListener(this);
        btn_mood_bad.setOnClickListener(this);
        btn_mood_happy.setOnClickListener(this);
        btn_weather_cloudy.setOnClickListener(this);
        btn_weather_rain.setOnClickListener(this);
        btn_weather_snow.setOnClickListener(this);
        btn_weather_sunny.setOnClickListener(this);
        btn_back.setOnClickListener(this);


        ed_title.setOnFocusChangeListener(this);
        ed_title.setOnClickListener(this);


    }

    public void initView() {
        RelativeLayout viewById = (RelativeLayout) findViewById(R.id.rl_edit_diary_bar);
        thisView = (SoftInputCoordinatorLayout) findViewById(R.id.cl_edit_diary);
        viewById.setPadding(0, getStatusBarHeight(), 0, 0);
        btn_back = (Button) findViewById(R.id.btn_editDiary_back);
        tv_day = (TextView) findViewById(R.id.tv_editDiary_day);
        tv_month = (TextView) findViewById(R.id.tv_editDiary_month);
        tv_week = (TextView) findViewById(R.id.tv_editDiary_week);
        btn_mood = (Button) findViewById(R.id.btn_editDiary_mood);
        btn_weather = (Button) findViewById(R.id.btn_editDiary_weather);
        btn_collection = (Button) findViewById(R.id.btn_editDiary_collection);
        ib_location = (ImageButton) findViewById(R.id.ib_editDiary_location);
        ib_more = (ImageButton) findViewById(R.id.ib_editDiary_more);
        ib_phone = (ImageButton) findViewById(R.id.ib_editDiary_phone);
        ib_voice = (ImageButton) findViewById(R.id.ib_editDiary_voice);
        ed_title = (EditText) findViewById(R.id.ed_editDiary_title);
        tv_location = (TextView) findViewById(R.id.tv_editDiary_location);

        ed_content = (RichEditorText) findViewById(R.id.ed_editDiary_content);

        ppw_mood = this.getLayoutInflater().inflate(R.layout.pupowindow_mood, null);
        ppw_weather = this.getLayoutInflater().inflate(R.layout.popupwindow_weather, null);
        ppw_audio = this.getLayoutInflater().inflate(R.layout.pupowindow_audio, null);
        btn_mood_bad = (Button) ppw_mood.findViewById(R.id.ppw_mood_bad);
        btn_modd_commonly = (Button) ppw_mood.findViewById(R.id.ppw_mood_commonly);
        btn_mood_happy = (Button) ppw_mood.findViewById(R.id.ppw_mood_happy);
        btn_weather_sunny = (Button) ppw_weather.findViewById(R.id.ppw_weather_sunny);
        btn_weather_cloudy = (Button) ppw_weather.findViewById(R.id.ppw_weather_cloudy);
        btn_weather_rain = (Button) ppw_weather.findViewById(R.id.ppw_weather_rain);
        btn_weather_snow = (Button) ppw_weather.findViewById(R.id.ppw_weather_snow);
        viewUtils = (RelativeLayout) findViewById(R.id.view_editDiary_utils);
        audioView = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.audio_recoder, null);

        btn_audio_recoder = (Button) audioView.findViewById(R.id.btn_audio_recoder);


        popupWindow = new PopupWindow(ppw_audio, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_editDiary_back) {
            finish();
        } else {
            impiEditDiaryPresenter.onClickListener(view);
        }
    }


    @Override
    public void setLocation(String location) {
        //显示定位
        tv_location.setText(location);
    }

    @Override
    public void addVoiceInEditText(String path) {

        ed_content.addAudioViewAtIndex(ed_content.getLastIndex(), path);

    }

    @Override
    public void closeViewUtils() {
        if (isOpenViewUtils) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewUtils.getLayoutParams();
            layoutParams.height = 0;
            viewUtils.setLayoutParams(layoutParams);
            isOpenViewUtils = false;

        }
    }


    @Override
    public void checkLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkLocationPermission()){
                //有权限则定位
                impiEditDiaryPresenter.loaction();

            }else {
                showNotPermissionInfo(505,"需要权限","需要获取定位权限，否则无法定位");
            }
        }else {
            impiEditDiaryPresenter.loaction();
        }
    }



    @Override
    public void setCollection(int collection) {
//        Log.i(TAG, "setCollection: 收藏设置" + collection);
        if (collection == 100) {//不收藏
            COLLECTION = 100;
            btn_collection.setBackgroundResource(R.drawable.collno);

        } else {
            btn_collection.setBackgroundResource(R.drawable.collyes);
            COLLECTION = 101;
        }
    }

    @Override
    public void updataCollection() {
//        Log.i(TAG, "updataCollection: 点击切换收藏" + COLLECTION);
        if (COLLECTION == 100) {
            COLLECTION = 101;
            btn_collection.setBackgroundResource(R.drawable.collyes);

        } else {
            COLLECTION = 100;
            btn_collection.setBackgroundResource(R.drawable.collno);
        }

    }

    @Override
    public void setMood(int mood) {
        switch (mood) {
            case 200:
                btn_mood.setBackgroundResource(R.drawable.ic_diary_bar_happy);
                MOOD = 200;
                break;
            case 201:
                btn_mood.setBackgroundResource(R.drawable.ic_diary_bar_commonly);
                MOOD = 201;
                break;
            case 202:
                MOOD = 202;
                btn_mood.setBackgroundResource(R.drawable.ic_diary_bar_bad);
                break;

        }
        if (popupwindowMood != null) {
            if (popupwindowMood.isShowing()) {
                popupwindowMood.dismiss();

            }
        }

    }

    @Override

    public void showChooseMood() {

        //根据点击的按钮设置弹窗的大小
        int width = btn_mood.getMeasuredWidth();
        int height = btn_mood.getMeasuredHeight();
        popupwindowMood = new PopupWindow(ppw_mood, width, height * 3);
        //不触发键盘
        popupwindowMood.setFocusable(false);
        popupwindowMood.setOutsideTouchable(true);
        popupwindowMood.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        popupwindowMood.update();
        popupwindowMood.showAsDropDown(btn_mood, 0, -width);
    }

    @Override

    public void showChooseWeather() {

        //根据点击的按钮设置弹窗的大小
        int width = btn_weather.getMeasuredWidth();
        int height = btn_weather.getMeasuredHeight();
        popupWindowWeather = new PopupWindow(ppw_weather, width, height * 4);
        popupWindowWeather.setFocusable(false);
        popupWindowWeather.setOutsideTouchable(true);
        popupWindowWeather.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        popupWindowWeather.update();
        popupWindowWeather.showAsDropDown(btn_weather, 0, -width);

    }

    @Override
    public void setWeather(int weather) {
        switch (weather) {
            case 300:
                WEATHER = 300;
                btn_weather.setBackgroundResource(R.drawable.ic_diary_bar_sunny);
                break;
            case 301:
                WEATHER = 301;
                btn_weather.setBackgroundResource(R.drawable.ic_diary_bar_cloudy);
                break;
            case 302:
                WEATHER = 302;
                btn_weather.setBackgroundResource(R.drawable.ic_diary_bar_rain);
                break;
            case 303:
                WEATHER = 303;
                btn_weather.setBackgroundResource(R.drawable.ic_diary_bar_sonw);
                break;

        }
        if (popupWindowWeather != null) {
            if (popupWindowWeather.isShowing()) {
                popupWindowWeather.dismiss();
            }
        }
    }

    @Override
    public void setDiaryContent(String number) {
        ed_content.setmContentList(number);
    }

    @Override
    public void setDiaryTitle(String title) {
        ed_title.setText(title);
    }

    @Override
    public void setMonth(String month) {
        tv_month.setText(month);
    }

    @Override
    public void setDay(String day) {
        tv_day.setText(day);
    }

    @Override
    public void setWeek(String week) {
        tv_week.setText(week);
    }

    @Override
    public void showMore() {
        //显示更多
        Log.i(TAG, "showMore: 显示更多");



    }

    @Override
    public void addPhone(String uri) {

        ed_content.addImage(uri);
    }

    @Override
    public void showChooseGetImageDialog() {
        //隐藏软键盘
        closeSoftKeyboard();
        showChooseGetPhoto();
    }


    @Override
    public void atPhotoGetImage(String fileName) {
        impiEditDiaryPresenter.addImage(MyNoteBookConfig.imageUrl + fileName + ".jpg");
    }

    @Override
    public void atAlbumGetImage(String imgUri) {
        impiEditDiaryPresenter.addImage(imgUri);

    }

    @Override
    public void addVoice() {
        //关闭软键盘
        closeSoftKeyboard();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查录音权限
            if (checkRecordAudioPermissions()&&checkWritePermission()){
               showAudioUtils();
            }else {
                //显示警告框
                showNotPermissionInfo(502,"需要获取权限","需要获取录音和写入文件权限\n否则无法正常使用");
            }
        }else {
            showAudioUtils();
        }



    }
    private void showAudioUtils(){

        if (!isOpenViewUtils) {
            isOpenViewUtils = true;
            openViewUtils();
            viewUtils.removeAllViews();
            viewUtils.addView(audioView);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) audioView.getLayoutParams();
            layoutParams.height = layoutParams.MATCH_PARENT;
            layoutParams.width = layoutParams.MATCH_PARENT;
            audioView.setLayoutParams(layoutParams);


        } else {//如果已经打开那就关闭
            impiEditDiaryPresenter.closeViewUtils();
        }

    }

    private void openViewUtils() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewUtils.getLayoutParams();
        layoutParams.height = (getWindowManager().getDefaultDisplay().getHeight()) / 3;
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
        viewUtils.setLayoutParams(layoutParams);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String allData = ed_content.getAllData();
        //标题或者内容都不为空时退出自动保存
        if (!allData.equals("") ||

                !ed_title.getText().toString().equals("")) {

            impiEditDiaryPresenter.saveDiary(getIntent().getStringExtra("Id"),
                    ed_title.getText().toString(),

                    allData,

                    tv_month.getText().toString(),
                    tv_day.getText().toString(),
                    tv_week.getText().toString(),
                    COLLECTION,
                    MOOD,
                    WEATHER, getIntent().getStringExtra("Position"),
                    getIntent().getStringExtra("Who"),
                    tv_location.getText().toString());
        }
        //注销广播
        unregisterReceiver(impiEditDiaryPresenter);
    }



    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //退出键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOpenViewUtils) {
                impiEditDiaryPresenter.closeViewUtils();
            } else {
                finish();
            }
        }

        return true;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            impiEditDiaryPresenter.closeViewUtils();
        }
    }

    @Override
    public void startRecordAudio() {

        impiEditDiaryPresenter.startRecordAudio();

    }

    @Override
    public void getPermission(int code) {
        super.getPermission(code);
        //再次申请录音权限
        if (code==502) {
            requestRecordAudioPermissions();

        }
        //再次申请定位权限
        if (code==505){
            requestLocationPermission();
        }

    }

    @Override
    public void saveRecordAudio() {
        impiEditDiaryPresenter.saveRecordAudio();
    }

    @Override
    public void cancelRecordAudio() {
        impiEditDiaryPresenter.cancelRucordAudio();
    }

}

