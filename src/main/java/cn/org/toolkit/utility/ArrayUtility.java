package cn.org.toolkit.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author
 * @since 2019/4/12
 */
public class ArrayUtility {
    /**
     * 将Array 变成 list
     * @param val
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(T[] val){
        List<T> list = new ArrayList<>();
        Collections.addAll(list,val);
        return list;
    }
}
