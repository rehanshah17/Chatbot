package com.example.chatbot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class RecieverClass extends BroadcastReceiver {
    Handler handler;
    SmsManager smsManager;
    String message;
    SmsMessage[] arrMessages;

    @Override
    public void onReceive(Context context, Intent intent) {
        message = "";

        Bundle bundle = intent.getExtras();
        Object[] objects = (Object[])bundle.get("pdus");
        arrMessages = new SmsMessage[objects.length];
        for(int i = 0; i < objects.length;i++)
        {
            arrMessages[i] = SmsMessage.createFromPdu((byte[]) objects[i],bundle.getString("format"));
            message += arrMessages[i].getMessageBody();
        }
        Log.d("TAG","" + arrMessages[arrMessages.length-1].getMessageBody());

        MainActivity.textView.setText("Message recieved: "  + message);
        Log.d("TAG", "set the text");
        new BackgroundThread().execute(message);
        Log.d("TAG", "execute");
    }

    public class BackgroundThread extends AsyncTask<String, Void, JSONObject>
    {
        URLConnection urlConnection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json;
            Log.d("TAG", "doinbackground");
            String txt;
            try
            {
                String zipcode = strings[0];
                URL url = new URL("http://api.brainshop.ai/get?bid=175242&key=D7FyCig6vbMYSuG1&uid=175242&msg=" + message);
                urlConnection = url.openConnection();
                Log.d("TAG", "urlconnection");
                inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Log.d("TAG", "bufferedreader");
                String line = "";
                txt = bufferedReader.readLine();
                while((line = bufferedReader.readLine()) != null)
                {
                    txt += line;
                }
                json = new JSONObject(txt);
             return json;
            }catch(Exception e)
            {
                e.printStackTrace();
                Log.d("TAG",e.toString());
            }
            return null;
        }

        protected void onPostExecute(JSONObject text)
        {
            try {
                super.onPostExecute(text);
                MainActivity.textView2.setText(text.getString("cnt"));
                handler = new Handler();
                handler.postDelayed(typing(text.getString("cnt")),2000);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private Runnable typing(String text)
    {
        return new Runnable() {
            @Override
            public void run() {
                smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(arrMessages[arrMessages.length-1].getOriginatingAddress(),null,text,null,null);
            }
        };
    }

}
