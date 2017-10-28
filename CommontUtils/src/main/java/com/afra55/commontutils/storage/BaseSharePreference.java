package com.afra55.commontutils.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.R;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * Created by Afra55 on 2017/10/19.
 * Smile is the best name card.
 */

public class BaseSharePreference {
    public static final String PREFERENCE = "base_" + AppCache.getContext().getResources().getString(R.string.app_name) + "_SharePreference";
    private static BaseSharePreference instance = null;

    private SharedPreferences mPreference;

    public static BaseSharePreference getInstance() {
        if (instance == null) {
            instance = new BaseSharePreference();
        }
        return instance;
    }

    public BaseSharePreference() {
        if (AppCache.getContext() != null) {
            try {
                mPreference = AppCache.getContext().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SharedPreferences getPreference() {
        return mPreference;
    }

    /**
     * 设置文件数据
     *
     * @param key
     * @param value
     */
    public void setValue(String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            mPreference.edit().putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            mPreference.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            mPreference.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof Long) {
            mPreference.edit().putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            mPreference.edit().putFloat(key, (Float) value).apply();
        } else if (value instanceof Set) {
            //noinspection unchecked
            mPreference.edit().putStringSet(key, (Set<String>) value).apply();
        }
    }

    /**
     * @param key
     * @param value 初始化默认数据反馈
     * @return
     */
    public boolean getBooleanValue(String key, boolean... value) {
        if (value != null && value.length > 0) {
            boolean result = value[0];
            return mPreference.getBoolean(key, result);
        }
        return mPreference.getBoolean(key, false);
    }

    public float getFloatValue(String key, Float... value) {
        if (value != null && value.length > 0) {
            float result = value[0];
            return mPreference.getFloat(key, result);
        }
        return mPreference.getFloat(key, 0f);
    }

    public int getIntValue(String key, int... value) {
        if (value != null && value.length > 0) {
            int result = value[0];
            return mPreference.getInt(key, result);
        }
        return mPreference.getInt(key, 0);
    }

    public long getLongValue(String key, Long... value) {
        if (value != null && value.length > 0) {
            long result = value[0];
            return mPreference.getLong(key, result);
        }
        return mPreference.getLong(key, 0l);
    }

    public String getStringValue(String key, String... value) {
        if (value != null && value.length > 0) {
            String result = value[0];
            return mPreference.getString(key, result);
        }
        return mPreference.getString(key, "");
    }

    public Set<String> getSetValue(String key) {
        return mPreference.getStringSet(key, null);
    }


    public void setObject(String key, Object obj) {
        String info = JSONObject.toJSONString(obj);
        setValue(key, info);
    }

    public Object getObject(String key, Class cls) {
        if (!getStringValue(key, "").equals("")) {
            return JSONObject.parseObject(getStringValue(key, ""), cls);
        }
        return null;
    }

    public void clearAll() {
        try {
            mPreference.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
