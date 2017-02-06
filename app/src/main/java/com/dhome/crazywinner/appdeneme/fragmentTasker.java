package com.dhome.crazywinner.appdeneme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dhome.crazywinner.appdeneme.recyclerview.RecylerAdapter;
import com.melnykov.fab.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class fragmentTasker extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SharedPreferences kayitlar;
    private int suanki=-1;

    private int selectmode=-1;
    private RecylerAdapter myAdapter;
    private RecyclerView recyler;
    private AlertDialog addiyalog;
    private int lastint=-1;
    private EditText adinne;
    private int suanki2=-1;
    private View fragmentView;
    private boolean ceku;
    private FloatingActionButton fab;
    private sqLite SQL;
    private String mmode="";
    // TODO: Rename and change types of parameters
    public void setNumber(int number,int number2){
        suanki=number;
        suanki2=number2;
    }

     @Override
     public void onStart(){
         super.onStart();
         if(myAdapter!=null){
             myAdapter.notifyDataSetChanged();

         }
     }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event

        ceku=!ceku;
        kayitlar.edit().putBoolean("mysettings.aktifim",ceku).apply();
        recyler.setEnabled(ceku);

        if(ceku){
            if(!Utils.isMyServiceRunning( getActivity(),MyService.class)){
                Intent in=new Intent( getActivity(),MyService.class);
                getActivity().startService(in);
            }}else{
            if(Utils.isMyServiceRunning( getActivity(),MyService.class)){
                Intent in=new Intent( getActivity(),MyService.class);
                getActivity().stopService(in);
            }
        }
        item.setTitle(ceku ? "Active" : "Inactive");
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        inflater.inflate(R.menu.menu_activity__tasker, menu);
        menu.findItem(R.id.action_settings).setTitle(ceku ? "Active" : "Inactive");


    }

    private void startFragment(){

        kayitlar = getActivity().getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);






        ceku=kayitlar.getBoolean("mysettings.aktifim",false);
        SQL=new sqLite(getActivity());
        adinne=new EditText(getActivity());


        addiyalog=new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.taskname)).setView(adinne).setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!adinne.getText().toString().equals("")){
                    Intent i = new Intent(getActivity(),activity_parameters.class);

                    i.putExtra("action",mmode);
                    i.putExtra("name",adinne.getText().toString());
                    adinne.setText("");
                    addiyalog.dismiss();

                    startActivityForResult(i, 1);}else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.cannotempty),Toast.LENGTH_SHORT).show();
                    adinne.setText("");
                    addiyalog.dismiss();
                }
            }
        }).create();





        recyler=(RecyclerView)fragmentView.findViewById(R.id.recyclerview);


        fab =(FloatingActionButton)fragmentView.findViewById(R.id.fabMenu);

        fab.attachToRecyclerView(recyler);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openContextMenu(v);

                selectmode=1;

            }
        });
        recyler.setEnabled(ceku);
        recyler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyler.setLayoutManager(llm);

        List<Integer> degerler3=new ArrayList<>();
        for (kernelTask task2 : SQL.getAllTasks()) {
            degerler3.add(task2.id);


        }

        myAdapter=new RecylerAdapter(this,degerler3,recyler);
        recyler.setAdapter(myAdapter);
        recyler.setItemAnimator(new DefaultItemAnimator());





        ((FragmentHolderActivity) getActivity()).setStarted();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tasker");


    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1){
            if(resultCode==Activity.RESULT_OK){
                myAdapter.satirEkle(data.getIntExtra("newId",-1));
                ((FragmentHolderActivity)getActivity()).setmDrawerProfile(data.getIntExtra("newId", -1), false);
            }else{
                if(data.getIntExtra("newId",-1)!=-1){
                ((FragmentHolderActivity)getActivity()).upgradeDrawerProfile(data.getIntExtra("newId", -1));}}
        }
    }

    public void openContextMenu(View v) {


        if(v.getId()==R.id.recyclerview ){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(myAdapter.getItemName(suanki))
                    .setItems(R.array.menuarray, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                         selectMenuItem(which);
                        }
                    });
             builder.show();
            selectmode=0;


        }else{


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Apply profile when...")
                    .setItems(R.array.optionarray, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                          selectMenuItem(which);
                        }
                    });
            builder.show();

        }

    }
    private void TaskDelete(int position){

        SQL.deleteTask(position);
        ((FragmentHolderActivity)getActivity()).setmDrawerProfile(position, true);

    }

    private boolean selectMenuItem(int menuItemIndex) {


        mmode="";
        if(selectmode==1){
            setAction(menuItemIndex,false,-1);


        }
        if(selectmode==2){
            setAction(menuItemIndex,true,lastint);
        }
        if(selectmode==0) {
            final int globalint=suanki;
            final kernelTask task=SQL.getTask(globalint);
            switch (menuItemIndex) {
                case 0:
                    Intent i = new Intent(getActivity(), activity_parameters.class);
                    i.putExtra("editmi", true);
                    i.putExtra("num", globalint);
                    i.putExtra("action", task.getAction());
                    i.putExtra("name", task.getAd());
                    startActivity(i);
                    break;
                case 1:
                    final EditText editcek = new EditText(getActivity());
                    new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.renametask)).setView(editcek)
                            .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    task.setAd(editcek.getText().toString());
                                    SQL.upgradeTask(globalint, task);
                                    ((FragmentHolderActivity)getActivity()).upgradeDrawerProfile(globalint);
                                    myAdapter.notifyDataSetChanged();
                                }
                            }).show();
                    break;
                case 2:
                    new AlertDialog.Builder(getActivity()).setTitle("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TaskDelete(globalint);
                            myAdapter.satirKaldir(suanki2);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                    break;
                case 3:
                    openContextMenu(fab);
                    lastint=globalint;
                    selectmode=2;


                    break;

            }


        }


        return true;
    }
    private void showAdDiyalog(boolean trumu,int index){
        if(!trumu){
            addiyalog.show();
        }else{
            kernelTask taskim=SQL.getTask(index);
            taskim.setAction(mmode);
            SQL.upgradeTask(index, taskim);
            ((FragmentHolderActivity)getActivity()).upgradeDrawerProfile(index);
            myAdapter.notifyDataSetChanged();
        }


    }
    private HashMap<String,String> getInstalledApss(boolean getSysPackages){
        HashMap<String,String> amcalar=new HashMap<>();
        List<PackageInfo> packs =getActivity().getPackageManager().getInstalledPackages(0);
        String appname;
        String pname;
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            appname = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
            pname = p.packageName;
            amcalar.put(appname,pname);

        }
        return amcalar;

    }
    private void setAction(int menuItemIndex,final boolean editmi,final int editse){

        switch (menuItemIndex) {

            case 0:
                mmode="onScreenOn";
                showAdDiyalog(editmi,editse);
                break;
            case 1:
                mmode="onScreenOff";
                showAdDiyalog(editmi, editse);
                break;
            case 2:
                mmode="onLaunch-";
                boolean baslatula=true;
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                if(!Utils.checkPermission(getActivity())){
                    new AlertDialog.Builder(getActivity()).setMessage("I need permission for this action.").setPositiveButton("Okay,show me", new DialogInterface.OnClickListener() {
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
                    baslatula=false;
                }}
               if(baslatula){final ListView lv=new ListView(getActivity());
                final HashMap<String,String> uygulamalar=getInstalledApss(false);
                ArrayList<String> items=new ArrayList<>();

                for(String a:uygulamalar.keySet()){
                    items.add(a);

                }
                final AlertDialog ads=new AlertDialog.Builder(getActivity()).setView(lv).create();
                ArrayAdapter<String> adapterarray=new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
                lv.setAdapter(adapterarray);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        mmode="onLaunch-"+uygulamalar.get(lv.getItemAtPosition(position));
                        ads.dismiss();
                        showAdDiyalog(editmi, editse);
                    }
                });

                ads.show();}
                break;
            case 3:
                mmode="onBootComplete";
                showAdDiyalog(editmi, editse);
                break;
            case 4:

                final TextView tatava=new TextView(getActivity());

                final SeekBar sb=new SeekBar(getActivity());
                LinearLayout dikeylay=new LinearLayout(getActivity());
                dikeylay.setOrientation(LinearLayout.VERTICAL);
                dikeylay.addView(tatava);
                dikeylay.addView(sb);


                tatava.setText("%0");
                sb.setMax(100);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tatava.setText("%"+progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                new AlertDialog.Builder(getActivity()).setView(dikeylay).setTitle("Battery Percent")
                        .setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mmode="onBatteryLow-"+sb.getProgress();
                                showAdDiyalog(editmi, editse);
                            }
                        }).show();

                break;
            case 5:
                mmode="onActivate";
                showAdDiyalog(editmi, editse);
                break;
            case 6:

                final TimePicker picker=new TimePicker(getActivity());
                new AlertDialog.Builder(getActivity()).setView(picker).setPositiveButton(R.string.OK,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mmode="timeScheduler-"+picker.getCurrentHour()+":"+picker.getCurrentMinute();
                        showAdDiyalog(editmi, editse);
                    }
                }).show();
                break;
            case 7:
                final TimePicker pickerfu=new TimePicker(getActivity());
                new AlertDialog.Builder(getActivity()).setView(pickerfu).setTitle("From").setPositiveButton(R.string.OK,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mmode="timeSchedulerFT-"+pickerfu.getCurrentHour()+":"+pickerfu.getCurrentMinute();

                        final TimePicker pickerfu2=new TimePicker(getActivity());
                        new AlertDialog.Builder(getActivity()).setView(pickerfu2).setTitle("To").setPositiveButton(R.string.OK,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Utils.saatbuyukmu(pickerfu2.getCurrentHour(),pickerfu2.getCurrentMinute(),pickerfu.getCurrentHour(),pickerfu.getCurrentMinute())){
                                    mmode=mmode+"to"+pickerfu2.getCurrentHour()+":"+pickerfu2.getCurrentMinute();

                                    showAdDiyalog(editmi, editse);}else{mmode="";
                                    Toast.makeText(getActivity(), getString(R.string.wrongvalue), Toast.LENGTH_SHORT).show();}
                            }
                        }).show();

                    }
                }).show();
                break;
            case 8:
                mmode="onCharge";
                showAdDiyalog(editmi, editse);
                break;
            case 9:
                mmode="AlwaysOn";
                showAdDiyalog(editmi, editse);
                break;



        }

    }




    public fragmentTasker() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_fragment_tasker, container, false);
        setHasOptionsMenu(true);
        startFragment();
        return fragmentView;
    }
    @Override
    public void onActivityCreated(Bundle x){
        super.onActivityCreated(x);
        setHasOptionsMenu(true);
    }



    @Override
    public void onDetach() {
        super.onDetach();

    }




}
