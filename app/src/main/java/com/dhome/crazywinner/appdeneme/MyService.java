package com.dhome.crazywinner.appdeneme;

import android.annotation.SuppressLint;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;
import com.badoo.mobile.util.WeakHandler;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyService extends Service {

    SharedPreferences kayitlar;
    private int applyit = -1;
    private sqLite SQL;
    private int saymasayisi=0;
    private int lastayarcik;
    private kernelTask lastTask;
    private Boolean screenon;

    private List<kernelTask> listem;
    private WeakHandler handler;
    public MyService() {



    }

    private boolean saatarasi(String saatler){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);

     int kucuksaat=Utils.parseInt(saatler.substring(0, saatler.indexOf(":")));
        int kucukdakika=Utils.parseInt(saatler.substring(saatler.indexOf(":")+1,saatler.indexOf("to") ));
        saatler=saatler.substring(saatler.indexOf("to")+2,saatler.length());
        int buyuksaat=Utils.parseInt(saatler.substring(0, saatler.indexOf(":")));
        int buyukdakika=Utils.parseInt(saatler.substring(saatler.indexOf(":")+1,saatler.length() ));
        return Utils.saatbuyukmu(buyuksaat, buyukdakika, hour, minute) || Utils.saatbuyukmu(hour, minute, kucuksaat, kucukdakika);

    }

    private int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);




            return (int)(((float)level / (float)scale) * 100.0f);


    }
    @SuppressLint("NewApi")
    private boolean isScreenOn(){

     PowerManager pm=(PowerManager)getSystemService(POWER_SERVICE);
       int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion>=20){
            if(pm.isInteractive()){
                return true;
            }
        }else{

            if(pm.isScreenOn()){
                return true;}}
        return false;
    }

    private boolean isCharging(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int status=batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING;
    }
    public void onCreate(){


        kayitlar=getSharedPreferences("com.dhome.crazywinner.appdeneme",MODE_PRIVATE);
        SQL=new sqLite(this);
           listem=Utils.arrangeList(SQL.getAllTasks());
         kayitlar.edit().putBoolean("refreshProfiles",false).commit();
        if(Utils.recoverProfiles(this)){
            Toast.makeText(this, "Your profiles has been recovered from old config!", Toast.LENGTH_LONG).show();
        }

       final Runnable runnable = new runnablebaba() {

            @SuppressLint("NewApi")
            @Override
            public void run() {
      /* do what you need to do */
               if(!kayitlar.getBoolean("mysettings.forceset",false)){
                 applyit=-1;

                   if(lastayarcik!=kayitlar.getInt("lastayar", -1)){
                       lastayarcik=kayitlar.getInt("lastayar", -1);
                       lastTask = SQL.getTask(lastayarcik);
                   }


                screenon = isScreenOn();

                 if(kayitlar.getBoolean("refreshProfiles",false)){
                     listem=Utils.arrangeList(SQL.getAllTasks());
                     kayitlar.edit().putBoolean("refreshProfiles",false).apply();
                 }
                for (kernelTask task : listem) {

                    if (task.isActive()) {
                        String action = task.getAction();
                        int actaction = Utils.getAction(action);

                        if (actaction == Utils.ONSCREENON  ) {

                            if (screenon) {
                                if(task.id!=lastayarcik){
                                applyit = task.id;}
                                break;

                            }
                        }
                        if (actaction == Utils.ONSCREENOFF ) {
                            if (!screenon) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}
                                break;

                            }
                        }
                        if (actaction == Utils.ONCHARGE ) {
                            if (isCharging()) {
                                if(task.id!=lastayarcik){
                                    applyit = task.id;}
                                break;
                            }
                        }
                        if (actaction == Utils.ONLAUNCHING ) {
                            if (isAppRunning(action.substring(9, action.length()))) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}


                                break;
                            }
                        }
                        if (actaction == Utils.ONBATTERYLOW ) {
                            if (getBatteryLevel() <= Utils.parseInt(action.substring(13, action.length()))) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}

                                break;

                            }

                        }
                        if (actaction == Utils.TIMESCHEDULER ) {

                            Calendar c = Calendar.getInstance();
                            int hour = c.get(Calendar.HOUR_OF_DAY);
                            int minute = c.get(Calendar.MINUTE);


                            if (hour == Utils.parseInt(action.substring(14, action.indexOf(":"))) && minute == Utils.parseInt(action.substring(action.indexOf(":") + 1, action.length()))) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}
                                break;


                            }
                        }
                        if (actaction == Utils.TIMESCHEDULERFT ) {
                            if (saatarasi(action.substring(16, action.length()))) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}
                                break;

                            }
                        }
                        if (actaction == Utils.ALWAYSON ) {

                                if(task.id!=lastayarcik){
                                    applyit = task.id;}
                                break;


                        }

                    }
                }

                if (applyit != -1) {
                    if (lastayarcik != applyit) {
                        SettingApplier.Apply(MyService.this, applyit, false);
                    } else {
                        if (lastTask.isSolid()) {
                            Utils.checkSolidTask(MyService.this, lastTask);
                        }

                    }
                }
            }
                handler.removeCallbacks(this);
                 saymasayisi++;
                if(saymasayisi>60){saymasayisi=0;
                System.gc();}
                handler.postDelayed(this, 1000);
            }
        };
        handler=new handlerbaba();
        handler.postDelayed(runnable, 1000);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
     @Override
     public void onDestroy(){
         super.onDestroy();
         handler.removeMessages(0);
     }
    private static class handlerbaba extends WeakHandler{

    }
    private static class runnablebaba implements Runnable {
     
        @Override
        public void run() {
        }
    }
    private Boolean isAppRunning(String app){
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {


                @SuppressLint("InlinedApi") UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                // We get usage stats for the last 10 seconds
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
                // Sort the stats by the last time used
                if(stats != null) {
                    SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
                    for (UsageStats usageStats : stats) {
                        mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                    }
                    if( !mySortedMap.isEmpty()) {
                        currentApp =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                    }
                }


        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        return currentApp.equals(app);
    }


}
