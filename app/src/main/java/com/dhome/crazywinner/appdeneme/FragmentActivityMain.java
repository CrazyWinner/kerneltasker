package com.dhome.crazywinner.appdeneme;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentActivityMain extends Fragment {
    private char newLine='\n';

    HashMap<String,SeekBar> timeinseek;
    HashMap<String,TextView> timeintw;
    HashMap<Integer,SeekBar> seeks;
    HashMap<Integer,TextView> tws;
    HashMap<String,Long> timeinlong;
    public SharedPreferences kayitlar;

    private int corecount;
    private View fragmentView;

    Timer timer;

    public FragmentActivityMain() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void refreshTimes(){
         if(timeinlong!=null){
             timeinlong.clear();
             timeinlong=null;
         }
        if(timeinseek!=null){
            timeinseek.clear();
            timeinseek=null;
        }
        if(timeintw!=null){
            timeintw.clear();
            timeintw=null;
        }
        timeinseek=new HashMap<>();
        timeintw=new HashMap<>();
        timeinlong=new HashMap<>();

        String timein = Utils.readText(Paths.CPU_TIME_STATE);
        if (timein == null) {
            timein = "";
        } else {
            timein = timein + newLine;
        }

        LinearLayout lbasd = (LinearLayout) fragmentView.findViewById(R.id.lineartimeinstate2);
        lbasd.removeAllViews();
        long alltime = 0;
        long alltime2 = 0;
        Boolean dana = false;
        while (timein.contains("" + newLine) && timein.contains(" ")) {
            String freqq = timein.substring(0, timein.indexOf(" "));
            String sure = timein.substring(timein.indexOf(" ") + 1, timein.indexOf(newLine));
            timein = timein.substring(timein.indexOf(newLine) + 1, timein.length());
            TextView tww1 = new TextView(getActivity());
            TextView tww2 = new TextView(getActivity());
            Long sure2 = Long.parseLong(sure);
            int saat = 0;
            int dakika = 0;
            int saniye = 0;
            saniye = (int) (sure2 / 100);
            dakika = saniye / 60;
            saniye = saniye % 60;

            saat = dakika / 60;
            dakika = dakika % 60;
            SeekBar seekbeni = new SeekBar(getActivity());
            LinearLayout yana = new LinearLayout(getActivity());
            seekbeni.setThumb(getResources().getDrawable(R.drawable.butback));
            yana.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout dike = new LinearLayout(getActivity());
            dike.setOrientation(LinearLayout.VERTICAL);
            View viisa = new View(getActivity());
            viisa.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    0, 1));
            tww1.setText(freqq);
            String dakikast = "" + dakika;
            if (dakikast.length() == 1) {
                dakikast = "0" + dakikast;
            }
            String saniyest = "" + saniye;
            if (saniyest.length() == 1) {
                saniyest = "0" + saniyest;
            }
            tww2.setText(" " + saat + ":" + dakikast + ":" + saniyest);
            yana.addView(tww1);
            yana.addView(viisa);
            yana.addView(tww2);
            dike.addView(yana);
            seekbeni.setMax(100);
            seekbeni.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            dike.addView(seekbeni);
            lbasd.addView(dike);
            alltime = alltime + sure2;
            timeinlong.put(freqq, sure2);
            timeinseek.put(freqq, seekbeni);
            timeintw.put(freqq, tww2);
            if (!dana && !timein.contains("" + newLine)) {
                alltime2 = ((SystemClock.elapsedRealtime()
                )) / 10;
                timein = "DeepSleep " + (alltime2 - alltime) + newLine;

                dana = true;
            }
        }
        {
            int saat = 0;
            int dakika = 0;
            int saniye = 0;
            saniye = (int) (alltime2 / 100);
            dakika = saniye / 60;
            saniye = saniye % 60;

            saat = dakika / 60;
            dakika = dakika % 60;
            TextView sdah = (TextView) fragmentView.findViewById(R.id.textviewtotaltime);
            sdah.setText(saat + ":" + dakika + ":" + saniye);
        }
        for (String s : timeinseek.keySet()) {
            timeinseek.get(s).setProgress((int) ((timeinlong.get(s) * 100 / alltime2)));

        }
    }
    private void startFragment(){




        kayitlar=getActivity().getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);
        if(!kayitlar.contains("mysettings.showtoast")){
            kayitlar.edit().putBoolean("mysettings.showtoast",true).apply();
        }
        if(!kayitlar.contains("mysettings.aktifim")){
            kayitlar.edit().putBoolean("mysettings.aktifim",true).apply();
        }



        TextView tbbn=(TextView)fragmentView.findViewById(R.id.textviewservicestatus);






        String kernelVersion=Utils.readText("/proc/version");
        if(!kayitlar.getString("mysettings.procversion","").equals(kernelVersion) && !kayitlar.getString("mysettings.procversion","").equals("")){
            for(int se21=0;se21<kayitlar.getInt("num",0);se21++){
                kayitlar.edit().putBoolean("activee"+se21,false).apply();
            }
            kayitlar.edit().putString("mysettings.procversion",kernelVersion).apply();

            Notification.Builder noti=new Notification.Builder(getActivity()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Config changes detected!").setContentInfo("All profiles has been disabled.");
            NotificationManager nm=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT < 16) {
                nm.notify(0, noti.getNotification());
            } else {
                nm.notify(0, noti.build());
            }
        }
        if(!Utils.isMyServiceRunning(getActivity(),MyService.class)){
            if(kayitlar.getBoolean("mysettings.aktifim",false)){
                Intent aben=new Intent(getActivity(),MyService.class);
                getActivity().startService(aben);
                tbbn.setText(getResources().getString(R.string.servicerunning));
                tbbn.setTextColor(Color.GREEN);}else{tbbn.setText(getResources().getString(R.string.servicenotrunning));
                tbbn.setTextColor(Color.RED);}
        }else{ tbbn.setText(getResources().getString(R.string.servicerunning));
            tbbn.setTextColor(Color.GREEN);}




        seeks=new HashMap<>();
        tws=new HashMap<>();
      corecount=Runtime.getRuntime().availableProcessors();




        for(int z=0;z<corecount;z++){
            LinearLayout babos = (LinearLayout) fragmentView.findViewById(R.id.lineardevicestatus);
            LinearLayout lay = new LinearLayout(getActivity());
            lay.setOrientation(LinearLayout.VERTICAL);
            TextView tw = new TextView(getActivity());
            final SeekBar sb =new SeekBar(getActivity());
            tws.put(z, tw);
            tw.setText("");
            seeks.put(z, sb);

            String max=Utils.readText(Paths.CPU_MAX_FREQ.replace("%d","0"));

            if(max==null){max= "0";
            }
            sb.setMax(Utils.parseInt(max)/1000);


            sb.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    return true;
                }
            });
            lay.addView(tw);
            lay.addView(sb);
            sb.setThumb(getResources().getDrawable(R.drawable.butback));
            babos.addView(lay);

        }


       callAsynchronousTask();

       refreshTimes();





        final Button refreshbutton = (Button) fragmentView.findViewById(R.id.refreshbutton);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           refreshTimes();

            }
        });




        if(kayitlar.getBoolean("mysettings.rateus",true)){
            new AlertDialog.Builder(getActivity()).setTitle("Rate us").setMessage("If you like our project . \n Please rate us on Google Play")
                    .setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url="https://play.google.com/store/apps/details?id=com.dhome.crazywinner.appdeneme";
                            Uri uri = Uri.parse(url);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            kayitlar.edit().putBoolean("mysettings.rateus",false).apply();
                        }
                    }).setNegativeButton("Nope",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    kayitlar.edit().putBoolean("mysettings.rateus",false).apply();
                }
            }).setNeutralButton("Later",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

        }
        if(Utils.recoverProfiles(getActivity())){
            Toast.makeText(getActivity(), "Your profiles has been recovered from old config!", Toast.LENGTH_LONG).show();
        }
        ((FragmentHolderActivity) getActivity()).setStarted();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.fragment_fragment_activity_main, container, false);

        startFragment();
        return fragmentView;
    }





         private void callAsynchronousTask() {
             final Handler handler = new Handler();
             timer = new Timer();
             TimerTask doAsynchronousTask = new TimerTask() {
                 @Override
                 public void run() {
                     handler.post(new Runnable() {
                         public void run() {
                             try {
                                 new AsyncTask<Void, Integer, Void>() {
                                     @Override
                                     protected void onProgressUpdate(Integer... values) {
                                         int s=values[0];
                                         int sst=values[1];
                                         SeekBar selam=seeks.get(sst);
                                         TextView tw=tws.get(sst);

                                         if(s!=0){
                                             selam.setProgress(s);
                                             tw.setText("Cpu"+sst+":"+s+" Mhz");
                                         }else{selam.setProgress(0);
                                             tw.setText("Cpu"+sst+":"+"offline");}

                                     }
                                     @Override
                                     protected Void doInBackground(Void... params) {


                                             for(int xss=0;xss<corecount;xss++){
                                                 final int xsds=xss;


                                                 String s=Utils.readText(Paths.CPU_CUR_FREQ.replace("%d",""+xsds));
                                                 if(s!=null && !s.equals("")){
                                                     publishProgress(Utils.parseInt(s)/1000,xsds);}else{
                                                     publishProgress(0,xsds);
                                                 }





                                                 try {
                                                     Thread.sleep(2);
                                                 } catch (InterruptedException e) {
                                                     e.printStackTrace();
                                                 }
                                             }


                                         return null;
                                     }
                                 }.execute();
                             } catch (Exception e) {
                                 // TODO Auto-generated catch block
                             }
                         }
                     });
                 }
             };
             timer.schedule(doAsynchronousTask, 0, 1000);
         }

    @Override
    public void onDetach() {
        super.onDetach();
         if(timer!=null)
        timer.cancel();


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
