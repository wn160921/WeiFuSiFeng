package com.example.a25564.weifusifeng.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by 25564 on 2017/7/12.
 */

public class SharePreUtil {
    public static void setShareInt(Context context,String key,int value){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(key,value).apply();
    }
    public static int getShareInt(Context context,String keyname){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(keyname,-1);
    }
    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @param value 存储数据的value
     * @description shared保存String类型数据
     */
    public static void SetShareString(Context context,String keyname,String value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putString(keyname, value).commit();
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @description shared获取String类型数据
     */
    public static String GetShareString(Context context,String keyname){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String value = sp.getString(keyname, "");
        return value;
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @description shared获取boolean类型数据
     */
    public static boolean GetShareBoolean(Context context,String keyname){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        boolean value = sp.getBoolean(keyname, false);
        return value;
    }

    /**
     * @param context 上下文
     * @param keyname 存储数据的key
     * @param value 存储数据的value
     * @description shared保存boolean类型数据
     */
    public static void SetShareBoolean(Context context,String keyname,boolean value){
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        sp.edit().putBoolean(keyname, value).commit();
    }

}
