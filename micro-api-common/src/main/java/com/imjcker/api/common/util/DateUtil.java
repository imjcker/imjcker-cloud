package com.imjcker.api.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author yezhiyuan
 * @version V1.0
 * @Title: 时间操作工具类
 * @Package com.lemon.common.util
 * @date 2017年3月20日 上午11:16:40
 */
public class DateUtil {

    /*
     * 将时间戳转换为时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long lt = new Long(s);
        long lt = Long.parseLong(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampLongToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间，格式：yyyy-MM-dd HH:mm:ss:sss
     * 作为版本号
     * @author kiana
     */
    public static String stampToDates(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        long lt = new Long(s);
        long lt = Long.parseLong(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间，格式：yyyy-MM-dd
     */
    public static String stampToDateNoTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        long lt = new Long(s);
        long lt = Long.parseLong(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间，格式：yyyy-MM-dd
     */
    public static String stampToDateNoTime(long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Date strToDate(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(str);
    }

    /*
     * 将日期转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    /*
     * 将时间戳转换为时间，格式：yyyy-MM-dd
     */
    public static String stampToTimeNoDate(long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    /*
     * 得到今天的时间
     */
    public static String getTodayTime() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        String day = format.format(today);
        return day;
    }

    /*
     * 得到今天的日期
     */
    public static String getTodayDate() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String day = format.format(today);
        return day;
    }

    /**
     * @param
     * @Description : 得到昨天的日期
     * @Return : java.lang.String
     * @Date : 2020/4/2 8:53
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    /*
     * 得到给定时间戳日期前一天的最后一秒的时间戳
     */
    public static String dateToStampLastMin(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime() - 1000;
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 得到今天的日期
     */
    public static String getToday() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String day = format.format(today);
        return day;
    }

    public static String getTodayWithPattern(String pattern) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String day = format.format(today);
        return day;
    }

    /**
     * 当天的开始时间
     *
     * @return
     */
    public static Date startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * 得到明天的日期
     */
    @SuppressWarnings("static-access")
    public static String getNextDay() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 时间转为时间戳
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static String timeToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) throws ParseException {
        Calendar c = Calendar.getInstance();
        Date date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /*
     * 得到n天前（负）后（正）的时间戳
     */
    @SuppressWarnings("static-access")
    public static String getBeforeTimestamp(Integer n) throws ParseException {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, n);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateToStamp(dateString);
    }

    /**
     * 获取一个月前的当前时刻时间戳
     *
     * @param currentTime
     * @return
     */
    public static Long getOneMonthAgo(Long currentTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(currentTime));
        c.add(Calendar.MONTH, -1);
        return c.getTime().getTime();
    }

    /*
     * 获取指定日期前一天的最后时间戳(13位时间戳)
     */
    public static long dateToStampLastMill(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = simpleDateFormat.parse(s);
        long ts = date.getTime() - 1;
        return ts;
    }

    /**
     * 获取timeMills指定日期当月起始时间
     * num=1表示下个月起始时间戳
     *
     * @return
     * @throws ParseException
     */
    public static long monthTimeInMillis(int num, Long timeMills) throws ParseException {
        Date date = null;
        if (null == timeMills)
            date = new Date();
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date(timeMills);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + num);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;
    }

    /**
     * 获取timeMill所在年初起始时间，num=1表示明年开始时间戳
     *
     * @return
     * @throws ParseException
     */
    public static long yearTimeInMillis(int num, Long timeMills) throws ParseException {
        Date date = null;
        if (null == timeMills)
            date = new Date();
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date(timeMills);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + num);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time;

    }

    /**
     * 获取timeMills所在天的起始时间戳
     *
     * @param num
     * @param timeMills
     * @return
     */
    public static Long dayTimeInMillis(int num, Long timeMills) {
        Date date = null;
        if (null == timeMills)
            date = new Date();
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = new Date(timeMills);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + num);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取两个时间戳之间相差的天数
     *
     * @param endTime
     * @param startTime
     * @return
     */
    public static int getDayBetween(long startTime, long endTime) {
        long n = endTime - startTime;
        return (int) (n / (24 * 60 * 60 * 1000L));
    }
}
