package com.qiang.practice.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: 时间工具类
 */
@SuppressWarnings("all")
public class DateUtil {

    /**
     * 获取指定时间与现在时间的天数差值
     * @param date
     */
    public static int getDayDiff(Date date) {
        Long timeMillisDiff = getTimeMillisDiff(date);
        return (int) (timeMillisDiff / (1000 * 60 * 60 * 24));
    }

    /**
     * 获取指定时间与现在时间的小时差值
     * @param date
     */
    public static int getHourDiff(Date date) {
        Long timeMillisDiff = getTimeMillisDiff(date);
        return (int) (timeMillisDiff / (1000 * 60 * 60));
    }

    /**
     * 获取指定时间与现在时间的分钟差值
     * @param date
     */
    public static int getMiniteDiff(Date date) {
        Long timeMillisDiff = getTimeMillisDiff(date);
        return (int) (timeMillisDiff / (1000 * 60));
    }

    /**
     * 获取系统时间毫秒与指定时间的毫秒差值
     *
     * @param date 指定时间
     * @return
     */
    private static Long getTimeMillisDiff(Date date) {
        return System.currentTimeMillis() - date.getTime();
    }

    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getAllTime(Date date) {
        String todySDF = "HH:mm";
        String yesterDaySDF = "昨天";
        String beforYesterDaySDF = "前天";
        String otherYearSDF = "yyyy年MM月dd日 HH:mm";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return time;
        } else {
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                sfd = new SimpleDateFormat(todySDF);
                time = yesterDaySDF + " " + sfd.format(date);

                return time;
            }
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是前天

                sfd = new SimpleDateFormat(todySDF);
                time = beforYesterDaySDF + " " + sfd.format(date);
                return time;
            }
        }

        //是前天之前
        sfd = new SimpleDateFormat(otherYearSDF);
        time = sfd.format(date);

        return time;
    }

    // 将传入时间与当前时间进行对比，是否今天\昨天\前天\同一年
    public static String getPartTime(Date date) {
        String todySDF = "HH:mm";
        String yesterDaySDF = "昨天";
        String beforYesterDaySDF = "EEEE";
        String otherYearSDF = "yy/MM/dd";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(now);
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        if (dateCalendar.after(todayCalendar)) {// 判断是不是今天
            return new SimpleDateFormat(todySDF).format(date);
        } else {
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是昨天
                return yesterDaySDF;
            }
            todayCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(todayCalendar)) {// 判断是不是前天

                return new SimpleDateFormat(beforYesterDaySDF).format(date);
            }
        }

        //是前天之前
        sfd = new SimpleDateFormat(otherYearSDF);
        time = sfd.format(date);

        return time;
    }

    public static String getTimeString(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date);
    }

    public static Date newDateIfNull(Date date) {
        return Objects.isNull(date) ? new Date() : date;
    }
}
