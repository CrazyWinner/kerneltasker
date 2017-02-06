package com.dhome.crazywinner.appdeneme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.List;


public class MyReceiver extends BroadcastReceiver {

    SharedPreferences kayitlar;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        kayitlar=context.getSharedPreferences("com.dhome.crazywinner.appdeneme",Context.MODE_PRIVATE);
        sqLite SQL = new sqLite(context);
        List<kernelTask> tasks= SQL.getAllTasks();
        String kernelVersion=Utils.readText("/proc/version");
        if(!kayitlar.getString("mysettings.procversion","").equals(kernelVersion) && !kayitlar.getString("mysettings.procversion","").equals("")){
            for (kernelTask task2 : tasks) {
                task2.setActive(0);
                SQL.upgradeTask(task2.id,task2);
            }

            kayitlar.edit().putString("mysettings.procversion",kernelVersion).apply();
            Notification.Builder noti=new Notification.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Config changes detected!").setContentInfo("All profiles has been disabled.");
            NotificationManager nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT < 16) {
                nm.notify(0, noti.getNotification());
            } else {
                nm.notify(0, noti.build());
            }
        }
        for (kernelTask task : tasks) {
            if(task.getAction().equals("onBootComplete") && task.isActive()){
                SettingApplier.Apply(context,task.id,false);
            }
        }

        if(kayitlar.getBoolean("mysettings.aktifim",false)){
        Intent i=new Intent(context,MyService.class);
        context.startService(i);}
    }

    public MyReceiver() {
    }



}
