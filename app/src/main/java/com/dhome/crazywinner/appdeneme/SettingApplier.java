package com.dhome.crazywinner.appdeneme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mertcan on 11.04.2015.
 */
public class SettingApplier {
    private static SharedPreferences kayitlar;
    private static sqLite SQL;


    private static List<String> commands;


    public static void Apply( Context context,int id ,boolean delete)
    {
        SQL=new sqLite(context);
        commands=new ArrayList<>();



        kayitlar=context.getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = kayitlar.edit();
        kernelTask task=SQL.getTask(id);
        if(kayitlar.getBoolean("mysettings.shownoti",false)){
            String goster=task.getAd();
            if(goster.equals("")){goster="None";}
           Notification noti1= new NotificationCompat.Builder(context).setContentTitle("Kernel Tasker is working").setContentText("Current profile is "+ goster).setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher).build();
            noti1.flags |=Notification.FLAG_NO_CLEAR;
            NotificationManager nm= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1,noti1);
        }
        ayarKabul(task);
        runCommands();
        editor.putInt("lastayar",id).apply();
        commands.clear();
           commands=null;
        if(delete){
            TaskDelete(id);
            editor.putInt("lastayar",0).apply();
            if(kayitlar.getBoolean("mysettings.showtoast",false)){
                Toast.makeText(context, context.getResources().getString(R.string.settingsaved), Toast.LENGTH_SHORT).show();}
        }else{if(kayitlar.getBoolean("mysettings.showtoast",false)){
            Toast.makeText(context,context.getResources().getString(R.string.profileapply)+task.getAd(),Toast.LENGTH_SHORT).show();
     }}


        kayitlar=null;
        SQL=null;

    }



    private static void TaskDelete(int position){
    SQL.deleteTask(position);
    }
    private static void runCommands(){
        for (String command : commands) {
            Utils.runCommand(command);
        }

    }
    private static void RunRootCommand(String fiile,String valuee)
    {

        commands.add("chmod 644 " + fiile);
        commands.add("echo "+valuee+" > "+fiile);
        commands.add("chmod 444 "+fiile);








    }

    private static void ayarKabul(kernelTask task){


        JSONArray jsonArrayyollar;
        JSONArray jsonArraydegerler;

            jsonArrayyollar = task.getYollar();
            jsonArraydegerler = task.getDegerler();
            // jsonArray contains the data, use jsonArray.getString(index) to
            // retreive the elements

        for(int i=0;i<jsonArrayyollar.length();i++){


            String deger= null;
            String yol=null;
            try {
                yol=jsonArrayyollar.getString(i);
                deger = jsonArraydegerler.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(yol!=null && deger!=null)    {
                RunRootCommand(yol,deger);}


        }



    }
}
