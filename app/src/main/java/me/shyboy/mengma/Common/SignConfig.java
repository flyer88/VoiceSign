package me.shyboy.mengma.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by foul on 14/11/16.
 */
public final class SignConfig {
    public static final int DBVERSION = 5;
    public static final String TABLEUSER = "User";
    public static final String URLLOGIN = "http://192.168.233.2/laravel/sign/public/login";
    public static final String URLNEWSIGN = "http://192.168.233.2/laravel/sign/public/sign/new";
    public static final String URLSIGNCHECK = "http://192.168.233.2/laravel/sign/public/sign/check";
    public static final String URLISSIGNED = "http://192.168.233.2/laravel/sign/public/sign/signed";
    public static final String URLLISTSIGN = "http://192.168.233.2/laravel/sign/public/sign/list";
    public static final String URLSIGNDETAIL = "http://192.168.233.2/laravel/sign/public/sign/detail";
    public static final String TABLESIGN = "Sign";
    public static final String TABLEDETAIL = "SignDetail";
    public static final int SNOLENGTH = 8;
    public static final int PASSWORDMINLENGTH = 6;
    public static final int PASSWORDMAXLENGTH = 16;
    public static final int HTTPERRORCODEOK = 2000;
    public static final int HTTPERRORCODE_FAILED = 5000;
    public static final int HTTPERRORCODE_OUTOFDATE = 4010;
    public static final int HTTPERRORCODE_NOSIGNEXISTS = 4008;
    public static final int HTTPERRORCODE_TOKENWRONG = 4007;
    //签到类型
    public static final int SIGN_TYPE_QR = 1;
    public static final int SIGN_TYPE_VOICE = 2;
    public static final int SIGN_TYPE_NFC = 3;

    public static final int HTTP_CODE_SIGNED = 4006;

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
