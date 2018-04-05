package com.mrg.mynotbook.presenter.impi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import com.mrg.mynotbook.model.DiaryModel;
import com.mrg.mynotbook.model.MainModel;
import com.mrg.mynotbook.model.PhoneUserModel;
import com.mrg.mynotbook.model.QuiteModel;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.Note;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.impi.ImpQuiteModel;
import com.mrg.mynotbook.model.impi.ImpiDiaryModel;
import com.mrg.mynotbook.model.impi.ImpiMainModel;
import com.mrg.mynotbook.model.impi.ImpiNoteModel;

import com.mrg.mynotbook.model.impi.ImpiPhoneUserModel;
import com.mrg.mynotbook.presenter.MainListener;
import com.mrg.mynotbook.presenter.MainPresenter;
import com.mrg.mynotbook.ui.MainView;

import com.mrg.mynotbook.utils.AudioRecoderUtils;
import com.mrg.mynotbook.utils.DataTimeUtils;
import com.mrg.mynotbook.utils.DialogUtils;
import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.NativeAdInfoIndex;
import com.xiaomi.ad.NativeAdListener;
import com.xiaomi.ad.adView.StandardNewsFeedAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.text.ParseException;
import java.util.ArrayList;

import java.util.List;

import cn.bmob.v3.Bmob;


/**
 * Created by MrG on 2017-05-05.
 */
public class ImpiMainPresenter extends BroadcastReceiver implements MainPresenter, MainListener {

    public static final String TAG = "mrg";
    public static final int TYPE_DIARY = 1;
    public static final int TYPE_NOTE = 3;
    public static final int TYPE_PHONE = 2;
    private MainView mainView;
    private MainModel mainModel;
    private Context context;
    private DiaryModel impiDiaryModel;
    private PhoneUserModel impiPhoneUserModel;

    private DialogUtils dialogUtils;
    private final AudioRecoderUtils audioRecoderUtils;
    private String audioPath;
    private QuiteModel quiteModel;


    public ImpiMainPresenter(MainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        mainModel = new ImpiMainModel(context);
        impiDiaryModel = new ImpiDiaryModel(context);
        impiPhoneUserModel = new ImpiPhoneUserModel(context);

        dialogUtils = new DialogUtils(context);
        audioRecoderUtils = new AudioRecoderUtils();
        quiteModel = new ImpQuiteModel(context);



    }

    @Override
    public void goItem(ContentList contentList) {

        switch (contentList.getType()) {
            case TYPE_DIARY:
                mainView.startDiaryActivity("show", contentList.getId(), contentList.getPosition(), contentList.getTitle());
                break;


            case TYPE_PHONE:
                mainView.startPhoneActivity("show", contentList.getId(), contentList.getPosition(), contentList.getTitle());
                break;

        }


    }

    @Override

    public void goNoteItem(String id, String position) {
        mainView.startNoteActivity("show", id, position);
    }

    @Override
    public void goQuiteText(Quite quite) {
        mainView.openQuiteTextItem(quite);
    }

    @Override

    public void addDiary() {
        mainView.addDiaryItem();
    }

    @Override
    public void addNote() {
        mainView.startNoteActivity("add", "new", "0");
    }

    @Override
    public void addPhone() {



        mainView.addPhoneItem();
    }

    @Override

    public void deleteItem(final ContentList contentList) {

        switch (contentList.getType()) {
            case TYPE_DIARY:
                new Thread() {
                    @Override
                    public void run() {
                        impiDiaryModel.deleteTypeDiary(contentList.getTitle());
                        mainModel.deleteMainData(contentList.getId());
                        Intent intent = new Intent();
                        intent.setAction("com.mrg.myNoteBook.UI.REMOVE_MAIN_DATA");
                        intent.putExtra("Position", contentList.getPosition());
                        context.sendBroadcast(intent);
                    }
                }.start();

                break;
            case TYPE_NOTE:
                //删除耗时操作
                new Thread() {
                    @Override
                    public void run() {
                        mainModel.deleteMainData(contentList.getId());
                        Intent intent = new Intent();
                        intent.setAction("com.mrg.myNoteBook.UI.REMOVE_MAIN_DATA");
                        intent.putExtra("Position", contentList.getPosition());
                        context.sendBroadcast(intent);
                    }
                }.start();

                break;
            case TYPE_PHONE:
                new Thread() {
                    @Override
                    public void run() {
                        mainModel.deleteMainData(contentList.getId());
                        impiPhoneUserModel.deleteTypePhone(contentList.getTitle());
                        Intent intent = new Intent();
                        intent.setAction("com.mrg.myNoteBook.UI.REMOVE_MAIN_DATA");
                        intent.putExtra("Position", contentList.getPosition());
                        context.sendBroadcast(intent);
                    }
                }.start();

                break;

        }


    }

