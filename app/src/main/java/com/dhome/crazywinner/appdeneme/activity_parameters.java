package com.dhome.crazywinner.appdeneme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class activity_parameters extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences kayitlar=getSharedPreferences("com.dhome.crazywinner.appdeneme",MODE_PRIVATE);
        if(kayitlar.getBoolean("mysettings.darkmode",false)){
            setTheme(R.style.AppTheme2);}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_parameters);
        Intent saves =getIntent();
        String action=saves.getStringExtra("action");
        String name=saves.getStringExtra("name");
        boolean editmi=saves.getBooleanExtra("editmi", false);
        int sayisi=saves.getIntExtra("num", -1);
        Toolbar mytool=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(mytool);

        View statusView=findViewById(R.id.statusBarBackground);
          Utils.setStatusBarColor(this,statusView, Color.parseColor("#B71C1C"));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, FragmentTweaker.newInstance(sayisi,editmi,name,action))
                .commit();

    }

  public void geridon(int id,boolean cancelled){
      Intent returnIntent=new Intent();

      returnIntent.putExtra("newId",id);
      setResult(cancelled?RESULT_OK:RESULT_CANCELED,returnIntent);
      finish();
  }
    public void onBackPressed(){
        Intent returnIntent=new Intent();
        returnIntent.putExtra("newId",-1);

        setResult(RESULT_CANCELED,returnIntent);
        super.onBackPressed();
    }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode,resultCode,data);
  }


}
