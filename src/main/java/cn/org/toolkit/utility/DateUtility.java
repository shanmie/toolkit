package cn.org.toolkit.utility;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.ZoneId.systemDefault;

/**
 * @author
 * @since 2019/4/12
 */
public class DateUtility {
    public static final String DEFAULT_FORMATTER = "yyyy-MM-dd HH:mm:ss";


    /**
     * 获取当前时间
     * @return
     */
    public static String now(){
        return format(new Date(),DEFAULT_FORMATTER,null);
    }

    /**
     * 获取当前时间 可变格式
     * @param formatter
     * @return
     */
    public static String now(String formatter){return format(new Date(),formatter ,null); }

    /**
     * 时间戳格式化输出 "yyyy-MM-dd HH:mm:ss"
     * @param val
     * @return
     */
    public static String toString(long val){
        return toString(new Date(val));
    }

    /**
     * 日期格式化输出 "yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String toString(Date date){
        return format(date,DEFAULT_FORMATTER,null);
    }

    /**
     * 字符串时间 变Date
     * @param date
     * @return
     */
    public static Date toDate(String date){ return format(date,DEFAULT_FORMATTER,null);}

    /**
     * 字符串时间 变Date 可变格式
     * @param date
     * @param formatter
     * @return
     */
    public static Date toDate(String date , String formatter){ return format(date,formatter,null);}

    /**
     * 时间戳变 Date
     * @param val
     * @return
     */
    public static Date toDate(long val){return new Date(val);}

    /**
     * Date日期 变时间戳
     * @param d
     * @return
     */
    public static long toMillis(Date d){return d.getTime();}


    /****************************** JDK8 *************************************/

    /**
     * JDK8
     * 时间戳变为 LocalDate
     * @param epochMilli
     * @return
     */
    public static LocalDate toLocalDate(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), systemDefault()).toLocalDate();
    }

    /**
     * JDK8
     * Date变为 LocalDate
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date) {
        return date == null ? null : toLocalDate(date.getTime());
    }

    /**
     * JDK8
     * string 格式日期 变 LocalDate
     * @param date
     * @return
     */
    public static LocalDate localDate(String date){
        return null;
    }


    /**
     * JDK8
     * LocalDateTime日期 变时间戳
     * @param t
     * @return
     */
    public static long toMillis(LocalDateTime t) {
        return t == null ? 0L : t.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * JDK8
     * LocalDate日期 变时间戳
     * @param t
     * @return
     */
    public static long toMillis(LocalDate t) {
        return t == null ? 0L : t.atStartOfDay().atZone(systemDefault()).toInstant().toEpochMilli();
    }

    /****************************** JDK8 *************************************/


    /**
     * 给某一个日期加上多少天
     *
     * @param date
     * @param day
     * @return
     */
    public static long plusDay(long date, long day) {
        day = day * 24 * 60 * 60 * 1000;
        date += day;
        return date;
    }



    /**
     * 某一个日期 时分秒 清空
     * @param val
     * @return
     */
    public static long reDateBeforDayNil(long val){
        return java.sql.Date.valueOf(toLocalDate(val)).getTime();
    }



    private static String format(Date src, String pattern, String defaultValue) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(src);
        } catch (Exception e) {
            // do nothing
        }
        return defaultValue;
    }

    private static Date format(String src, String pattern, Date defaultDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(src);
        } catch (Exception e) {
            // do nothing
        }
        return defaultDate;
    }
}
