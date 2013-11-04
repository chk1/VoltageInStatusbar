package org.bweb.voltageinstatusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import java.util.Date;

/**
 * Created by Christoph on 04.11.13.
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {
    private BatteryBroadcastCallback callback;

    public BatteryBroadcastReceiver() {
        // nothing
    }

    public interface BatteryBroadcastCallback {
        public void onBatteryBroadcastReceived(Intent intent);
    }

    public BatteryBroadcastReceiver(BatteryBroadcastCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        callback.onBatteryBroadcastReceived(intent);
    }
}
