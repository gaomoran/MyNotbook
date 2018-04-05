package com.mrg.mynotbook.presenter.impi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.mrg.mynotbook.model.DiaryModel;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.impi.ImpiDiaryModel;
import com.mrg.mynotbook.presenter.DiaryListenter;
import com.mrg.mynotbook.presenter.DiaryPresenter;
import com.mrg.mynotbook.ui.DiaryView;

import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public class ImpiDiaryPresenter extends BroadcastReceiver implements DiaryPresenter,DiaryListenter {

    public static final String TAG="mrg";
    private DiaryView diaryView;
    private final DiaryModel diaryModel;
    private Context context;
    public ImpiDiaryPresenter(DiaryView diaryView,Context context){
        this.diaryView=diaryView;
        diaryModel = new ImpiDiaryModel(context);
        this.context=context;

    }

    @Override
    public void onItemClickListenter(String id,String position) {
        diaryView.startEditDiary(id,position);
    }

    @Override
    public void setDiary(Diary diary, String id) {
        diaryModel.setDiary(diary,id);
    }

    @Override
    public Diary getDiary(String id) {
       return diaryModel.getDiary(id);
    }

    @Override
    public void deleteDiary(final String id, final String position) {
        new Thread(){
            @Override
            public void run() {
                diaryModel.deleteDiary(id);
                Intent intent = new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.REMOVE_DIARY_DATA");
                intent.putExtra("Position",position);
                context.sendBroadcast(intent);
            }
        }.start();




    }

    @Override
    public void addDiary(Diary diary,String who) {
        diaryModel.addDiary(diary,who);
    }

    @Override
    public void initView(String who) {

        List<Diary> diaryList = diaryModel.getDiaryList(who);
        if (diaryList.size()==0){
            diaryView.showNull(true);
        }
        else {
            diaryView.showNull(false);

        }
        diaryView.showList(diaryList);

    }

    @Override
    public void sureDelete(String id,String position) {
        diaryView.showSureDelete(id,position);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {

            case "com.mrg.myNoteBook.UI.ADD_DIARY_DATA":
//                Log.i(TAG, "onReceive: 收到增加子项广播");
                String who = intent.getStringExtra("Who");
                Diary newDairy = diaryModel.getNewDairy(who);
                diaryView.addItem(newDairy);
                break;

            case "com.mrg.myNoteBook.UI.REMOVE_DIARY_DATA":
                String position1 = intent.getStringExtra("Position");
                diaryView.deleteItem(position1);
                break;

            case "com.mrg.myNoteBook.UI.UP_DIARY_DATA":
                String id1 = intent.getStringExtra("Id");
                String position = intent.getStringExtra("Position");
                Diary diary = diaryModel.getDiary(id1);
                diaryView.updataItem(position, diary);
                break;


        }
    }
}
