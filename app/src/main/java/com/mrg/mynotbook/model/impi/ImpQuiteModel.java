package com.mrg.mynotbook.model.impi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mrg.mynotbook.model.datebase.MySQLite;
import com.mrg.mynotbook.model.QuiteModel;
import com.mrg.mynotbook.model.entities.Note;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.utils.DataTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-11-15.
 */
public class ImpQuiteModel implements QuiteModel {

    private MySQLite mySQLite;
    private String TAG="mrg";

    public ImpQuiteModel(Context context) {
        mySQLite = new MySQLite(context);
    }

    @Override
    public List<Quite> getAll() {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from Quite", null);
        ArrayList<Quite> quites = new ArrayList<>();
        while (cursor.moveToNext()) {
            Quite quite = new Quite();
            quite.setId(cursor.getInt(0) + "");
            quite.setNumber(cursor.getString(1));
            quite.setDate(cursor.getString(2));
            quite.setTime(cursor.getString(3));
            quite.setWeek(cursor.getString(4));
            quite.setType(cursor.getString(5));
            quites.add(quite);
        }
        cursor.close();
        readableDatabase.close();

        return quites;
    }

    @Override
    public Quite getNewQuite() {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from Quite", null);
        cursor.moveToLast();
        Quite quite = new Quite();
        quite.setId(cursor.getInt(0) + "");
        quite.setNumber(cursor.getString(1));
        quite.setDate(cursor.getString(2));
        quite.setTime(cursor.getString(3));
        quite.setWeek(cursor.getString(4));
        quite.setType(cursor.getString(5));

        cursor.close();
        readableDatabase.close();

        return quite;
    }

    @Override
    public Quite getQuite(String id) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from Quite where Id=?", new String[]{id});
        cursor.moveToFirst();
        Quite quite = new Quite();
        quite.setId(cursor.getInt(0) + "");
        quite.setNumber(cursor.getString(1));
        quite.setDate(cursor.getString(2));
        quite.setTime(cursor.getString(3));
        quite.setWeek(cursor.getString(4));
        quite.setType(cursor.getString(5));

        cursor.close();
        readableDatabase.close();
        return quite;
    }

    @Override
    public void addQuite(Quite quite) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", quite.getNumber());
        values.put("date", quite.getDate());
        values.put("time", quite.getTime());
        values.put("type", quite.getType());
        values.put("week", quite.getWeek());

        writableDatabase.insert("Quite", null, values);

        writableDatabase.close();
    }

    @Override
    public void deleteQuite(String id) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete("Quite", "Id=?", new String[]{id});
        writableDatabase.close();
    }

    @Override
    public void setQuite(Quite quite) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", quite.getNumber());
        values.put("date", quite.getDate());
        values.put("time", quite.getTime());
        values.put("type", quite.getType());
        values.put("week", quite.getWeek());

        writableDatabase.update("Quite", values, "Id=?", new String[]{quite.getId()});

        writableDatabase.close();
    }

    public void importNoteDate(List<Note> list){
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();

        for (Note note : list) {

            ContentValues values = new ContentValues();

            values.put("number", note.getNumber());
            String date = note.getDate();
            values.put("date", date);
            values.put("time", note.getTime());
            values.put("type", "N");
            values.put("week", "");

            writableDatabase.insert("Quite", null, values);

        }

        writableDatabase.close();



    }


    public String getNowData() {
        return DataTimeUtils.getNowDate();
    }
    public String getNowTime() {
        return DataTimeUtils.getNowTime();
    }

}
