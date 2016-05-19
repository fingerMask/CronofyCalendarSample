package com.ileja.calendar.cronofy;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chentao on 16/5/16.
 */
public class PrefHelper {
    private static final String Calender = "calender";
    private static final String App_Code = "AppCode";
    private static final String Access_Token = "AccessToken";
    private static final String Refresh_Token = "RefreshToken";
    private static final String Expire_Time = "ExpireTime";
    private static final String Token_Time = "TokenTime";


    private static final SharedPreferences getCalenderSharedPref(Context context){
        SharedPreferences sp = context.getSharedPreferences(Calender, Context.MODE_PRIVATE);

        return sp;
    }

    /**
     * 获取appCode
     * @param context
     * @return
     */
    public static final String getCode(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            return sp.getString(App_Code, "");
        }

        return "";
    }

    /**
     * 设置appCode
     * @param context
     * @param appCode
     */
    public static void setCode(Context context, final String appCode){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(App_Code, appCode);
            editor.apply();
        }
    }

    /**
     * 获取accessToken
     * @param context
     * @return
     */
    public static final String getAccessToken(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            return sp.getString(Access_Token, "");
        }

        return "";
    }

    /**
     * 设置accessToken
     * @param context
     * @param accessToken
     */
    public static void setAccessToken(Context context, final String accessToken){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Access_Token, accessToken);
            editor.apply();
        }
    }

    /**
     * 获取refreshToken
     * @param context
     * @return
     */
    public static final String getRefreshToken(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            return sp.getString(Refresh_Token, "");
        }

        return "";
    }

    /**
     * 设置refreshToken
     * @param context
     * @param refreshToken
     */
    public static void setRefreshToken(Context context, final String refreshToken){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Refresh_Token, refreshToken);
            editor.apply();
        }
    }

    /**
     * 获取expireTime
     * @param context
     * @return
     */
    public static final long getExpireTime(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            return sp.getLong(Expire_Time, 0);
        }

        return 0;
    }

    /**
     * 设置expireTime
     * @param context
     * @param expireTime
     */
    public static void setExpireTime(Context context, final long expireTime){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(Expire_Time, expireTime);
            editor.apply();
        }
    }

    /**
     * 获取tokenTime
     * @param context
     * @return
     */
    public static final long getTokenTime(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            return sp.getLong(Token_Time, 0);
        }

        return 0;
    }

    /**
     * 设置tokenTime
     * @param context
     */
    public static void setTokenTime(Context context){
        SharedPreferences sp = getCalenderSharedPref(context);
        if(sp != null){
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(Token_Time, System.currentTimeMillis());
            editor.apply();
        }
    }
}
