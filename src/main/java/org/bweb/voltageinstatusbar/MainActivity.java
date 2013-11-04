package org.bweb.voltageinstatusbar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
    private NotificationManager notifi;
    private BatteryBroadcastReceiver brbr;
    private IntentFilter filter;
    int scale = -1;
    int level = -1;
    int voltage = -1;
    float temp = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI elements
        final Button button1 = (Button) findViewById(R.id.button);
        final TextView textLevel = (TextView) findViewById(R.id.textViewLevel);
        final TextView textTemp = (TextView) findViewById(R.id.textViewTemp);
        final TextView textVolt = (TextView) findViewById(R.id.textViewVoltage);
        final TextView textTime = (TextView) findViewById(R.id.textViewTime);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // listen for battery broadcasts
        // http://stackoverflow.com/questions/9413490/notification-bar-information-about-battery
        brbr = new BatteryBroadcastReceiver(new BatteryBroadcastReceiver.BatteryBroadcastCallback() {
            @Override
            public void onBatteryBroadcastReceived(Intent intent) {
                Date date = new Date();
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                temp = Math.round(temp/10);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                Log.i("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);
                textLevel.setText(""+level+" %");
                textTemp.setText(""+temp+" °C");
                textVolt.setText(""+voltage+" mV");
                textTime.setText(dateFormat.format(date));
                notif(voltage);
            }
        });
        filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(brbr, filter);

        // stop notification & exit
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifi.cancelAll();
                        finish();
                    }
                }
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(brbr);
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(brbr, filter);
    }

    // generate and display notification
    public void notif(int voltage) {
        // http://stackoverflow.com/questions/15853162/creating-dynamic-r-drawable-source-and-imageview
        String packageName = getPackageName();
        notifi = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // get Intent reference, so tap on notification brings us back to app
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent penInt = PendingIntent.getActivity(getApplicationContext(), 0 , i , 0);

        // generate id from voltage and find image file for notification icon
        int voltage_for_icon = (int)Math.round(voltage / 100);
        int resid = getResources().getIdentifier("v" + voltage_for_icon, "drawable", packageName);

        // define notification
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Battery voltage")
                .setContentText("Currently " + voltage + " mV")
                .setSmallIcon(resid)
                .setOngoing(true)
                .setContentIntent(penInt)
                .build();
        notifi.notify(1337, notification);
    }


}
