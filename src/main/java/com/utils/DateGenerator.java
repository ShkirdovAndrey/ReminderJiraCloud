package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateGenerator {
    private static SimpleDateFormat bothDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static SimpleDateFormat onlyTime = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat onlyHour = new SimpleDateFormat("HH");
    private static SimpleDateFormat onlyDate = new SimpleDateFormat("dd/MM/yyyy");
    private static Date date;

    public static String getCurrentDateTime(){
        Calendar calendar = new GregorianCalendar();
        date = calendar.getTime();
        return bothDateTime.format(date);
    }
    public static String getPastDate(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        date = calendar.getTime();
        return onlyDate.format(date);
    }
    public static String getPastHour(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        date = calendar.getTime();
        return onlyHour.format(date);
    }

    public static String getFutureDate(int afterHowManyDays){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_WEEK,afterHowManyDays);
        date = calendar.getTime();
        return onlyDate.format(date);
    }
    public static String getFutureHour(int afterHowManyHours){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.HOUR_OF_DAY,afterHowManyHours); //Это может быть плохое решение или неточность
        date = calendar.getTime();
        return onlyHour.format(date);
    }

    public static String getTonightDateTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return bothDateTime.format(date);
    }

    public static String getTomorrowDateTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        date = calendar.getTime();
        return bothDateTime.format(date);
    }

    public static String getTomorrowDate(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        date = calendar.getTime();
        return onlyDate.format(date);
    }

    public static String getCurrentHour(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        date = calendar.getTime();
        return onlyTime.format(date);
    }
    public static String getCurrentOnlyHour(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        date = calendar.getTime();
        return onlyHour.format(date);
    }

    public static String getInAWeekDateTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        date = calendar.getTime();
        return bothDateTime.format(date);
    }

    public static String getInAMonthDateTime(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, 1);
        date = calendar.getTime();
        return bothDateTime.format(date);
    }
}
