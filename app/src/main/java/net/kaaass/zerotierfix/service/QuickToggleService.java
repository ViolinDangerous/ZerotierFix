package net.kaaass.zerotierfix.service;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import net.kaaass.zerotierfix.Global;
import net.kaaass.zerotierfix.R;
import net.kaaass.zerotierfix.events.StartEvent;
import net.kaaass.zerotierfix.events.StopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickToggleService extends TileService {

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null) return;

        if (tile.getState() == Tile.STATE_ACTIVE) {
            stopZeroTierService();
        } else if (tile.getState() == Tile.STATE_INACTIVE) {
            startZeroTierService();
        }
    }

    private void stopZeroTierService() {
        Global.stopZt1Service();
    }

    private void startZeroTierService() {
        Global.startZt1Service();
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile == null) return;

        tile.setState(Global.isRunning() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.setIcon(Icon.createWithResource(this, R.drawable.logo_tile));

        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        EventBus.getDefault().register(this);
        updateTile();
    }

    @Override
    public void onStopListening() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopEvent(StopEvent stopEvent) {
        updateTile();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartEvent(StartEvent startEvent) {
        updateTile();
    }
}
