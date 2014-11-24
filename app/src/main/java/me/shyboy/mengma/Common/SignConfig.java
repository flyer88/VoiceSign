package me.shyboy.mengma.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by foul on 14/11/16.
 */
public final class SignConfig {
    public static final int DBVERSION = 7;
    public static final String HOST = "http://www.flappyant.com/VoiceSignListener/public/";
    //public static final String HOST = "http://192.168.233.2/laravel/sign/public/";
    public static final String TABLEUSER = "User";
    public static final String URLLOGIN = HOST+"login";
    public static final String URLNEWSIGN = HOST+"sign/new";
    public static final String URLSIGNCHECK = HOST+"sign/check";
    public static final String URLISSIGNED = HOST+"sign/signed";
    public static final String URLLISTSIGN = HOST+"sign/list";
    public static final String URLSIGNDETAIL = HOST+"sign/detail";
    public static final String URLUSERLIST = HOST+"auth/list";
    public static final String URLUSERMANAGER = HOST+"auth/manager";
    public static final String TABLESIGN = "Sign";
    public static final String TABLEDETAIL = "SignDetail";
    public static final String TABLEMEMBER = "members";
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
