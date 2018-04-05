package com.mrg.mynotbook.presenter.impi;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.mrg.mynotbook.config.MyNoteBookConfig;
import com.mrg.mynotbook.model.NoteModel;

import com.mrg.mynotbook.model.QuiteModel;
import com.mrg.mynotbook.model.entities.Note;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.impi.ImpQuiteModel;

import com.mrg.mynotbook.model.impi.ImpiNoteModel;
import com.mrg.mynotbook.presenter.NoteListenter;
import com.mrg.mynotbook.presenter.NotePresenter;
import com.mrg.mynotbook.ui.NoteView;

import com.mrg.mynotbook.utils.DataTimeUtils;
import com.mrg.mynotbook.utils.FileUtils;


import java.io.File;


/**
 * Created by MrG on 2017-05-06.
 */
public class ImpiNotePresenter implements NotePresenter,NoteListenter {
    public static final String TAG="mrg";
    private NoteView noteView;

    private QuiteModel noteModel;
    private Context context;
    private final FileUtils fileUtils;

    public ImpiNotePresenter(NoteView noteView,Context context){
        this.noteView=noteView;
        this.context=context;
        noteModel=new ImpQuiteModel(context);
        fileUtils = new FileUtils();

    }
    @Override
    public void saveNote(String data, String time,String number, final String id, final String position) {



        final Quite quite = new Quite();
        quite.setId(id);
        quite.setDate(data);
        quite.setNumber(number);
        quite.setTime(time);
        quite.setType("N");

        //耗时操作

        if (id.equals("new")){//新建note
            new Thread(){
                @Override
                public void run() {

                    noteModel.addQuite(quite);
                    Intent intent=new Intent();
                    intent.setAction("com.mrg.myNoteBook.UI.ADD_QUITE_DATA");

                    context.sendBroadcast(intent);

                }
            }.start();


        }else {//修改note
            new Thread(){
                @Override
                public void run() {

                    Log.i(TAG, "run: 修改便签:"+quite.getNumber());
                    noteModel.setQuite(quite);
                    Intent intent=new Intent();
                    intent.setAction("com.mrg.myNoteBook.UI.UP_QUITE_DATA");

                    intent.putExtra("Id", id);
                    intent.putExtra("Position",position);
                    context.sendBroadcast(intent);

                }
            }.start();

        }

    }

    @Override
    public void deleteNote(final String id, final String position) {
        new Thread(){
            @Override
            public void run() {


                noteModel.deleteQuite(id);
                Intent intent=new Intent();
                intent.setAction("com.mrg.myNoteBook.UI.DELETE_QUITE_DATA");

                intent.putExtra("Position",position);
                context.sendBroadcast(intent);

            }
        }.start();


    }

    @Override
    public void addImage(String url) {

        noteView.addImage(url);
    }

    @Override
    public Uri saveImage(String fileName,Context context) {

       return fileUtils.saveImage(fileName,context);

    }

    @Override
    public void initView() {
        switch (noteView.getIntentType()){

            case "add":
                noteView.setBarTitle(noteModel.getNowData());
                noteView.setBarSubtitle(noteModel.getNowTime());
                noteView.setDeleteEnabled(false);
//                Log.i(TAG, "initView: 新建note界面");
                break;
            case "show":

                Quite note = noteModel.getQuite(noteView.getIntentData());

                noteView.setBarTitle(note.getDate());
                noteView.setBarSubtitle(note.getTime());
                noteView.setNoteNumber(note.getNumber());
//                Log.i(TAG, "initView: 打开note界面："+note.getNumber());
                break;
        }
    }
}
