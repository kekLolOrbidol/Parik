package kristi.heroofmythhaven;

import android.app.Application;

import com.onesignal.OneSignal;

public class App extends Application {
    @Override
    public void onCreate() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        super.onCreate();
    }
}
