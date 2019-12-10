package com.hp.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateString {
    public static String getString(Date date,String s){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(s);
        String s1 = simpleDateFormat.format(date);
        return s1;
    }
    public static Date getDate(String s,String s1){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(s);
        Date date=null;
        try {
            date = simpleDateFormat.parse(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
