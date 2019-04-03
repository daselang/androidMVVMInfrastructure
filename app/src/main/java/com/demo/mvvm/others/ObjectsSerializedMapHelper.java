package com.demo.mvvm.others;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 参考 by https://github.com/Jay-YaoJie/DbHelpeForAndroid.git
 * T对象转换成amp对象
 * 或者map对象转换为T对象
 * <p>
 * user to map or map to user
 * <p>
 * public class ObjectsSerializedMapHelper<T> {
 * private T object;
 * private Gson gson;
 * <p>
 * /**
 * 把T对象转换为Map对象
 *
 * @param
 * @return Map<Object                               ,                               Object>
 */
public class ObjectsSerializedMapHelper<T> {
    private Gson gson;

    /**
     * 把T对象转换为Map对象
     *
     * @param tClass T对象
     * @return Map<Object       ,       Object>
     */
    public Map<String, Object> getObjectMap(T tClass) {
        try {
            if (gson == null) {
                gson = new Gson();
            }
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();

            String jsonStr = gson.toJson(tClass);
            return gson.fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Map对象转换为T对象
     *
     * @param objectMap 　Map<Object, Object>对象
     * @param object    　Class<T> 对象
     * @return T对象
     */
    public T getT(Map<Object, Object> objectMap, Class<T> object) {
        try {

            if (gson == null) {
                gson = new Gson();
            }
            String jsonStr = gson.toJson(objectMap);
            return gson.fromJson(jsonStr, object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    List<T> tList = null;

    /**
     * 把对象转换为list对象
     *
     * @param tClass
     * @return
     */
    public List<T> setListT(T tClass) {

        try {
            if (tList == null) {
                tList = new ArrayList<T>();
            }
            tList.add(tClass);
            return tList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得单个T对象
     *
     * @return T 对象
     */
    public T getObjectList() {
        try {
            if (tList != null && tList.size() > 0) {
                return tList.get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
