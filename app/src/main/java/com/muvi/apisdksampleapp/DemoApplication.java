package com.muvi.apisdksampleapp;

import android.app.Application;
import com.muvi.apisdksampleapp.util.LogUtil;
import com.release.muvisdk.api.APIUrlConstant;
import com.release.muvisdk.api.apiController.SDKInitializer;


/**
 * Created by BISHAL on 08-09-2017.
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());
        APIUrlConstant.BASE_URl="https://www.muvi.com/rest/";
        LogUtil.showLog("MUVI PCKG NAME", SDKInitializer.user_Package_Name_At_Api+"::::"+getPackageName());
    }
}
