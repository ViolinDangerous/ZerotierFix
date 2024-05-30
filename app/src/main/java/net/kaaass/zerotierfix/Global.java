package net.kaaass.zerotierfix;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import net.kaaass.zerotierfix.events.StartEvent;
import net.kaaass.zerotierfix.events.StopEvent;
import net.kaaass.zerotierfix.service.ZeroTierOneService;

import org.greenrobot.eventbus.EventBus;

public class Global {
    private static Application app;
    private static boolean isRunning = false;

    public static void init(Application app) {
        Global.app = app;
    }

    public static Application app() {
        return app;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void startZt1Service() {
        if (app() == null) return;

        var intent = new Intent(app(), ZeroTierOneService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            app().startForegroundService(intent);
        } else {
            app().startService(intent);
        }

        isRunning = true;
        EventBus.getDefault().post(new StartEvent());
    }

    public static void startZt1Service(long networkId) {
        if (app() == null) return;

        var intent = new Intent(app(), ZeroTierOneService.class);
        intent.putExtra(ZeroTierOneService.ZT1_NETWORK_ID, networkId);
        if (Build.VERSION.SDK_INT >= 26) {
            app().startForegroundService(intent);
        } else {
            app().startService(intent);
        }

        isRunning = true;
        EventBus.getDefault().post(new StartEvent());
    }

    public static void stopZt1Service() {
        if (app() == null) return;

        var intent = new Intent(app(), ZeroTierOneService.class);
        app().stopService(intent);

        isRunning = false;
        EventBus.getDefault().post(new StopEvent());
    }

}
