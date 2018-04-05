package com.mrg.mynotbook.model.impi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.mrg.mynotbook.model.datebase.MySQLite;

import com.mrg.mynotbook.model.PhoneUserModel;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.utils.IndexFristName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public class ImpiPhoneUserModel implements PhoneUserModel {

    private final MySQLite mySQLite;
    private final String phoneUserName;

    public ImpiPhoneUserModel(Context context) {
        mySQLite = new MySQLite(context);
        phoneUserName = MySQLite.PhoneUser;

    }

    @Override
    public List<PhoneUser> getPhoneUserList(String who) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        List<PhoneUser> phoneUserList = new ArrayList<>();
        Cursor cursor = readableDatabase.rawQuery("select name,phone_Number,Id,sort from " + phoneUserName + " where who=? order by sort ASC", new String[]{who});

        while (cursor.moveToNext()) {

            PhoneUser phoneUser = new PhoneUser();
            phoneUser.setUserName(cursor.getString(0));
            phoneUser.setPhone(cursor.getString(1));
            phoneUser.setId(cursor.getString(2));
            phoneUser.setSort(cursor.getString(3));
            phoneUserList.add(phoneUser);

        }
        cursor.close();
        readableDatabase.close();

        return phoneUserList;

    }

    @Override
    public void addPhone(PhoneUser phoneUser,String who) {

        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", phoneUser.getUserName());
        values.put("phone_Number", phoneUser.getPhone());
        String spells = IndexFristName.getSpells(phoneUser.getUserName());
        if (spells.equals("")){
            values.put("sort",phoneUser.getUserName());
        }else{
            values.put("sort", spells);
        }

        values.put("sort_two", System.currentTimeMillis());
        values.put("who",Long.parseLong(who));
        writableDatabase.insert(phoneUserName, null, values);
        writableDatabase.close();
    }

    @Override
    public void deletePhone(String id) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete(phoneUserName, "Id=?", new String[]{id});
        writableDatabase.close();


    }

    @Override
    public void setPhone(String id, PhoneUser phoneUser) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", phoneUser.getUserName());
        values.put("phone_Number", phoneUser.getPhone());


        writableDatabase.update(phoneUserName, values, "Id=?", new String[]{id});
        writableDatabase.close();

    }

    @Override
    public PhoneUser getPhone(String id) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select name,phone_Number,Id,sort from " + phoneUserName + " where Id=?", new String[]{id});
        cursor.moveToFirst();
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setUserName(cursor.getString(0));
        phoneUser.setPhone(cursor.getString(1));
        phoneUser.setId(cursor.getString(2));
        phoneUser.setSort(cursor.getString(3));
        cursor.close();
        readableDatabase.close();
        return phoneUser;
    }

    @Override
    public PhoneUser getNewPhone(String who) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select name,phone_Number,Id,sort from " + phoneUserName + " where who=? order by sort_two DESC", new String[]{who});
        cursor.moveToFirst();
        PhoneUser phoneUser = new PhoneUser();

        phoneUser.setUserName(cursor.getString(0));
        phoneUser.setPhone(cursor.getString(1));
        phoneUser.setId(cursor.getString(2));
        phoneUser.setSort(cursor.getString(3));

        cursor.close();
        readableDatabase.close();
        return phoneUser;
    }

    @Override
    public void deleteTypePhone(String who) {
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        writableDatabase.delete(phoneUserName,"who=?",new String[]{who});
        writableDatabase.close();
    }
}
