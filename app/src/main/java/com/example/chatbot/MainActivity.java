package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static TextView textView, textView2;
    RecieverClass rc;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS} ,100);
        }
    }
    protected void onStart()
    {
        super.onStart();
        rc = new RecieverClass();
        intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(rc,intentFilter);
    }

    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(rc);
    }


}