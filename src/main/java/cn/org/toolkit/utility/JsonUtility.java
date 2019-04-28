package cn.org.toolkit.utility;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author
 * @since 2019/4/12
 */
public class JsonUtility {
    /**
     * 当 json数据 key值动态变化时
     * 解析json数据下的Array {"key":"[{ }]"}
     *
     * @param set        将需要解析的json 变成set
     * @param jsonObject 需要解析的json
     * @param key        需要解析动态Array中的key值
     * @return
     * @throws Exception
     */
    public static Set<String> dynamicParseArray(Set<String> set, JSONObject jsonObject, String key) {
        Set<String> newSet = new HashSet<>();
        Iterator iterator = set.iterator();
        dynamicParseArrayByJSON(key, jsonObject, newSet, iterator);
        return newSet;
    }

    /**
     *
     * @param data          数据
     * @param arrayKey      需要解析的Array的key
     * @param key           需要解析动态Array中的key值
     * @param innerArray    key对应的值 是数组还是json true为数组 反之json
     * @return
     */

    public static Set<String> dynamicParseArray(String data,String arrayKey,String key,boolean innerArray){
        JSONObject jb = JSONObject.parseObject(data);
        Set<String> newSet = new HashSet<>();
        if (innerArray){
            JSONArray val = jb.getJSONArray(arrayKey);
            for (int i = 0; i < val.size(); i++) {
                String vo = val.getJSONObject(i).getString(key);
                if (vo != null)
                    newSet.add(vo);
            }
        }else {
            JSONObject jsonObject = JSONObject.parseObject(jb.getString(arrayKey));
            Iterator iterator = jsonObject.keySet().iterator();
            dynamicParseArrayByJSON(key, jsonObject, newSet, iterator);
        }
        return newSet;
    }


    /**
     * 当 json数据 key值动态变化时
     * 解析json array 数据下的Array {"key":"[{ }]"}
     * @param val 需要解析的数组
     * @param key 需要解析动态Array中的key值
     * @return
     */
    public static Set<String> dynamicParseArray(JSONArray val, String key) {
        Set<String> newSet = new HashSet<>();
        for (int i = 0; i < val.size(); i++) {
            String vo = val.getJSONObject(i).getString(key);
            if (vo != null)
                newSet.add(vo);
        }
        return newSet;
    }


    /**
     * 当 json数据 key值动态变化时
     * 解析json数据下的普通key {"key":"val"}
     *
     * @param set        将需要解析的json 变成set
     * @param jsonObject 需要解析的json
     * @param key        需要动态解析的key
     * @return
     * @throws Exception
     */
    public static Set<?> dynamicParse(Set<String> set, JSONObject jsonObject, String key) {
        Set<String> newSet = new HashSet<>();
        Iterator iterator = set.iterator();
        dynamicParse(key, jsonObject, newSet, iterator);
        return newSet;
    }

    /**
     *
     * @param data      数据
     * @param dynKey    需要解析的动态key
     * @param key       需要解析动态Array中的key值
     * @return
     */
    public static Set<?> dynamicParse(String data, String dynKey, String key) {
        JSONObject jb = JSONObject.parseObject(data);
        JSONObject jsonObject = JSONObject.parseObject(jb.getString(dynKey));
        Set<String> newSet = new HashSet<>();
        dynamicParse(key, jsonObject, newSet, jsonObject.keySet().iterator());
        return newSet;
    }


    /**
     * 校验json数据的合法性
     *
     * @param val
     * @return
     */
    public boolean verify(String val) {
        try {
            JSONObject.parseObject(val);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(val);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取java list
     * @param data
     * @param key
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(String data , String key, Class<T> cls){
        return JSONObject.parseObject(data).getJSONArray(key).toJavaList(cls);
    }


    private static void dynamicParse(String key, JSONObject jsonObject, Set<String> newSet, Iterator iterator) {
        while (iterator.hasNext()) {
            String dynamicKey = (String) iterator.next();
            String string = jsonObject.getString(dynamicKey);
            JSONObject val = JSONObject.parseObject(string);
            String vo = val.getString(key);
            if (vo != null)
                newSet.add(vo);
        }
    }


    private static void dynamicParseArrayByJSON(String key, JSONObject jsonObject, Set<String> newSet, Iterator iterator) {
        while (iterator.hasNext()) {
            String dynamicKey = (String) iterator.next();
            JSONArray jsonArray = JSONObject.parseArray(jsonObject.getString(dynamicKey));
            for (int i = 0; i < jsonArray.size(); i++) {
                String vo = jsonArray.getJSONObject(i).getString(key);
                if (vo != null)
                    newSet.add(vo);
            }
        }
    }

}