    @Override

    public void editItem(ContentList contentList) {


        switch (contentList.getType()) {
            case TYPE_DIARY:
                mainView.setDiaryItem(contentList);
                break;
            case TYPE_NOTE:
                goItem(contentList);
                break;
            case TYPE_PHONE:
                mainView.setPhoneItem(contentList);
                break;

        }
    }

    @Override

    public void showQuiteText() {
        mainView.showQuiteText();
    }

    @Override
    public void showQuiteAudio() {
        mainView.showQuiteAudio();
    }

    @Override
    public void showChooseImage() {
        mainView.showQuiteImage();
    }

    @Override
    public void saveQuiteText(String number) {

        Quite quite = new Quite();
        quite.setNumber(number);
        String nowDate = DataTimeUtils.getNowTime();
        quite.setDate(DataTimeUtils.getNowDate("MM-dd"));
        quite.setTime(nowDate);
        quite.setWeek(DataTimeUtils.getWeek());
        quite.setType("T");
        quiteModel.addQuite(quite);
        Intent intent = new Intent();
        intent.setAction("com.mrg.myNoteBook.UI.ADD_QUITE_DATA");
        context.sendBroadcast(intent);
    }

    @Override
    public void saveQuiteImage(String number) {
        Quite quite = new Quite();
        quite.setNumber(number);
        String nowDate = DataTimeUtils.getNowTime();
        quite.setDate(DataTimeUtils.getNowDate("MM-dd"));
        quite.setTime(nowDate);
        quite.setWeek(DataTimeUtils.getWeek());
        quite.setType("I");
        quiteModel.addQuite(quite);
        Intent intent = new Intent();
        intent.setAction("com.mrg.myNoteBook.UI.ADD_QUITE_DATA");
        context.sendBroadcast(intent);
    }

    @Override
    public void deleteQuite(String id, String position) {
        quiteModel.deleteQuite(id);
        Intent intent = new Intent();
        intent.putExtra("Position", position);
        intent.setAction("com.mrg.myNoteBook.UI.DELETE_QUITE_DATA");
        context.sendBroadcast(intent);
    }

    @Override
    public void updateQuiteText(final Quite quite) {
        //更新耗时操作
        new Thread() {
            @Override
            public void run() {

                quiteModel.setQuite(quite);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.UP_QUITE_DATA");
                intent.putExtra("Position", quite.getPosition());
                intent.putExtra("Id", quite.getId());
                context.sendBroadcast(intent);


            }
        }.start();

    }

    @Override
    public void initview() {

        upview();
        //权限申请
        mainView.getPermission();
        //数据转存
        updata();
        //初始化后端
        Bmob.initialize(context,"9250f448a6a36153db58ac21eb00075b","bmob");

    }

    public void upview() {
        List<ContentList> contentList = mainModel.getContentList();
        List<Quite> quiteList = quiteModel.getAll();

        if (contentList.size() == 0) {
            mainView.showIsNullText(true);
        } else {
            mainView.showIsNullText(false);
        }
        //显示主数据
        mainView.showList(contentList, quiteList);

    }

    public void updata() {
        List<Note> noteAll = mainModel.getNoteAll();
        if (noteAll.size() != 0) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
                //版本小于2.0的进行数据转存
                if (packageInfo.versionCode > 6) {
                    quiteModel.importNoteDate(noteAll);
                    mainModel.deleteAllNote();
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            upview();
            Log.i(TAG, "onReceive: 接收开始数据转存");
        }



    }

    @Override
    public ContentList getNewData() {
        return mainModel.getNewData();
    }

