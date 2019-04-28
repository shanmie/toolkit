package cn.org.toolkit.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deacon
 * @since 2019/4/28
 */
public class ListUtility {
    public static String[] get(String s){
        if (StringUtils.isBlank(s)){
            return new String[0];
        }
        return s.split(",");
    }
    public static <T> T getIndex(List<T> list,int index , T defaultVal){
        if (list != null && list.size() > index){
            return list.get(index);
        }
        return defaultVal;
    }
    public static List<String> getString(String s){
        List<String> list = new ArrayList<>();
        String[] strings = get(s);
        for (String str : strings) {
            list.add(str);
        }
        return list;
    }
    public static List<Long> getLong(String s){
        List<Long> list = new ArrayList<>();
        String[] strings = get(s);
        for (String str : strings) {
            list.add(NumberUtils.toLong(str,0));
        }
        return list;
    }
    public static List<Integer> getInteger(String s){
        List<Integer> list = new ArrayList<>();
        String[] strings = get(s);
        for (String str : strings) {
            list.add(NumberUtils.toInt(str,0));
        }
        return list;
    }
    public static String getStringFirst(String s,String defaultVal){
        String[] strings = get(s);
        if (strings.length <=0){
            return defaultVal;
        }
        return strings[0];
    }
    public static String getStringLast(String s,String defaultVal){
        String[] strings = get(s);
        if (strings.length <=0){
            return defaultVal;
        }
        return strings[0];
    }

    public static long getLongFirst(String s,long defaultVal){
        String[] strings = get(s);
        if (strings.length <=0){
            return defaultVal;
        }
        return NumberUtils.toLong(strings[0],defaultVal);
    }
    public static long getLongLast(String s, long defaultVal) {
        if (StringUtils.isNotBlank(s)) {
            int index = s.lastIndexOf(',');
            if (index > 0) {
                s = s.substring(index+1);
            }
            return NumberUtils.toLong(s.trim(), defaultVal);
        }
        return defaultVal;
    }

    public static int getIntegerFirst(String s,int defaultVal){
        String[] strings = get(s);
        if (strings.length <=0){
            return defaultVal;
        }
        return NumberUtils.toInt(strings[0],defaultVal);
    }
    public static int getIntegerLast(String s, int defaultVal) {
        if (StringUtils.isNotBlank(s)) {
            int index = s.lastIndexOf(',');
            if (index > 0) {
                s = s.substring(index+1);
            }
            return NumberUtils.toInt(s.trim(), defaultVal);
        }
        return defaultVal;
    }

}
