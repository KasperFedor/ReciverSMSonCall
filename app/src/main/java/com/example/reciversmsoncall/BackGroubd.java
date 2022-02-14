package com.example.reciversmsoncall;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static com.example.reciversmsoncall.MainActivity.userClass;

import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class BackGroubd extends Service {

    private static final String CHANNEL_ID = "1";
    Handler h = new Handler();
    Runnable run = new Runnable() {
        //Новый поток для проверки разрешения звонка
        @Override
        public void run() {
            //проверка разрешения звонка
            Toast.makeText(BackGroubd.this,
                    "Event" ,Toast.LENGTH_SHORT).show();
            if (userClass.isCall) {
                //Произвести звонок
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserClass.numbSet)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                userClass.nCall();//Поставить запрет на вызов
            }
            h.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(
                    "MyChannelId",
                    "My Foreground Service",
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this, "MyChannelId");
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher_background)
                    .setContentTitle("App is running on foreground")
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setChannelId("MyChannelId")
                    .build();

            startForeground(1, notification);
        }
        super.onCreate();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        run.run();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel chan = new NotificationChannel(
                    "MyChannelId",
                    "My Foreground Service",
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    this, "MyChannelId");
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher_background)
                    .setContentTitle("App is running on foreground")
                    .setPriority(NotificationManager.IMPORTANCE_LOW)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setChannelId("MyChannelId")
                    .build();

            startForeground(1, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
