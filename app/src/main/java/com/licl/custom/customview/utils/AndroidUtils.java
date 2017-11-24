package com.licl.custom.customview.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.util.List;

public abstract class AndroidUtils {


    /**
     * 判断当前是否在线，true表示在线
     *
     * @param ctx
     * @return
     */
    public static boolean isOnline(Context ctx) {
        boolean flag = false;
        try {
            ConnectivityManager cwjManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = cwjManager.getActiveNetworkInfo();
            if (null == networkinfo || !networkinfo.isAvailable()) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
        }
        return flag;
    }


    public static float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    /**
     * 获取手机IMEI
     *
     * @return String
     */
    public static String getIMEI(Context context) {
        String imei = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        return imei;
    }

    /**
     * 获取手机imis
     *
     * @return String‰
     */
    public static String getImsi(Context context) {
        String imsi = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        imsi = tm.getSubscriberId();

        if (imsi == null || imsi.length() == 0) {
            imsi = "";
        }
        return imsi;
    }

    //获取手机的mac地址；
    public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = null;
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress())) {
                    macAddress = info.getMacAddress().replace(":", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (macAddress == null) {
            macAddress = "000000000000";
        }
        return macAddress;
    }

    /**
     * 状态栏高度
     *
     * @param context Context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public static void hideInputMothed(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) context)
                .getCurrentFocus().getWindowToken(), 0);
    }

    public static void showInputMothed(final Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isAppInstalled(Context ctx, String packageName) {
        try {
            ctx.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getGoogleAdvertiserID() {
        return "";
    }

    public static boolean sdcardReady() {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        return true;
    }

    public static String getNetworkType(Context context) {
        String strNetworkType = "none";
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                int type = networkInfo.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "wifi";
                } else if (type == ConnectivityManager.TYPE_ETHERNET) {
                    strNetworkType = "ethernet";
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String subTypeName = networkInfo.getSubtypeName();
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            strNetworkType = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            strNetworkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            strNetworkType = "4G";
                            break;
                        default:
                            // 中国移动 联通 电信 三种3G制式
                            if ("TD-SCDMA".equalsIgnoreCase(subTypeName) || "WCDMA".equalsIgnoreCase(subTypeName)
                                    || "CDMA2000".equalsIgnoreCase(subTypeName)) {
                                strNetworkType = "3G";
                            } else if ("FDD-LTE".equalsIgnoreCase(subTypeName)) {
                                strNetworkType = "4G";
                            } else {
                                strNetworkType = "unknown";
                            }

                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strNetworkType;
    }

    /**
     * 获取运营商名称
     *
     * @return unknown/CMCC(中国移动)/CU(中国联通)/CT(中国电信)
     */
    public static String getSimOperatorName(Context context) {
        String imsi = getImsi(context);
        String son = null;
        if (TextUtils.isEmpty(imsi)) {
            son = "unknown";
        } else if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) { //中国移动
            son = "CMCC";
        } else if (imsi.startsWith("46001")) { //中国联通
            son = "CU";
        } else if (imsi.startsWith("46003")) { //中国电信
            son = "CT";
        } else {
            son = "unknown";
        }
        return son;
    }

    public static Pair<Integer, Integer> getScreenSize(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return Pair.create(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * 当前进程包名
     *
     * @param context Context
     * @return String
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 判断是否为主进程
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isMainProcess(Context context) {
        String curProcessName = getCurProcessName(context);
        return (!TextUtils.isEmpty(curProcessName) && context.getPackageName().equals(curProcessName));
    }

    /**
     * 格式化文件大小
     * @param fileSize 大小(b)
     * @return K/M
     */
    public static String formatFielSize(long fileSize) {
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        String unit;
        float size = fileSize * 1.0f / 1024;
        if (size / 1024 < 0.1) {
            unit = " K";
        } else {
            size = size / 1024;
            unit = " M";
        }
        if(size <= 0f){
            return 0 + unit;
        }
        return decimalFormat.format(size) + unit;
    }

    /**
     * 安装应用
     * @param uri
     */
    public static void installApk(Context context, Uri uri){
        if(uri == null){
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    /**
     * 判断是否是横屏
     */

    public static boolean isScreenChange(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            return false;
        }
        return false;
    }

    /**
     * 判断Android 版本是否小于5.0大于等于4.4+
     */
    public static boolean isAndroidVeriosnJudge() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        }
        return false;
    }
    /**
     * 判断应用是否已经启动
     * @param context 一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
