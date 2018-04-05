package com.mrg.mynotbook.model.impi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mrg.mynotbook.model.SearchModel;
import com.mrg.mynotbook.model.datebase.MySQLite;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.entities.SearchEntity;
import com.mrg.mynotbook.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.mrg.mynotbook.model.impi.ImpiMainModel.TAG;

/**
 * Created by MrG on 2018-03-18.
 */

public class ImpiSearchModel implements SearchModel {

    private MySQLite mySQLite;

    public ImpiSearchModel(Context context) {
        mySQLite = new MySQLite(context);
    }
    @Override
    public List<SearchEntity> SearchDiaryData(String keyword) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,title,number,who,month,day from " + mySQLite.Diary + " where  number like '%"+keyword+"%' or title like '%"+keyword+"%'",null);
        List<SearchEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SearchEntity searchEntity = new SearchEntity();
            searchEntity.setId(cursor.getString(0));
            searchEntity.setTitle(cursor.getString(1));
            searchEntity.setWho(cursor.getString(3));
            searchEntity.setSearchNumber(StringUtils.getNumber(cursor.getString(2)));
            searchEntity.setDate(cursor.getString(4)+"月"+cursor.getString(5)+"日");
            searchEntity.setSearchType("Diary");
            list.add(searchEntity);
            Log.i(TAG, "SearchDiaryData: 搜素数据"+searchEntity.getTitle());
        }
        cursor.close();
        readableDatabase.close();

        return list;

    }

    @Override
    public List<SearchEntity> SearchQuiteData(String keyword) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,number,date from " + mySQLite.Quite + " where  number like '%"+keyword+"%'",null);
        List<SearchEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SearchEntity searchEntity = new SearchEntity();
            searchEntity.setId(cursor.getString(0));
            searchEntity.setSearchNumber(StringUtils.getNumber(cursor.getString(1)));
            searchEntity.setDate(cursor.getString(2));
            searchEntity.setSearchType("Quite");
            list.add(searchEntity);
            Log.i(TAG, "SearchDiaryData: 搜素数据"+searchEntity.getTitle());
        }
        cursor.close();
        readableDatabase.close();
        return list;
    }

    @Override
    public List<SearchEntity> SearchPhoneData(String keyword) {
        SQLiteDatabase readableDatabase = mySQLite.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select Id,phone_Number,name from " + mySQLite.PhoneUser + " where  name like '%"+keyword+"%' or phone_Number like '%"+keyword+"%'",null);
        List<SearchEntity> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SearchEntity searchEntity = new SearchEntity();
            searchEntity.setId(cursor.getString(0));
            searchEntity.setSearchNumber(cursor.getString(1));
            searchEntity.setTitle(cursor.getString(2));
            searchEntity.setSearchType("Phone");
            list.add(searchEntity);
            Log.i(TAG, "SearchDiaryData: 搜素数据"+searchEntity.getTitle());
        }
        cursor.close();
        readableDatabase.close();
        return list;
    }
}
