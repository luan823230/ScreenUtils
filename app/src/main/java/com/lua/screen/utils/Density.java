package com.lua.screen.utils;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Density {
    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;
    private static int barHeight;

    public static void setDensity(@NonNull final Activity application){
        //获取application的DisplayMetrics
        appDisplayMetrics=application.getResources().getDisplayMetrics();
        //获取状态栏高度
        barHeight=AppUtils.getStateBar2(application);

        if(appDensity==0){
            //初始化的时候赋值
            appDensity=appDisplayMetrics.density;
            appScaledDensity=appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks(){
                @Override
                public void onConfigurationChanged(Configuration newConfig){
                    //字体改变后,将appScaledDensity重新赋值
                    if(newConfig!=null&&newConfig.fontScale>0){
                        appScaledDensity=application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory(){
                }
            });
        }
    }

    //此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好了)
    public static void setDefault(Activity activity){
        setAppOrientation(activity,AppUtils.HEIGHT);
    }

    //此方法用于在某一个Activity里面更改适配的方向
    public static void setOrientation(Activity activity, String orientation){
        setAppOrientation(activity,orientation);
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     * <p>
     * orientation:方向值,传入width或height
     */
    private static void setAppOrientation(@Nullable Activity activity, String orientation){

        float targetDensity;

        if(orientation.equals("height")){
            targetDensity=(appDisplayMetrics.heightPixels-barHeight)/1280f;
        }else{
            targetDensity=appDisplayMetrics.widthPixels/800f;
        }

        float targetScaledDensity=targetDensity*(appScaledDensity/appDensity);
        int targetDensityDpi=(int)(160*targetDensity);

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */

        DisplayMetrics activityDisplayMetrics=activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density=targetDensity;
        activityDisplayMetrics.scaledDensity=targetScaledDensity;
        activityDisplayMetrics.densityDpi=targetDensityDpi;
    }

}
