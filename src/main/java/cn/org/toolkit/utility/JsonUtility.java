package cn.org.toolkit.utility;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * @author
 * @since 2019/4/12
 */
public class JsonUtility {

    /**
     * 当 json数据 key值动态变化时解析json array
     * 例如数据结构
     * { "data": {"123": [{"name": "bob"}] } }
     *
     * @param data    原始数据
     * @param dataKey 需要解析的data
     * @param keys    需要解析动态Array中的key值
     * @return
     */

    public static List<Map<String, Object>> dynamicParseArray(String data, String dataKey, String... keys) {
        JSONObject dataJson = JSONObject.parseObject(data);
        JSONObject jsonObject = JSONObject.parseObject(dataJson.getString(dataKey));
        return dynamicParseArray(jsonObject, jsonObject.keySet().iterator(), keys);
    }


    /**
     * 当json数据 key值动态变化时解析json
     * 例如数据结构
     * {"dataKey": {" 123": {"name": "bob"} } }
     *
     * @param data    原始json数据
     * @param dataKey 需要解析的data数据集
     * @param keys    data里面需要解析的字段
     * @return
     */
    public static List<Map<String, Object>> dynamicParse(String data, String dataKey, String... keys) {
        JSONObject dataJson = JSONObject.parseObject(data);
        JSONObject jsonObject = JSONObject.parseObject(dataJson.getString(dataKey));
        return dynamicParse(jsonObject, jsonObject.keySet().iterator(), keys);
    }

    /**
     * 获取java list
     *
     * @param data
     * @param key
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(String data, String key, Class<T> cls) {
        return JSONObject.parseObject(data).getJSONArray(key).toJavaList(cls);
    }


    private static List<Map<String, Object>> dynamicParse(JSONObject jsonObject, Iterator iterator, String... keys) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        while (iterator.hasNext()) {
            String dynamicKey = (String) iterator.next();
            String string = jsonObject.getString(dynamicKey);
            JSONObject val = JSONObject.parseObject(string);
            Map<String, Object> map = Maps.newHashMap();
            for (String key : keys) {
                Object vo = val.get(key);
                map.put(key, vo);
            }
            mapList.add(map);
        }
        return mapList;
    }

    private static List<Map<String, Object>> dynamicParseArray(JSONObject jsonObject, Iterator iterator, String... keys) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        while (iterator.hasNext()) {
            String dynamicKey = (String) iterator.next();
            JSONArray jsonArray = JSONObject.parseArray(jsonObject.getString(dynamicKey));
            for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> map = Maps.newHashMap();
                for (String key : keys) {
                    Object vo = jsonArray.getJSONObject(i).get(key);
                    map.put(key, vo);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }


}