    @Override
    public void saveDiary(final String number, final String cover) {

        Log.i(TAG, "saveDiary: 封面" + cover);
        new Thread() {
            @Override
            public void run() {
                mainModel.addDiary(number, cover);

                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.ADD_MAIN_DATA");
                context.sendBroadcast(intent);
            }
        }.start();
    }

    @Override
    public void savePhone(final String number) {
        new Thread() {
            @Override
            public void run() {
                mainModel.addPhone(number);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.ADD_MAIN_DATA");
                context.sendBroadcast(intent);
            }
        }.start();


    }

    @Override
    public void updataDiary(final ContentList contentList) {

        Log.i(TAG, "updataDiary: 封面" + contentList.getCover());

        new Thread() {
            @Override
            public void run() {

                mainModel.setDiary(contentList);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.UP_MAIN_DATA");
                intent.putExtra("Position", contentList.getPosition());
                intent.putExtra("Id", contentList.getId());
                context.sendBroadcast(intent);

            }
        }.start();

    }

    @Override
    public void updataPhone(final ContentList contentList) {
        new Thread() {
            @Override
            public void run() {

                mainModel.setPhone(contentList);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.UP_MAIN_DATA");
                intent.putExtra("Position", contentList.getPosition());
                intent.putExtra("Id", contentList.getId());
                context.sendBroadcast(intent);

            }
        }.start();
    }

    @Override
    public void sureDelete(ContentList contentList) {
        mainView.showSureDelete(contentList);
    }

    @Override

    public void startAudio() {
        audioPath = audioRecoderUtils.startRecord();
    }

    @Override
    public void saveAudio() {
        long time = audioRecoderUtils.stopRecord();
        if (audioPath != null) {
            try {
                String s = DataTimeUtils.longToString(time, "mm:ss");
                Log.i(TAG, "saveRecordAudio: 录音时长" + s);
                audioPath = audioPath + "#" + s;
            } catch (ParseException e) {
                audioPath = audioPath + "#" + "未知时长";
            }
            //显示到列表中
            Quite quite = new Quite();
            quite.setType("A");
            quite.setNumber(audioPath);
            quite.setDate(DataTimeUtils.getNowDate("MM-dd"));
            quite.setTime(DataTimeUtils.getNowTime());
            quite.setWeek(DataTimeUtils.getWeek());
            quiteModel.addQuite(quite);
            Intent intent = new Intent();
            intent.setAction("com.mrg.myNoteBook.UI.ADD_QUITE_DATA");
            context.sendBroadcast(intent);
        }
    }

    @Override
    public void cancelAudio() {
        audioRecoderUtils.cancelRecord();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //广播接收处理

        switch (intent.getAction()) {

            case "com.mrg.myNoteBook.UI.ADD_MAIN_DATA":
                mainView.addDataListView(mainModel.getNewData());
                mainView.showIsNullText(false);
                break;

            case "com.mrg.myNoteBook.UI.UP_MAIN_DATA":
                String position = intent.getStringExtra("Position");

                ContentList date = mainModel.getMainData(intent.getStringExtra("Id"));
                mainView.updateListView(Integer.parseInt(position), date);

                break;

            case "com.mrg.myNoteBook.UI.REMOVE_MAIN_DATA":

                int position1 = Integer.parseInt(intent.getStringExtra("Position"));
                mainView.removeDataListView(position1);

                break;


            case "com.mrg.myNoteBook.UI.ADD_QUITE_DATA":

                mainView.addQuiteDateList(quiteModel.getNewQuite());

                break;
            case "com.mrg.myNoteBook.UI.UP_QUITE_DATA":

                Quite quite = quiteModel.getQuite(intent.getStringExtra("Id"));
                mainView.updateQuiteItem(Integer.parseInt(intent.getStringExtra("Position")), quite);

                break;
            case "com.mrg.myNoteBook.UI.DELETE_QUITE_DATA":
                int position2 = Integer.parseInt(intent.getStringExtra("Position"));
                mainView.deleteQuiteItem(position2);
                break;
            case "com.mrg.myNoteBook.UI.UP_DATA_ALL":
                Log.i(TAG, "onReceive: 刷新全部数据");
                upview();
                break;


        }


    }
}
