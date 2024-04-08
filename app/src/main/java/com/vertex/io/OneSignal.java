//package com.vertex.io;
//
//import android.app.Application;
//import android.content.RestrictionsManager;
//import android.os.Build;
//
//import com.bumptech.glide.GlideBuilder;
//import com.onesignal.Continue;
//import com.onesignal.ContinueResult;
//import com.onesignal.debug.LogLevel;
//
//
//public class OneSignal extends Application {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        final String ONESIGNAL_APP_ID = "3b23c073-ecac-48ad-ae9a-cb972206c4a2";
//
//        assert OneSignal.getDebug() != null;
//        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE.ordinal());
//
//        assert OneSignal.getNotifications() != null;
//        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
//            if (r.isSuccess()) {
//                if (r.getData()) {
//                    // `requestPermission` completed successfully and the user has accepted permission
//                }
//                else {
//                    // `requestPermission` completed successfully but the user has rejected permission
//                }
//            }
//            else {
//                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
//            }
//        }));
//
//    }
//
//    private static RestrictionsManager getNotifications() {
//        return null;
//    }
//
//    private static GlideBuilder getDebug() {
//        return null;
//    }
//
//    private static void getProcessName(OneSignal oneSignal, String onesignalAppId) {
//
//    }
//}
