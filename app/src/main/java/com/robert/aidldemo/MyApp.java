package com.robert.aidldemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

public class MyApp extends Application {

    private static final String TAG = MyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //可以根据不同的process初始化不同的东西
        Log.e(TAG, "onCreate: " + getProcessName());
    }

    private String getProcessName() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
                if (runningAppProcess.pid == Process.myPid()) {
                    return runningAppProcess.processName;
                }
            }
        }
        return "";
    }
}
