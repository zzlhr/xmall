package com.lhrsite.xshop.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    public static Date addDay(Date date, int dayNumber){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,dayNumber); //把日期往后增加一天,整数  往后推,负数往前移动
        return calendar.getTime(); //这个时间就是日期往后推一天的结果
    }



}
