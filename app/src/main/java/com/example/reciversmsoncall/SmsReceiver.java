package com.example.reciversmsoncall;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class SmsReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

//---получить входящее SMS сообщение---
        Bundle bundle= intent.getExtras();
        SmsMessage[] msgs=null;
        String str="";
        String nmb="";
        intent.removeExtra(bundle.toString());
        if(bundle!=null)
        {
//---извлечь полученное SMS ---
            Object[] pdus=(Object[]) bundle.get("pdus");
            msgs=new SmsMessage[pdus.length];
            for(int i=0; i<msgs.length; i++){
                msgs[i]= SmsMessage.createFromPdu((byte[])pdus[i]);
                nmb=msgs[i].getOriginatingAddress();
                //  str+=" :";
                str+= msgs[i].getMessageBody().toString();
                //   str+="\n";
            }
//---Показать новое SMS сообщение---
            Toast.makeText(context, nmb + ":\n" + str, Toast.LENGTH_SHORT).show();
            if (nmb.equals(MainActivity.numbGet) || MainActivity.numbGet.equals("*"))
            {
                if (str.equals(MainActivity.tex) || MainActivity.tex.equals("*"))
                {
                    MainActivity.call();
                    abortBroadcast();
                }
            }


        }
    }
}
