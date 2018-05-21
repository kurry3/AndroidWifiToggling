package com.example.rflab_dell_8.wificontroller;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.EditText;
import android.os.Handler;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import android.os.SystemClock;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    WifiManager wifimanager;
    TextView tvTimer;
    TextView wifiStatusText;
    EditText timeInterval;
    long startTime, timeInMilliseconds = 0;
    Handler timerHandler = new Handler();
    Handler wifiHandler = new Handler();
    ToggleButton toggleButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiStatusText = (TextView) findViewById(R.id.text_view);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        
        //find the initial wifi state 
        boolean wifiStatus = wifimanager.isWifiEnabled();
        if (wifiStatus) {
            wifiStatusText.setText("WiFi is ON");
        } else {
            wifiStatusText.setText("WiFi is OFF");
        }
        
        //execute class wifiRunnable, passing in the initial wifi status
        final wifiRunnable obj = new wifiRunnable(wifiStatus);
        // Put listener on toggle button
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    startTime = SystemClock.uptimeMillis();
                    timerHandler.postDelayed(updateTimerThread, 0);
                    wifiHandler.postDelayed(obj,0);
                } else {
                    timerHandler.removeCallbacks(updateTimerThread);
                    wifiHandler.removeCallbacks(obj);
                }
            }
        });
    }
    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            tvTimer.setText(getDateFromMillis(timeInMilliseconds));
            timerHandler.postDelayed(this, 0);
        }
    };

    public class wifiRunnable implements Runnable{
        private boolean nextState;
        public wifiRunnable(boolean status){
            this.nextState = status;
        }
        @Override
        public void run() {
            wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (nextState) {
                wifiStatusText.setText("WiFi is ON");
                wifimanager.setWifiEnabled(true);
                nextState = false;
            } else {
                wifiStatusText.setText("WiFi is OFF");
                wifimanager.setWifiEnabled(false);
                nextState = true;
            }
            //change delayMillis to change the delay of the toggling
            wifiHandler.postDelayed(this, 15000);
        }
    }
}
