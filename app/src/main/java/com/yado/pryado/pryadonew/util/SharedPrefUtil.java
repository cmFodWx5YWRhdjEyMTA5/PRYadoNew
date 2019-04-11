package com.yado.pryado.pryadonew.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eado on 2016/3/24.
 */
public class SharedPrefUtil {
    private static SharedPreferences sp;
    private static SharedPrefUtil instance = new SharedPrefUtil();

    public SharedPrefUtil() {
    }

    public synchronized static SharedPrefUtil getInstance(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("preado", Context.MODE_PRIVATE);
        }
        return instance;
    }

    @SuppressLint("ApplySharedPref")
    public void saveObject(String string, Object object) {
        if (object instanceof String) {
            sp.edit().putString(string, (String) object).commit();
        } else if (object instanceof Boolean) {
            sp.edit().putBoolean(string, (Boolean) object).commit();
        } else if (object instanceof Integer) {
            sp.edit().putInt(string, (Integer) object).commit();
        }
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void setIpList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        remove(tag);
        sp.edit().putString(tag, strJson).commit();
    }

    public List<String> getIpList(String tag) {
        List<String> datalist = new ArrayList<>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }

        Gson gson = new Gson();
        datalist = gson.fromJson( strJson, new TypeToken<List<String>>() {}.getType());
        return datalist;
    }


    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public <T> T getT(String key, T defValue) {
        T t = null;
        if (defValue instanceof Boolean) {
            Boolean value = sp.getBoolean(key, (Boolean) defValue);
            t = (T) value;
        } else if (defValue instanceof String || defValue == null) {
            String value = sp.getString(key, (String) defValue);
            t = (T) value;
        } else if (defValue instanceof Integer) {
            Integer value = sp.getInt(key, (Integer) defValue);
            t = (T) value;
        }
        return t;
    }

    public void remove(String key) {
        sp.edit().remove(key).commit();
    }
    public void removeAll() {
        sp.edit().clear().commit();
    }
}