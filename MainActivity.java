package com.example.rflab_dell_8.wificontroller;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.os.Handler;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import android.os.SystemClock;
import android.net.wifi.WifiManager;
import android.content.Context;

public class MainActivity extends AppCompatActivity {
    WifiManager wifimanager;
    TextView tvTimer;
    TextView wifiStatusText;
    EditText timeInterval;
    long startTime, timeInMilliseconds = 0;
    Handler timerHandler = new Handler();
    Handler wifiHandler = new Handler();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiStatusText = (TextView) findViewById(R.id.text_view);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
    }

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public void start(View v) {
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimerThread, 0);
        wifiHandler.postDelayed(wifiRunnable, 0);
    }

    public void stop(View v) {
        timerHandler.removeCallbacks(updateTimerThread);
        //wifiHandler.removeCallbacks(wifiRunnable);
    }

   /* public void wifiToggling(){
            if (wifimanager.isWifiEnabled()) {
                wifiStatusText.setText("WiFi is ON");
                wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifimanager.setWifiEnabled(true);
            }
            else {
                wifiStatusText.setText("WiFi is OFF");
                wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifimanager.setWifiEnabled(false);
            }
    }*/

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            tvTimer.setText(getDateFromMillis(timeInMilliseconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    private Runnable wifiRunnable = new Runnable() {
        public void run() {
            if (wifimanager.isWifiEnabled()) {
                wifiStatusText.setText("WiFi is ON");
                //wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                //wifimanager.setWifiEnabled(true);
            } else {
                wifiStatusText.setText("WiFi is OFF");
                //wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                //wifimanager.setWifiEnabled(false);
            }
            wifiHandler.postDelayed(this, 1000 * Integer.valueOf(timeInterval.getText().toString()));
        }
    };
}
