package com.dhome.crazywinner.appdeneme;



import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class settingactivity extends PreferenceActivity {
    SharedPreferences kayitlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        kayitlar=getSharedPreferences("com.dhome.crazywinner.appdeneme",MODE_PRIVATE);

        if(kayitlar.getBoolean("mysettings.darkmode",false)){
            setTheme(R.style.AppTheme2);}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingactivity);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new prefFragment())
                .commit();


    }


    public static class prefFragment extends PreferenceFragment{

     String getExtension(String file){
         String[] split = file.split("\\.");
         return split[split.length - 1];
     }
        @Override
        public void onCreate(Bundle save){
            super.onCreate(save);
            final sqLite sqHelper = new sqLite(getActivity());
            getPreferenceManager().setSharedPreferencesName("com.dhome.crazywinner.appdeneme");
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
            addPreferencesFromResource(R.xml.prefences);
            Preference sharingpref=findPreference("mysettings.share");
            sharingpref.setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            startActivity(Intent.createChooser(shareIntent, getString(R.string.shareval)));
                            return false;
                        }
                    }

            );
            Preference versionpref=findPreference("mysettings.version");
            versionpref.setTitle(BuildConfig.VERSION_NAME);
            Preference darkmode=findPreference("mysettings.darkmode");

            darkmode.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {

                            Toast.makeText(getActivity(),"Setting changed please save your profiles and restart app",Toast.LENGTH_LONG).show();
                            return true;
                        }
                    }
            );
            View statusBAr = getActivity().findViewById(R.id.statusBarBackground);
            Utils.setStatusBarColor(getActivity(),statusBAr,Color.parseColor("#B71C1C"));
            Preference backup=findPreference("mysettings.backup");
            backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final List<kernelTask> myTasks=sqHelper.getAllTasks();
                    String[] taskNames=new String[myTasks.size()];
                    int i=0;
                    for(kernelTask taskim:myTasks){
                        taskNames[i]=taskim.getAd();
                        i++;
                    }
                    final boolean[] myBooleans=new boolean[taskNames.length];
                    new AlertDialog.Builder(getActivity()).setTitle("Backup").setMultiChoiceItems(taskNames,myBooleans , new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        }
                    }).setPositiveButton("Backup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        for(int c=0;c<myBooleans.length;c++){
                            if(myBooleans[c]){
                                Utils.saveProfile(myTasks.get(c));

                            }
                        }
                            Toast.makeText(getActivity(),"Your profiles saved to Kernel Tasker directory in your internal storage!",Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
                    return false;
                }
            });
            Preference restore=findPreference("mysettings.restore");
            restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    File myDir= new File(Environment.getExternalStorageDirectory()+"/Kernel Tasker");
                    if(myDir.exists()){

                        final List<File> myFiles=new ArrayList<>();
                        File[] fileList=myDir.listFiles();
                        String[] fileNames=new String[fileList.length];
                        int i=0;
                        for(File fille:fileList){
                            if(getExtension(fille.getName()).equals("kts")){
                                myFiles.add(fille);
                                fileNames[i]=fille.getName();
                                i++;
                            }
                        }
                        final boolean[] booleans=new boolean[fileNames.length];
                        new AlertDialog.Builder(getActivity()).setMultiChoiceItems(fileNames, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                            }
                        }).setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int b=0;
                                for(boolean bool:booleans){
                                    if(bool){
                                        Utils.loadProfile(getActivity(),myFiles.get(b).getAbsolutePath());
                                    }
                                    b++;
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

                    }else{Toast.makeText(getActivity(),"No backups found!",Toast.LENGTH_SHORT).show();}
                    return false;
                }
            });
            Preference shownoti=findPreference("mysettings.shownoti");
            shownoti.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    NotificationManager nm=(NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    if(((boolean)o)){

                        Notification noti1= new NotificationCompat.Builder(getActivity()).setContentTitle("Kernel Tasker is working")
                                .setContentText("").setAutoCancel(false)
                                .setSmallIcon(R.mipmap.ic_launcher).build();
                        noti1.flags |=Notification.FLAG_NO_CLEAR;
                        nm.notify(1,noti1);
                    }else{nm.cancel(1);}
                    return true;
                }
            });
        }

    }

}
