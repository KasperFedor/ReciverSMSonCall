package com.example.reciversmsoncall;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    static UserClass userClass = new UserClass(); //Класс для синхронизации перехвачика СМС
    static String numbGet = "*"; // Переменная номера приёма
    static String tex = "*"; // Переменная значения СМС при приёме

    int i = 0;//Просто ради прикола

    //Создание глобальных переменных для компонентов
    Button button;
    Button test;
    EditText phoneGet;
    EditText phoneSet;
    EditText txt;

    Handler h = new Handler();
    Runnable run = new Runnable() {
        //Новый поток для проверки разрешения звонка
        @Override
        public void run() {
            //проверка разрешения звонка
            Toast.makeText(MainActivity.this,
                    "Event" ,Toast.LENGTH_SHORT).show();
            if (userClass.isCall) {
                //Произвести звонок
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UserClass.numbSet)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                userClass.nCall();//Поставить запрет на вызов
            }
            h.postDelayed(this, 1000);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Init() {
        Components();//Иницилизация компонентов
        Click();//СобытИЯ по кнопке

    }

    private void Components() {
        test = findViewById(R.id.stop_foreground_service_button);
        button = findViewById(R.id.btnSendSMS);
        phoneGet = findViewById(R.id.phoneGet);
        phoneSet = findViewById(R.id.phoneSet);
        txt = findViewById(R.id.txt);
       notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Click () {
        button.setOnClickListener(v -> {
            Show("Приминить");//Сообщение о клике
            //Проверка на заполненость строк
            if (nullStrings()) {

                numbGet = phoneGet.getText().toString();//Присвоение переменной номера отправителя СМС
                UserClass.numbSet = phoneSet.getText().toString();//Присвоение переменной номера для звонка
                tex = txt.getText().toString();//Присвоение переменной содержания СМС

                // Присвое́ние — преступление против собственности,
                // самостоятельная форма хищения, заключающееся
                // в изъятии, обособлении вверенных виновному
                // товарно-материальных ценностей и обращении их
                // в свою пользу либо в пользу других лиц путем
                // установления над ними их незаконного владения.

                //Запуск потока проверки
                 run.run();
            }
            else { //Если поля пустые, предупредить
                Show("Напишите что нибудь!");
            }
        });
        test.setOnClickListener(v ->{
            Context context = getApplicationContext();
            Intent intent = new Intent(context, BackGroubd.class); // Build the intent for the service
            context.startForegroundService(intent);
        });
    }

    public void Notify(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setAutoCancel(false)
                        .setSmallIcon(R.mipmap.ic_launcher_background)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Переодресатор с СМС на звонок")
                        .setContentText("запущенно")
                        .setPriority(PRIORITY_HIGH)
                        .setOngoing(true);

        createChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private boolean nullStrings(){
        if (phoneGet.getText().toString().length() > 0 && phoneSet.getText().toString().length() > 0 && txt.getText().toString().length() > 0)
            return true;
        else
            return false;
    }

    public void Show(String s) {
        Toast.makeText(MainActivity.this,
                s ,Toast.LENGTH_SHORT).show();
    }

    //Вызывается из события приёма СМС
    public static String call()
    {
        userClass.call();
        return "ShowMessage";
    }
}

/* Приложение версии 1
 * Событие приёма находиться в файле SmsReceiver.java
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * Автор кода, а если быть правильным создатель:
 * Fedor Kasper FvK +7-952-549-77-09
 */