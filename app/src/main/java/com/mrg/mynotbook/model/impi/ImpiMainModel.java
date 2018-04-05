package com.mrg.mynotbook.model.impi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mrg.mynotbook.model.MainModel;

import com.mrg.mynotbook.model.datebase.MySQLite;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.Note;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-05-05.
 */
public class ImpiMainModel implements MainModel {

    public static final String TAG = "mrg";
    public static final int TYPE_DIARY = 1;
    public static final int TYPE_PHONE = 2;
    private final MySQLite mySQLite;

    public ImpiMainModel(Context context) {
        mySQLite = new MySQLite(context);

    }

    @Override
    public List<ContentList> getContentList() {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from MainContent order by sort", null);
        List<ContentList> lists = new ArrayList<ContentList>();
//        Log.i(TAG, "getContentList: 查询主数据");
        while (cursor.moveToNext()) {
            ContentList contentList = new ContentList();
//            Log.i(TAG, "getContentList: 装载主数据");
            contentList.setId(cursor.getInt(0) + "");
            contentList.setDate(cursor.getString(1));
            contentList.setTitle(cursor.getString(2));
            contentList.setNumber(cursor.getString(3));
            contentList.setTitle(cursor.getString(4));
            contentList.setType(cursor.getInt(5));
            contentList.setCover(cursor.getString(8));
            lists.add(contentList);
        }

        cursor.close();
        readableDatabase.close();

        return lists;
    }

    @Override
    public ContentList getNewData() {


        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from MainContent order by sort DESC", null);
        cursor.moveToFirst();
        ContentList contentList = new ContentList();
//        Log.i(TAG, "getNewData: 获得最新一组数据");
        contentList.setId(cursor.getInt(0) + "");
        contentList.setDate(cursor.getString(1));
        contentList.setTitle(cursor.getString(2));
        contentList.setNumber(cursor.getString(3));
        contentList.setTitle(cursor.getString(4));
        contentList.setType(cursor.getInt(5));
        contentList.setCover(cursor.getString(8));
        Log.i(TAG, "getNewData: 获得数据"+cursor.getString(8));
        cursor.close();
        readableDatabase.close();

        return contentList;

    }

    @Override
    public ContentList getMainData(String id) {

        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from MainContent where Id=?", new String[]{id});
        cursor.moveToFirst();
        ContentList contentList = new ContentList();
        contentList.setId(cursor.getInt(0) + "");
        contentList.setDate(cursor.getString(1));
        contentList.setTitle(cursor.getString(2));
        contentList.setNumber(cursor.getString(3));
        contentList.setTitle(cursor.getString(4));
        contentList.setType(cursor.getInt(5));
        contentList.setCover(cursor.getString(8));
        cursor.close();
        readableDatabase.close();

        return contentList;
    }

    @Override
    public void deleteMainData(String id) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
//        Log.i(TAG, "deleteMainData: 数据库删除一个数据"+id);
        writableDatabase.delete("MainContent", "Id=?", new String[]{id});
        writableDatabase.close();

    }

    @Override
    public void addDiary(String number,String cover) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note_Number",number);
        values.put("title",System.currentTimeMillis());
        values.put("sort",System.currentTimeMillis());
        values.put("type", TYPE_DIARY);
        values.put("cover",cover);
        writableDatabase.insert("MainContent", null, values);
        writableDatabase.close();

    }

    @Override
    public void addPhone(String number) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note_Number",number);
        values.put("title",System.currentTimeMillis());
        values.put("sort",System.currentTimeMillis());
        values.put("type", TYPE_PHONE);
        writableDatabase.insert("MainContent", null, values);
        writableDatabase.close();
    }

    @Override
    public void setDiary(ContentList contentList) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note_Number",contentList.getNumber());
        values.put("title", contentList.getTitle());
        values.put("type",TYPE_DIARY);
        values.put("cover",contentList.getCover());

        writableDatabase.update("MainContent", values, "Id=?", new String[]{contentList.getId()});

        writableDatabase.close();
    }

    @Override
    public void setPhone(ContentList contentList) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note_Number",contentList.getNumber());
        values.put("title", contentList.getTitle());
        values.put("type",TYPE_PHONE);
        writableDatabase.update("MainContent",values,"Id=?",new String[]{contentList.getId()});
        writableDatabase.close();
    }

    @Override
    public List<Note> getNoteAll() {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,note_Date,note_Time,note_Number from MainContent where type=?", new String[]{"3"});
        List<Note> list = new ArrayList<>();
        while (cursor.moveToNext()) {

            Note note = new Note();
            note.setId(cursor.getInt(0) + "");
            note.setDate(cursor.getString(1));
            note.setTime(cursor.getString(2));
            note.setNumber(cursor.getString(3));
            list.add(note);

        }
        cursor.close();
        readableDatabase.close();
        return list;
    }
    @Override
    public void deleteAllNote(){

        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete("MainContent","type=?",new String[]{"3"});
        writableDatabase.close();

    }




}
