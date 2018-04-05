package com.mrg.mynotbook.model.impi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.mrg.mynotbook.model.datebase.MySQLite;

import com.mrg.mynotbook.model.NoteModel;
import com.mrg.mynotbook.model.entities.Note;
import com.mrg.mynotbook.utils.DataTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public class ImpiNoteModel implements NoteModel {
    public static final String TAG = "mrg";
    public static final int TYPE_NOTE = 3;

    private MySQLite mySQLite;


    public ImpiNoteModel(Context context) {
        mySQLite = new MySQLite(context);

    }

    @Override
    public void addNote(Note note) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note_Date", note.getDate());
        values.put("note_Time", note.getTime());
        values.put("note_Number", note.getNumber());
        values.put("title", "null");
        values.put("type", TYPE_NOTE);
        values.put("sort", System.currentTimeMillis());
//        Log.i(TAG, "addNote: 新保存一个note");
        writableDatabase.insert("MainContent", null, values);

        writableDatabase.close();
    }

    @Override
    public void deleteNote(String id) {

        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete("MainContent", "Id=?", new String[]{id});
        writableDatabase.close();

    }

    @Override
    public void setNote(Note note) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("note_Date", note.getDate());
        values.put("note_Time", note.getTime());
        values.put("note_Number", note.getNumber());
        values.put("title", "null");
        values.put("type", TYPE_NOTE);
        values.put("sort", System.currentTimeMillis());
        writableDatabase.update("MainContent", values, "Id=?", new String[]{note.getId()});
        writableDatabase.close();
//        Log.i(TAG, "setNote: 修改一个note");

    }

    @Override
    public Note getNote(String id) {

        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,note_Date,note_Time,note_Number from MainContent where Id=?", new String[]{id});
        Note note = new Note();
        cursor.moveToFirst();
//        Log.i(TAG, "getNote: 查询id:"+id);
        note.setId(cursor.getInt(0) + "");
        note.setDate(cursor.getString(1));
        note.setTime(cursor.getString(2));
        note.setNumber(cursor.getString(3));

        cursor.close();
        readableDatabase.close();
//        Log.i(TAG, "getNote: 查询一个note:"+note.getNumber());
        return note;
    }

    @Override
    public List<Note> getNoteAll() {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,note_Date,note_Time,note_Number form MainContent", null);
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
    public String getNowData() {
        return DataTimeUtils.getNowDate();
    }

    @Override
    public String getNowTime() {
        return DataTimeUtils.getNowTime();
    }
}
