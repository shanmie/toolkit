package cn.org.toolkit.utility;

import java.util.Arrays;
import java.util.List;

/**
 * @author deacon
 * @since 2019/4/12
 */
public class ArrayUtility {
    /**
     * Object数组变新的数组
     * @param val  Object[]
     * @param val2 新的数组类型
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(Object[] val,T[] val2){
        int size = val.length;
        if (val2.length < size)
            return (T[]) Arrays.copyOf(val, size, val2.getClass());
        if (val2.length > size)
            val2[size] = null;
        if (size == 0)
            val2 = null;
        return val2;
    }


    /**
     * 将Array 变成 list
     * @param val
     * @param <T>
     * @return
     */
    public static <T> List toList(T[] val){
        return null;
    }
}
