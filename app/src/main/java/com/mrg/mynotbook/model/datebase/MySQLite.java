package com.mrg.mynotbook.model.datebase;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by MrG on 2017-05-11.
 */
public class MySQLite extends SQLiteOpenHelper {
    private static final String BD_NAME = "myNoteBook.db";
    private static final int BD_VERISON = 2;
    public static final String PhoneUser = "PhoneUser";
    public static final String Diary = "Diary";
    public static final String Quite = "Quite";
    private Context context;

    public MySQLite(Context context) {
        super(context, BD_NAME, null, BD_VERISON);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         * 表名：MainContent(包含了Note)
         * 主键：Id
         * 字段：note_Date 便签日期（12）
         * 字段：note_Time 便签时间（6）
         * 字段：note_Number 便签内容（1000）
         * 字段：title 标题 (16)
         * 字段：type 类型
         * 字段：sort 排位标识
         * 字段：year 年份（5）
         * 字段：cover 封面（100）
         * 字段：synchronization 同步
         * 字段：encryption 加密
         * 字段：hide 隐藏
         *
         * */
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS MainContent(" +
                "Id integer primary key autoincrement," +
                "note_Date varchar(12)," +
                "note_Time varchar(16)," +
                "note_Number varchar(1000)," +
                "title varchar(16)," +
                "type integer," +
                "sort integer," +
                "year varchar(5)," +
                "cover varchar(100)," +
                "synchronization bit," +
                "encryption bit," +
                "hide bit)");

        /**
         * 表名：PhoneUser(电话本)
         * 主键：Id
         * 字段：name 名字（12）
         * 字段：phone_Number 电话号（6）
         * 字段：sort （6）排位标识
         * 字段：sort_two 排位标识2
         * 字段：who 谁的电话本
         * 字段：synchronization 同步
         * */
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + PhoneUser + "(" +
                "Id integer primary key autoincrement," +
                "name varchar(12)," +
                "phone_Number varchar(11)," +
                "sort varchar(6)," +
                "sort_two integer," +
                "who integer," +
                "synchronization bit)");
        /**
         *  id;//id
         month;//月份
         day;//天
         time;//时间
         week;//星期
         title;//标题
         number;//内容
         collection;//收藏，100不收藏，101收藏
         mood;//心情，200开心，201一般，202不开心
         weather;//天气，300晴，301阴，302雨，303雪
         photo;//照片地址
         字段：who integer
         字段：year 年份
         字段：synchronization 同步
         字段： location (200) //定位
         */


        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Diary + "(" +
                "Id integer primary key autoincrement," +
                "month varchar(2)," +
                "day varchar(2)," +
                "time varchar(5)," +
                "week varchar(3)," +
                "title varchar(16)," +
                "number varchar(1000)," +
                "collection integer," +
                "mood integer," +
                "weather integer," +
                "phone varchar(100)," +
                "sort integer," +
                "who integer," +
                "year varchar(5)," +
                "synchronization bit," +
                "location varchar(200))");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Quite + "(" +
                "Id integer primary key autoincrement," +
                "number varchar(11)," +
                "date varchar(6)," +
                "time varchar(5)," +
                "week varchar(3)," +
                "type varchar(2)," +
                "synchronization bit)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("mrg", "onUpgrade: 数据库更新");
        switch (i) {
            case 1:

            case 2:
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Quite + "(" +
                        "Id integer primary key autoincrement," +
                        "number varchar(11)," +
                        "date varchar(6)," +
                        "time varchar(5)," +
                        "week varchar(3)," +
                        "type varchar(2)," +
                        "synchronization bit)");
            case 3:


                break;
        }

    }
}
