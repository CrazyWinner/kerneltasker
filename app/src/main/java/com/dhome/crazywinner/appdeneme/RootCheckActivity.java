package com.dhome.crazywinner.appdeneme;


import android.*;
import android.Manifest;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


public class RootCheckActivity extends IntroActivity {
    @Override
    public void onBackPressed(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RootCheckActivity ben=this;
        addSlide(new SimpleSlide.Builder().title("Welcome")
                .background(R.color.deepteal)
                .description("Welcome to Kernel Tasker, easiest way to change your kernel parameters.")
                .image(R.mipmap.ic_launcher)
                .build());
        Log.i("apilevel",""+Build.VERSION.SDK_INT);
        if(Build.VERSION.SDK_INT>=23){
        addSlide(new SimpleSlide.Builder().title("Ordinary permissions")
                .background(R.color.pink)
                .description("I need this permissions to work.")
                .permissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE})
                .image(R.drawable.permission)
                .build());}
        addSlide(new FragmentSlide.Builder().fragment(rootCheckFragment.newInstance())
                .background(R.color.blue)
                .build());
        addSlide(new SimpleSlide.Builder().background(R.color.orange).image(R.drawable.okicon).title("Ready to go!").build());




    }
   public static class rootCheckFragment extends SlideFragment {
       Button gir;
       Boolean devam=false;
       public rootCheckFragment(){}
       static rootCheckFragment newInstance(){
           return new rootCheckFragment();
       }
      @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstance){
         View baba=inflater.inflate(R.layout.rootfragmentlayout,container,false);
          gir=(Button)baba.findViewById(R.id.button);
          gir.setOnClickListener(new View.OnClickListener() {

              @Override
              public void onClick(View view) {
                  gir.setEnabled(false);
                  new AsyncTask<Void, Boolean, Void>() {
                      @Override
                      protected void onProgressUpdate(Boolean... values) {
                          if (values[0] ) {
                              SharedPreferences kayitlar=getActivity().getSharedPreferences("com.dhome.crazywinner.appdeneme",MODE_PRIVATE);
                              kayitlar.edit().putBoolean("mysettings.firstroot",true).apply();
                              devam=true;




                          }else{
                              Toast.makeText(getActivity(), getResources().getString(R.string.notworkroot), Toast.LENGTH_SHORT).show();
                              gir.setEnabled(true);

                          }

                      }
                      @Override
                      protected Void doInBackground(Void... params) {
                          boolean hasRoot=false;
                          if(RootUtils.rooted()){

                              hasRoot=RootUtils.rootAccess();
                          }
                          publishProgress(hasRoot);
                          return null;
                      }
                  }.execute();
              }
          });
          return baba;
      }

      @Override
       public boolean canGoForward(){
          return devam;
      }

    }


}
