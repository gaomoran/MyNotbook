package com.mrg.mynotbook.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MrG on 2017-05-16.
 */
public class DataTimeUtils {
    //获取现在日期
    public static String getNowDate(){


        //格式化日期实例sdf
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        //创建Calendar 对象
        Calendar calendar = Calendar.getInstance();
        Date finalDate = calendar.getTime();

        return sdf.format(finalDate);
    }

    public static String getNowDate(String format){


        //格式化日期实例sdf
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //创建Calendar 对象
        Calendar calendar = Calendar.getInstance();
        Date finalDate = calendar.getTime();

        return sdf.format(finalDate);
    }

    public static String getNowTime(){

        //格式化时间实例sdf
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //创建Calendar 对象
        Calendar calendar = Calendar.getInstance();
        Date finalDate = calendar.getTime();

        return sdf.format(finalDate);

    }

    /**
     * long转string
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * long转date
     * @param currentTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * date转string
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /**
     * string转date
     * @param strTime
     * @param formatType
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }
    //根据日期获得星期

    public static String getWeek(String nowDate){

        String Week = "星期";

        if (nowDate.length()==0||nowDate==null){
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {


            c.setTime(format.parse(nowDate));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }

        return Week;

    }

    public static String getWeek(){
        String nowDate=getNowDate("yyyy-MM-dd");
        String week = getWeek(nowDate);
        return week;
    }

}
