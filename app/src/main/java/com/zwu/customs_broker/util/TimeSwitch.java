package com.zwu.customs_broker.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeSwitch {
    public static String timeTips(String Timetype) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        switch (Timetype) {
            case "今天":
                calendar.setTime(new Date());
                Date td = calendar.getTime();
                String today = format.format(td);
                return today;
            case "最近一天":
                //最近一天
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -1);
                Date d = calendar.getTime();
                String day = format.format(d);
                return day;
            case "最近一周":
                //过去七天
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, -7);
                Date d7 = calendar.getTime();
                String day7 = format.format(d7);
                return day7;
            case "最近一月":
                //过去一月
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -1);
                Date m = calendar.getTime();
                String mon = format.format(m);
                return mon;
            case "最近半年":
                //最近半年
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -6);
                Date m6 = calendar.getTime();
                String mon6 = format.format(m6);
                return mon6;
            default:
                //过去一年
                calendar.setTime(new Date());
                calendar.add(Calendar.YEAR, -1);
                Date y = calendar.getTime();
                String year = format.format(y);
                return year;
        }








/*
        //过去三个月
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -3);
        Date m3 = calendar.getTime();
        String mon3 = format.format(m3);
        //System.out.println("过去三个月："+mon3);*/

    }
}
