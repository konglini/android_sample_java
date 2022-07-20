/**
 * <pre>
 * Preferences 관리 Class
 * </pre>
 * <p/>
 * store.woongjinnet.commonview CLPreferences.java
 *
 * @version : v1.0.0
 * @author : Calix
 * @since : 2013.2013. 10. 4.
 */
package com.example.gurudevkwt.sensormanager.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SimplePreferences {
    /**
     * <pre>
     * 데이터를 Preferences를 이용하여 저장한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 꺼내올 Key값
     * @param _value : 저장할 데이터
     *
     * <pre></pre>
     */
    public static void setPreferences(Context _context, String _key, Object _value) {
        if (_context == null)
            return;

        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        if (_value == null)
            throw new NullPointerException("_value is not null (_value value = " + _value);

        String packageName = _context.getPackageName();

        SharedPreferences prefSaveID = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefSaveID.edit();
        // 이미 값이 저장되어있다면 지운다.
        if (editor.equals(_key))
            removePreferences(_context, _key);

        // 정보를 해당 형식에 맞게 저장
        if (_value instanceof Boolean)
            editor.putBoolean(_key, (Boolean) _value);
        else if (_value instanceof String) {
            _value = ((String) _value).trim();
            editor.putString(_key, (String) _value);
        } else if (_value instanceof Integer)
            editor.putInt(_key, (Integer) _value);
        else if (_value instanceof Float)
            editor.putFloat(_key, (Float) _value);
        else if (_value instanceof Long)
            editor.putLong(_key, (Long) _value);

        // 수정한 정보를 반영
        editor.commit();
    }

    /**
     * <pre>
     * 디비에 저장되어있는 해당하는 키값의 정보를 지운다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 지울 Key값
     *
     * <pre></pre>
     */
    public static void removePreferences(Context _context, String _key) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences prefSaveID = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefSaveID.edit();
        // 값을 저장 또는 변경.
        editor.remove(_key);
        // 수정한 정보를 반영
        editor.commit();
    }

    /**
     * <pre>
     * Boolean 값을 반환한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @return <pre>
     * 해당하는 키값이 없으면 [false] 반환
     * </pre>
     */
    public static boolean getBooleanPreference(Context _context, String _key) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences pref = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return pref.getBoolean(_key, false);
    }

    /**
     * <pre>
     * String 데이터를 반환한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @return <pre>
     * 해당하는 키값이 없으면 공백("") 반환
     * </pre>
     */
    public static String getStringPreference(Context _context, String _key) {
        return getStringPreference(_context, _key, "");
    }

    /**
     * <pre>
     *
     * </pre>
     *
     * @since 2014. 6. 12.
     * @version v1.0.0
     * @author hyunjinkang
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @param _strDefValue : 해당하는 key 값이 없을경우 반환 값
     * @return <pre></pre>
     */
    public static String getStringPreference(Context _context, String _key, String _strDefValue) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences pref = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return pref.getString(_key, _strDefValue);
    }

    /**
     * <pre>
     * Integet 데이터를 반환한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @return <pre>
     * 해당하는 키값이 없다면 [0] 반환
     * </pre>
     */
    public static Integer getIntegerPreference(Context _context, String _key) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences pref = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return pref.getInt(_key, 0);
    }

    /**
     * <pre>
     * Float 데이터를 반환한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @return <pre>
     * 해당하는 키값이 없다면 [-1f] 반환
     * </pre>
     */
    public static Float getFloatPreference(Context _context, String _key) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences pref = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return pref.getFloat(_key, -1f);
    }

    /**
     * <pre>
     * Long 데이터를 반환한다.
     * </pre>
     *
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param _context
     * @param _key : 데이터를 가져올 Key값
     * @return <pre>
     * 해당하는 키값이 없다면 [-1L]반환
     * </pre>
     */
    public static Long getLongPreference(Context _context, String _key) {
        if (_key == null)
            throw new NullPointerException("_key is not null (_key value = " + _key);

        String packageName = _context.getPackageName();

        SharedPreferences pref = _context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        return pref.getLong(_key, -1L);
    }
}
