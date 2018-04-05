package com.mrg.mynotbook.model.impi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mrg.mynotbook.model.DiaryModel;

import com.mrg.mynotbook.model.datebase.MySQLite;

import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public class ImpiDiaryModel implements DiaryModel {

    private final MySQLite mySQLite;
    private static String DairyTable;

    public ImpiDiaryModel(Context context) {

        mySQLite = new MySQLite(context);
        DairyTable = MySQLite.Diary;

    }

    @Override
    public List<Diary> getDiaryList(String who) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from " + DairyTable + " where who=? order by sort DESC", new String[]{who});

        List<Diary> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Diary diary = new Diary();
            diary.setId(cursor.getString(0));
            diary.setMonth(cursor.getString(1));
            diary.setDay(cursor.getString(2));
            diary.setTime(cursor.getString(3));
            diary.setWeek(cursor.getString(4));
            diary.setTitle(cursor.getString(5));
            diary.setNumber(StringUtils.getNumber(cursor.getString(6)));
            diary.setCollection(cursor.getInt(7));
            diary.setMood(cursor.getInt(8));
            diary.setWeather(cursor.getInt(9));
            diary.setPhoto(cursor.getString(10));
            diary.setLocation(cursor.getString(15));
            list.add(diary);
        }
        cursor.close();
        readableDatabase.close();

        return list;
    }

    @Override
    public void addDiary(Diary diary,String who) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", diary.getMonth());
        values.put("day", diary.getDay());
        values.put("time", diary.getTime());
        values.put("week", diary.getWeek());
        values.put("title", diary.getTitle());
        values.put("number", diary.getNumber());
        values.put("collection", diary.getCollection());
        values.put("mood", diary.getMood());
        values.put("weather", diary.getWeather());
        values.put("phone", diary.getPhoto());
        values.put("sort", System.currentTimeMillis());
        values.put("who", Long.parseLong(who));
        values.put("location",diary.getLocation());
        writableDatabase.insert(DairyTable, null, values);
        writableDatabase.close();
    }

    @Override
    public void deleteDiary(String id) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete(DairyTable, "Id=?", new String[]{id});

        writableDatabase.close();

    }

    @Override
    public void setDiary(Diary diary, String id) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", diary.getMonth());
        values.put("day", diary.getDay());
        values.put("time", diary.getTime());
        values.put("week", diary.getWeek());
        values.put("title", diary.getTitle());
        values.put("number", diary.getNumber());
        values.put("collection", diary.getCollection());
        values.put("mood", diary.getMood());
        values.put("weather", diary.getWeather());
        values.put("phone", diary.getPhoto());
        values.put("location", diary.getLocation());

        writableDatabase.update(DairyTable, values, "Id=?", new String[]{id});

        writableDatabase.close();
    }

    @Override
    public Diary getDiary(String id) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from " + DairyTable + " where Id=?", new String[]{id});
        cursor.moveToFirst();
        Diary diary = new Diary();
        diary.setId(cursor.getString(0));
        diary.setMonth(cursor.getString(1));
        diary.setDay(cursor.getString(2));
        diary.setTime(cursor.getString(3));
        diary.setWeek(cursor.getString(4));
        diary.setTitle(cursor.getString(5));
        diary.setNumber(cursor.getString(6));
        diary.setCollection(cursor.getInt(7));
        diary.setMood(cursor.getInt(8));
        diary.setWeather(cursor.getInt(9));
        diary.setPhoto(cursor.getString(10));
        diary.setLocation(cursor.getString(15));
        cursor.close();
        readableDatabase.close();

        return diary;
    }

    @Override
    public Diary getNewDairy(String who) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from " + DairyTable + " where who=? order by sort DESC", new String[]{who});
        cursor.moveToFirst();
        Diary diary = new Diary();
        diary.setId(cursor.getString(0));
        diary.setMonth(cursor.getString(1));
        diary.setDay(cursor.getString(2));
        diary.setTime(cursor.getString(3));
        diary.setWeek(cursor.getString(4));
        diary.setTitle(cursor.getString(5));
        diary.setNumber(cursor.getString(6));
        diary.setCollection(cursor.getInt(7));
        diary.setMood(cursor.getInt(8));
        diary.setWeather(cursor.getInt(9));
        diary.setPhoto(cursor.getString(10));
        diary.setLocation(cursor.getString(15));
        cursor.close();
        readableDatabase.close();


        return diary;
    }

    @Override
    public void deleteTypeDiary(String who) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete(DairyTable,"who=?",new String[]{who});
        writableDatabase.close();
    }


}
