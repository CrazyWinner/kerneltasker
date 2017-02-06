package com.dhome.crazywinner.appdeneme;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import java.util.List;


public class FragmentHolderActivity extends AppCompatActivity {
    private InterstitialAd gecisreklam;
    private ActionBarDrawerToggle mDrawerToggle;
    private AlertDialog eminmisin;
    private boolean starting=false;
    private sqLite SQL;
    private Fragment siradaki;
    private ProgressBar siradakiprogress;
    private DrawerView drawer;
    void setmDrawerProfile(int numa,boolean delete){
        if(delete){
            drawer.removeProfileById(numa);
        }else{
            kernelTask kerneltask=SQL.getTask(numa);
            DrawerProfile profile=new DrawerProfile().setId(kerneltask.id).setBackground(getResources().getDrawable(R.drawable.back_poly))
                    .setName(kerneltask.getAd()).setDescription(kerneltask.getAction());
            if(kerneltask.getImagePath()!=null &&!kerneltask.getImagePath().equals("")){

                profile.setRoundedAvatar(Utils.getBitmapFromPath(this,kerneltask.getImagePath(),78));
            }else{profile.setRoundedAvatar((BitmapDrawable)getResources().getDrawable(R.drawable.icon4));}
            drawer.addProfile(profile);
        }
    }
    void upgradeDrawerProfile(int numa){
            drawer.removeProfileById(numa);
            kernelTask kerneltask=SQL.getTask(numa);
            DrawerProfile profile=new DrawerProfile().setId(kerneltask.id).setBackground(getResources().getDrawable(R.drawable.back_poly))
                    .setName(kerneltask.getAd()).setDescription(kerneltask.getAction());
            if(kerneltask.getImagePath()!=null &&!kerneltask.getImagePath().equals("")){

                profile.setRoundedAvatar(Utils.getBitmapFromPath(this, kerneltask.getImagePath(), 78));
            }else{profile.setRoundedAvatar((BitmapDrawable) getResources().getDrawable(R.drawable.icon4));}
            drawer.addProfile(profile);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if(mDrawerToggle.onOptionsItemSelected(item))return true;
        return false;
    }
    void setSiradaki(Fragment frag){
        siradakiprogress.setVisibility(View.VISIBLE);
        siradaki=frag;
        FragmentManager fragmentManager =getSupportFragmentManager();
        List<Fragment> fragmentList=fragmentManager.getFragments();
        if(!starting){
        if(fragmentList.size()!=0 ){
        FragmentTransaction trans = fragmentManager.beginTransaction();

        for (Fragment fragment :fragmentList) {
            trans.remove(fragment);
        }
        trans.commit();}}
        starting=true;

    }
    @Override
    public void onBackPressed()
    {
        if(!eminmisin.isShowing()){
            eminmisin.show();
        }

    }
    void setStarted(){
        starting=false;
    }
    private void reklamHazirla(){
        gecisreklam=new InterstitialAd(this);
        gecisreklam.setAdUnitId("ca-app-pub-7735317833015304/8635748873");
        gecisreklam.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                gecisreklam.show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                try {
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("CF739F998F27FFBEE8A3A812A66D758A")
                .build();


        gecisreklam.loadAd(adRequest);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences kayitlar=getSharedPreferences("com.dhome.crazywinner.appdeneme",MODE_PRIVATE);
        if(!kayitlar.getBoolean("mysettings.firstroot",false)){
            Intent baslat=new Intent(this,RootCheckActivity.class);
            startActivity(baslat);

        }
        if(kayitlar.getBoolean("mysettings.darkmode",false)){
            setTheme(R.style.AppTheme2);}

        setContentView(R.layout.activity_fragment_holder);

        eminmisin= new AlertDialog.Builder(this).setTitle("Sure?").setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        drawer=(DrawerView)findViewById(R.id.drawerView);
        SQL=new sqLite(this);
        siradakiprogress=(ProgressBar)findViewById(R.id.progressBar2);
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.DrawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                null,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu();
                siradakiprogress.setVisibility(View.INVISIBLE);
                if(siradaki!=null){
                    Utils.setFragment(FragmentHolderActivity.this,siradaki);
                    siradaki=null;
                }

            }

            public void onDrawerOpened(View drawerView) {
               invalidateOptionsMenu();
            }

        };
        mDrawerToggle=drawerToggle;
        Utils.drawerHazirla(this,0,drawerToggle);
        Fragment mainFragment = new FragmentActivityMain();
// Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mainFragment)
                .commit();

        Toolbar mytool=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(mytool);

        ActionBar ab=getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
     
        if(System.currentTimeMillis()-kayitlar.getLong("mysettings.time",0l)>15*60000 ||
                System.currentTimeMillis()-kayitlar.getLong("mysettings.time",0l)<-15*60000 ){
            reklamHazirla();
            kayitlar.edit().putLong("mysettings.time", System.currentTimeMillis()).apply();

        }
        if(!kayitlar.getBoolean("firstTimePermission",false)){
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                if(!Utils.checkPermission(this)){

                    new AlertDialog.Builder(this).setMessage("I need permission for onAppLaunch actions if you dont use them you dont need to give.").setPositiveButton("Okay,show me", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            @SuppressLint("InlinedApi") Intent iins=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(iins);
                        }
                    }).setNegativeButton("Maybe later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }}
            kayitlar.edit().putBoolean("firstTimePermission",true).apply();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
