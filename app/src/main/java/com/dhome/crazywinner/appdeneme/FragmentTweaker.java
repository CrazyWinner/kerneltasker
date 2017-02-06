package com.dhome.crazywinner.appdeneme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;




public class FragmentTweaker extends Fragment  {


    private JSONArray jsonArrayyollar=null;
    private JSONArray jsonArraydegerler=null;
    private FloatingActionButton fab;
    private String imagePath="";
    private kernelTask myTask;
    private myPagerAdapter myAdapterPager;
    private SharedPreferences kayitlar;
    private Boolean isActivetask=false;
    private  Boolean solidacaba=false;
    private sqLite SQL;
    private  Boolean modes;
    private HashMap<String,View> views;
    private HashMap<String,Integer> types;
    private String actionn="";
    private String taskName="";
    private Boolean editmi=false;
    private int editnum;
    private View fragmentView;
    private ViewPager mPager;


    private static int TYPE_CPUSPINNER=0;
    private static int TYPE_IOSPINNER=1;
    private static int TYPE_EDITTEXT=2;
    private static int TYPE_SWITCH=3;
    private static int TYPE_VOLTAGE=4;

    private LinearLayout VIEW_CPU;
    private LinearLayout VIEW_GPU;
    private LinearLayout VIEW_VOLTAGE;
    private LinearLayout VIEW_MEMORY;
    private LinearLayout VIEW_HOTPLUG;
    private LinearLayout VIEW_WAKE;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private boolean mParam2=false;
    private String mParam3="";
    private String mParam4="";






    static FragmentTweaker newInstance(int param1,boolean param2,String param3,String param4) {
        FragmentTweaker fragment = new FragmentTweaker();
        Bundle args = new Bundle();
        args.putInt("numara", param1);
        args.putBoolean("editmi", param2);
        args.putString("ad", param3);
        args.putString("action",param4);

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTweaker() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("numara");
            mParam2 = getArguments().getBoolean("editmi");
            mParam3 = getArguments().getString("ad");
            mParam4 = getArguments().getString("action");




        }
    }
    private List<Integer> getRelated(String yazi){
        List<Integer> donecek=new ArrayList<>();
        if(yazi.contains(" ")){
            for(String a:yazi.split(" ")){
                donecek.add(Integer.parseInt(a));
            }
        }else if(yazi.contains("-")){
            for(int i=Integer.parseInt(yazi.split("-")[0]);i<Integer.parseInt(yazi.split("-")[1]);i++){
                donecek.add(i);
            }
        }
         return donecek;
    }
    private void startFragment(){
        kayitlar = getActivity().getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);





        LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        views=new HashMap<>();
        types=new HashMap<>();






        editmi = mParam2;
        modes=getActivity() instanceof activity_parameters;

        fab =(FloatingActionButton)fragmentView.findViewById(R.id.fab);

        SQL=new sqLite(getActivity());


        myTask=new kernelTask("","",new JSONArray().toString(),new JSONArray().toString(),0,0);

        if (modes) {


            if (editmi) {
                editnum = mParam1;

                myTask=SQL.getTask(editnum);
                taskName = myTask.getAd();
                actionn=myTask.getAction();
                solidacaba=myTask.isSolid();
                isActivetask=myTask.isActive();
                imagePath=myTask.getImagePath();
            }else{
                taskName=mParam3;
                actionn=mParam4;
            }




        }

        jsonArrayyollar = myTask.getYollar();
        jsonArraydegerler = myTask.getDegerler();

        // jsonArray contains the data, use jsonArray.getString(index) to
        // retreive the elements




        View viewcpu=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_CPU=(LinearLayout)viewcpu.findViewById(R.id.linearparameter);
        ((TextView)viewcpu.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.CpuParam));


        View viewmemory=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_MEMORY=(LinearLayout)viewmemory.findViewById(R.id.linearparameter);
        ((TextView)viewmemory.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.MemoryParam));

        View viewgpu=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_GPU=(LinearLayout)viewgpu.findViewById(R.id.linearparameter);
        ((TextView)viewgpu.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.GPUParam));
        View viewvoltage=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_VOLTAGE=(LinearLayout)viewvoltage.findViewById(R.id.linearparameter);
        ((TextView)viewvoltage.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.VoltageParam));
        View viewhotplug=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_HOTPLUG=(LinearLayout)viewhotplug.findViewById(R.id.linearparameter);
        ((TextView)viewhotplug.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.HotplugParam));
        View viewwake=inflater.inflate(R.layout.fragment_cpu_parameter,null);
        VIEW_WAKE=(LinearLayout)viewwake.findViewById(R.id.linearparameter);
        ((TextView)viewwake.findViewById(R.id.textviewfragment)).setText(getResources().getString(R.string.WakeParam));

        myAdapterPager=new myPagerAdapter();
         List<Integer> relateds=new ArrayList<>();
        for(int canimcim=0;canimcim<8;canimcim++) {

            if (!relateds.contains(canimcim)){
                parametreEkle(Paths.CPU_MIN_FREQ.replace("%d", "" + canimcim), TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS.replace("%d", "" + canimcim), "Cpu"+ canimcim+" min", getResources().getString(R.string.cpuminval), VIEW_CPU);
            parametreEkle(Paths.CPU_MAX_FREQ.replace("%d", "" + canimcim), TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS.replace("%d", "" + canimcim), "Cpu"+ canimcim+" max", getResources().getString(R.string.cpumaxval), VIEW_CPU);
            parametreEkle(Paths.CPU_SCALING_GOVERNOR.replace("%d", "" + canimcim), TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_GOVERNORS.replace("%d", "" + canimcim), "Cpu"+ canimcim+" governor" , getResources().getString(R.string.cpugovernor), VIEW_CPU);
                parametreEkle(Paths.CPU_MAX_SCREEN_OFF_FREQ.replace("%d",""+canimcim), TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "Cpu"+canimcim+" screen off max", getResources().getString(R.string.screenoffmax), VIEW_CPU);
                parametreEkle(Paths.CPU_VOLTAGE.replace("%d",""+canimcim), TYPE_VOLTAGE, null, getResources().getString(R.string.cpuvoltage), "", VIEW_VOLTAGE);

        }
            if (new File(Paths.CPUS_RELATED.replace("%d", ""+canimcim)).exists()) {
                String buyukkucuk = Utils.readText(Paths.CPUS_RELATED.replace("%d", ""+canimcim));
                relateds.addAll(getRelated(buyukkucuk));

            }
        }

        parametreEkle(Paths.GPU_MAX_KGSL2D0_QCOM_FREQ, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_KGSL2D0_QCOM_FREQS, "Gpu 2d clock", getResources().getString(R.string.gpu2dclock), VIEW_GPU);
        parametreEkle(Paths.GPU_SCALING_KGSL2D0_QCOM_GOVERNOR, TYPE_IOSPINNER, null, "Gpu 2d governor", getResources().getString(R.string.gpu2dgovernor), VIEW_GPU);
        parametreEkle(Paths.GPU_MAX_KGSL3D0_QCOM_FREQ, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_KGSL3D0_QCOM_FREQS, "Gpu 3d clock", getResources().getString(R.string.gpu3dclock), VIEW_GPU);
        parametreEkle(Paths.GPU_SCALING_KGSL3D0_QCOM_GOVERNOR, TYPE_IOSPINNER, null, "Gpu 3d governor", getResources().getString(R.string.gpu3dgovernor), VIEW_GPU);
        parametreEkle(Paths.IO_INTERNAL_SCHEDULER, TYPE_IOSPINNER, null, "IO internal scheduler", getResources().getString(R.string.IOval), VIEW_MEMORY);
        parametreEkle(Paths.IO_EXTERNAL_SCHEDULER, TYPE_IOSPINNER, null, "IO external scheduler", getResources().getString(R.string.IOval), VIEW_MEMORY);
        parametreEkle(Paths.TCP_CONGESTION, TYPE_CPUSPINNER, Paths.TCP_AVAILABLE_CONGESTIONS, "TCP", getResources().getString(R.string.TCPval), VIEW_MEMORY);
        parametreEkle(Paths.GPU_OVERCLOCK, TYPE_EDITTEXT, null, "Gpu overclock", getResources().getString(R.string.gpuclock), VIEW_GPU);
        parametreEkle(Paths.GPU_MAX_FDB00000_QCOM_FREQ, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_FDB00000_QCOM_FREQS, "Gpu clock", getResources().getString(R.string.gpuclock), VIEW_GPU);
        parametreEkle(Paths.GPU_MAX_1C00000_QCOM_FREQ, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_1C00000_QCOM_FREQ, "Gpu clock", getResources().getString(R.string.gpuclock), VIEW_GPU);
        parametreEkle(Paths.GPU_SCALING_FDB00000_QCOM_GOVERNOR, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_FDB00000_QCOM_GOVERNORS, "Gpu governor", getResources().getString(R.string.gpugovernor), VIEW_GPU);
        parametreEkle(Paths.GPU_MAX_OMAP_FREQ, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_OMAP_FREQS, "Gpu clock", getResources().getString(R.string.gpuclock), VIEW_GPU);
        parametreEkle(Paths.GPU_SCALING_OMAP_GOVERNOR, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_OMAP_GOVERNORS, "Gpu governor", getResources().getString(R.string.gpugovernor), VIEW_GPU);
        parametreEkle(Paths.GPU_SCALING_1C00000_QCOM_GOVERNOR, TYPE_CPUSPINNER, Paths.GPU_AVAILABLE_1C00000_QCOM_GOVERNORS, "Gpu govervor", getResources().getString(R.string.gpugovernor), VIEW_GPU);
        parametreEkle(Paths.IO_INTERNAL_READ_AHEAD, TYPE_EDITTEXT, null, "Read ahead internal", getResources().getString(R.string.readahead), VIEW_MEMORY);
        parametreEkle(Paths.IO_EXTERNAL_READ_AHEAD, TYPE_EDITTEXT, null, "Read ahead external", getResources().getString(R.string.readahead), VIEW_MEMORY);
        parametreEkle(Paths.DYNAMIC_FSYNC, TYPE_SWITCH, null, "Dynamic fsync", getResources().getString(R.string.fsyncval), VIEW_MEMORY);
        parametreEkle(Paths.FORCE_FAST_CHARGE, TYPE_SWITCH, null, "Force Fast Charge", getResources().getString(R.string.ffc2val), VIEW_MEMORY);
        parametreEkle(Paths.CPU_FAUX_VOLTAGE, TYPE_VOLTAGE, null, getResources().getString(R.string.cpuvoltage), "", VIEW_VOLTAGE);
        parametreEkle(Paths.GPU_VOLTAGE, TYPE_VOLTAGE, null, getResources().getString(R.string.gpuvoltage), "", VIEW_VOLTAGE);
        parametreEkle(Paths.LP_VOLTAGE, TYPE_VOLTAGE, null, getResources().getString(R.string.lpvoltage), "", VIEW_VOLTAGE);
        parametreEkle(Paths.RAM_VOLTAGE, TYPE_VOLTAGE, null, getResources().getString(R.string.ramvoltage), "", VIEW_VOLTAGE);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_ENABLE, TYPE_SWITCH, null, "Intelli plug active", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_ECO, TYPE_SWITCH, null, "Intelli plug eco", getString(R.string.hotplugecoenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_TOUCH_BOOST, TYPE_SWITCH, null, "Intelli plug touch boost", getString(R.string.hotplugtouchboostenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_HYSTERESIS, TYPE_EDITTEXT, null, "Intelli plug hysteresis", getString(R.string.hotplughysteresis), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_THRESHOLD, TYPE_EDITTEXT, null, "Intelli plug threshold", getString(R.string.hotplugthreshold), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_SCREEN_OFF_MAX, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "Intelli plug screen off max", getResources().getString(R.string.screenoffmax), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_ENABLE, TYPE_SWITCH, null, "Intelli plug active", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_DEBUG, TYPE_SWITCH, null, "Intelli plug debug", getString(R.string.hotplugdebugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_SUSPEND, TYPE_SWITCH, null, "Intelli plug suspend", getString(R.string.hotplugsuspendenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_CPUS_BOOSTED, TYPE_EDITTEXT, null, "Intelli plug cpus boosted", getString(R.string.hotplugcpusboosted), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_HYSTERESIS, TYPE_EDITTEXT, null, "HYSTERESIS", getString(R.string.hotplughysteresis), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_MAX_CPUS_ONLINE, TYPE_EDITTEXT, null, "Intelli plug max cpus online", getString(R.string.hotplugmaxcpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_MIN_CPUS_ONLINE, TYPE_EDITTEXT, null, "Intelli plug min cpus online", getString(R.string.hotplugmincpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_MAX_CPUS_ONLINE_SUSP, TYPE_EDITTEXT, null, "Intelli plug max cpus online suspend", getString(R.string.hotplugmaxcpususpend), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_SUSPEND_DEFER_TIME, TYPE_EDITTEXT, null, "Intelli plug suspend defer time", getString(R.string.hotplugdefertime), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_DEFER_SAMPLING, TYPE_EDITTEXT, null, "Intelli plug defer sampling", getString(R.string.hotplugdefersampling), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_BOOST_LOCK_DURATION, TYPE_EDITTEXT, null, "Intelli plug boost lock duration", getString(R.string.hotplugboostduration), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_DOWN_LOCK_DURATION, TYPE_EDITTEXT, null, "Intelli plug down lock duration", getString(R.string.hotplugdownduration), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_THRESHOLD, TYPE_EDITTEXT, null, "Intelli plug threshold", getString(R.string.hotplugthreshold), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_FSHIFT, TYPE_EDITTEXT, null, "Intelli plug shift",getString(R.string.hotplugshift), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_INTELLI_PLUG_5_SCREEN_OFF_MAX, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "Intelli plug screen off max", getResources().getString(R.string.screenoffmax), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_ENABLE, TYPE_SWITCH, null, "Blu plug enable", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_DOWN_TIMER_CNT, TYPE_EDITTEXT, null, "Blu plug down timer cnt", getString(R.string.hotplugtimercnt), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_POWERSAVER_MODE, TYPE_EDITTEXT, null, "Blu plug power save", "To activate type Y, to disactivate type N", VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_MAX_CORES_SCREEN_OFF, TYPE_EDITTEXT, null, getString(R.string.hotplugmaxcpusscreenoff), "", VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_MAX_FREQ_SCREEN_OFF, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, getString(R.string.hotplugmaxfreqscreenoff), getResources().getString(R.string.screenoffmax), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_MIN_ONLINE, TYPE_EDITTEXT, null, "Blu plug min cores online", getString(R.string.hotplugmincpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_MAX_ONLINE, TYPE_EDITTEXT, null, "Blu plug max cores online", getString(R.string.hotplugmaxcpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_UP_THRESHOLD, TYPE_EDITTEXT, null, "Blu plug up threshold", getString(R.string.hotplugthreshold), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_BLU_PLUG_UP_TIMER_CNT, TYPE_EDITTEXT, null, "Blu plug up timer cnt", getString(R.string.hotplugtimercnt), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_ENABLED, TYPE_SWITCH, null, "Mako hotplug enable", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_CORES_ON_TOUCH, TYPE_SWITCH, null, "Mako hotplug active cores on touch", getString(R.string.hotplugtouchcores), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_CPUFREQ_UNPLUG_LIMIT, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "Mako hotplug unplug cpu limit", getResources().getString(R.string.screenoffmax), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_FIRST_LEVEL, TYPE_EDITTEXT, null, "Mako hotplug first level", getString(R.string.hotplugfirstlevel), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_HIGH_LOAD_COUNTER, TYPE_EDITTEXT, null, "Mako hotplug high load counter", getString(R.string.hotplughighloadcounter), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_LOAD_THRESHOLD, TYPE_EDITTEXT, null, "Mako hotplug load threshold", getString(R.string.hotplugloadthreshold), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_MAX_LOAD_COUNTER, TYPE_EDITTEXT, null, "Mako hotplug max load counter",getString(R.string.hotplugmaxloadcounter), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_MIN_TIME_CPU_ONLINE, TYPE_EDITTEXT, null, "Mako hotplug min time cpu online", getString(R.string.hotplugmintimecpuonline), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_TIMER, TYPE_EDITTEXT, null, "Mako hotplug timer", getString(R.string.hotplugtimer), VIEW_HOTPLUG);
        parametreEkle(Paths.MAKO_HOTPLUG_SUSPEND_FREQ, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "Mako hotplug suspend freq", getResources().getString(R.string.screenoffmax), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_ENABLE, TYPE_SWITCH, null, "MSM hotplug active", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_ENABLE_2, TYPE_SWITCH, null, "MSM hotplug active", getString(R.string.hotplugenable), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_MIN_CPUS_ONLINE, TYPE_EDITTEXT, null, "MSM hotplug min cpus online", getString(R.string.hotplugmincpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_MAX_CPUS_ONLINE, TYPE_EDITTEXT, null, "MSM hotplug max cpus online", getString(R.string.hotplugmaxcpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_CPUS_BOOSTED, TYPE_EDITTEXT, null, "MSM hotplug cpus boosted", getString(R.string.hotplugcpusboosted), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_MAX_CPUS_ONLINE_SUSP, TYPE_EDITTEXT, null, "MSM hotplug max cpus online suspend", getString(R.string.hotplugsuspend), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_BOOST_LOCK_DURATION, TYPE_EDITTEXT, null, "MSM hotplug boost lock duration", getString(R.string.hotplugboostduration), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_DOWN_LOCK_DURATION, TYPE_EDITTEXT, null, "MSM hotplug down lock duration", getString(R.string.hotplugdownduration), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_UPDATE_RATE, TYPE_CPUSPINNER, Paths.HOTPLUG_MSM_UPDATE_RATES, "MSM hotplug update rate", getString(R.string.hotplugupdaterate), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_HISTORY_SIZE, TYPE_EDITTEXT, null, "MSM hotplug history size", getString(R.string.hotplughistorysize), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_FAST_LANE_LOAD, TYPE_EDITTEXT, null, "MSM hotplug fast lane load", getString(R.string.hotplugfastlaneload), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_FAST_LANE_MIN_FREQ, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "MSM hotplug fast lane min freq", getString(R.string.hotplugfastlaneminfreq), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_OFFLINE_LOAD, TYPE_EDITTEXT, null, "MSM hotplug offline load", getString(R.string.hotplugofflineload), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_HP_IO_IS_BUSY, TYPE_SWITCH, null, "MSM hotplug HP IO is busy", getString(R.string.hotplughpioisbusy), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_IO_IS_BUSY, TYPE_SWITCH, null, "MSM hotplug IO is busy", getString(R.string.hotplugioisbusy), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_SUSPEND_MAX_CPUS, TYPE_EDITTEXT, null, "MSM hotplug suspend max cpus", getString(R.string.hotplugsuspendmaxcpus), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_SUSPEND_FREQ, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "MSM hotplug suspend freq", getString(R.string.hotplugsuspendfreq), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_SUSPEND_MAX_FREQ, TYPE_CPUSPINNER, Paths.CPU_AVAILABLE_FREQS, "MSM hotplug suspend max freq", getString(R.string.hotplugsuspendmaxfreq), VIEW_HOTPLUG);
        parametreEkle(Paths.HOTPLUG_MSM_SUSPEND_DEFER_TIME, TYPE_EDITTEXT, null, "MSM hotplug suspend defer time", getString(R.string.hotplugdefertime), VIEW_HOTPLUG);

        parametreEkle(Paths.LGE_TOUCH_DT2W, TYPE_SWITCH, null, "DT2W", getString(R.string.dt2w), VIEW_WAKE);
        parametreEkle(Paths.LGE_TOUCH_CORE_DT2W, TYPE_SWITCH, null, "DT2W", getString(R.string.dt2w), VIEW_WAKE);
        parametreEkle(Paths.DT2W, TYPE_SWITCH, null, "DT2W", getString(R.string.dt2w), VIEW_WAKE);
        parametreEkle(Paths.TOUCH_PANEL_DT2W, TYPE_SWITCH, null, "DT2W", getString(R.string.dt2w), VIEW_WAKE);
        parametreEkle(Paths.SW2, TYPE_SWITCH, null, "S2W", getString(R.string.s2w), VIEW_WAKE);
        parametreEkle(Paths.S2W_ONLY, TYPE_SWITCH, null, "Only S2W", getString(R.string.s2w), VIEW_WAKE);
        parametreEkle(Paths.TSP_T2W, TYPE_SWITCH, null, "T2W", getString(R.string.t2w), VIEW_WAKE);
        parametreEkle(Paths.TOUCHWAKE_T2W, TYPE_SWITCH, null, "T2W", getString(R.string.t2w), VIEW_WAKE);
        myAdapterPager.addView(viewcpu,"CPU");
        if(VIEW_MEMORY.getChildCount()==1){
            TextView tw=new TextView(getActivity());
            tw.setText(getString(R.string.nosupport));
            VIEW_MEMORY.addView(tw);
            if(!kayitlar.getBoolean("mysettings.hideparameter",false)){  myAdapterPager.addView(viewmemory,"Memory");

            }
        }else{myAdapterPager.addView(viewmemory,"Memory");
        }
        if(VIEW_GPU.getChildCount()==1){
            TextView tw=new TextView(getActivity());
            tw.setText(getString(R.string.nosupport));
            VIEW_GPU.addView(tw);
            if(!kayitlar.getBoolean("mysettings.hideparameter",false)){  myAdapterPager.addView(viewgpu,"GPU");

            }
        }else{myAdapterPager.addView(viewgpu,"GPU");}
        if(VIEW_VOLTAGE.getChildCount()==1){
            TextView tw=new TextView(getActivity());
            tw.setText(getString(R.string.nosupport));

            VIEW_VOLTAGE.addView(tw);
            if(!kayitlar.getBoolean("mysettings.hideparameter",false)){  myAdapterPager.addView(viewvoltage,"Voltage");

            }
        }else{myAdapterPager.addView(viewvoltage,"Voltage");
        }
        if(VIEW_HOTPLUG.getChildCount()==1){
            TextView tw=new TextView(getActivity());
            tw.setText(getString(R.string.nosupport));
            VIEW_HOTPLUG.addView(tw);
            if(!kayitlar.getBoolean("mysettings.hideparameter",false)){  myAdapterPager.addView(viewhotplug,"Hotplug");

            }
        }else{myAdapterPager.addView(viewhotplug,"Hotplug");}
        if(VIEW_WAKE.getChildCount()==1){
            TextView tw=new TextView(getActivity());
            tw.setText(getString(R.string.nosupport));
            VIEW_WAKE.addView(tw);
            if(!kayitlar.getBoolean("mysettings.hideparameter",false)){  myAdapterPager.addView(viewwake,"Wake");

            }
        }else{myAdapterPager.addView(viewwake,"Wake");}

        fab.attachToScrollView((ObservableScrollView) viewcpu.findViewById(R.id.observablescroll));
        fab.attachToScrollView((ObservableScrollView)viewgpu.findViewById(R.id.observablescroll));
        fab.attachToScrollView((ObservableScrollView)viewmemory.findViewById(R.id.observablescroll));
        fab.attachToScrollView((ObservableScrollView)viewvoltage.findViewById(R.id.observablescroll));
        fab.attachToScrollView((ObservableScrollView)viewhotplug.findViewById(R.id.observablescroll));
        fab.attachToScrollView((ObservableScrollView)viewwake.findViewById(R.id.observablescroll));

// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) fragmentView.findViewById(R.id.pager);

        mPager.setAdapter(myAdapterPager);
        mPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                fab.show(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply_void();
            }
        });
        if(!modes){
        ((FragmentHolderActivity) getActivity()).setStarted();}
        if(modes){((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(taskName);}else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Parameters");
        }
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) fragmentView.findViewById(R.id.materialTabHost);

        tabs.setViewPager(mPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // When an Image is picked
        if (requestCode == 15 && resultCode == Activity.RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            imagePath=selectedImage.toString();





        }}
    private void apply_void(){
        Boolean oldumu=false;
        List<String> yollar=new LinkedList<>();
        List<String> degerler=new LinkedList<>();
        List<Integer> typeler=new LinkedList<>();
        for (String s : views.keySet()) {

            int type=types.get(s);
            Boolean yap=true;
            switch (type){
                //edittext
                case 0:{
                    View vv=views.get(s);
                    if(modes){
                        if(!((CheckBox)vv.findViewById(R.id.checboxparam)).isChecked()) yap=false;
                    }
                    if(yap){
                        EditText editparam=(EditText)vv.findViewById(R.id.edittext);
                        yollar.add(s);
                        typeler.add(type);

                        degerler.add(editparam.getText().toString());
                        oldumu=true;}
                    break;
                }
                //iospinner
                case 1:{
                    View vv=views.get(s);
                    if(modes){
                        if(!((CheckBox)vv.findViewById(R.id.checboxparam)).isChecked())yap=false;}
                    if(yap) {
                        Spinner spinnerparam=(Spinner)vv.findViewById(R.id.spinner);
                        yollar.add(s);
                        typeler.add(type);
                        degerler.add(spinnerparam.getSelectedItem().toString());
                        oldumu=true;}
                    break;
                }
                //cpuspinner
                case 2:{
                    View vv=views.get(s);
                    if(modes){
                        if(!((CheckBox)vv.findViewById(R.id.checboxparam)).isChecked())yap=false;}
                    if(yap){
                        Spinner spinnerparam=(Spinner)vv.findViewById(R.id.spinner);
                        yollar.add(s);
                        typeler.add(type);
                        degerler.add(spinnerparam.getSelectedItem().toString());

                        oldumu=true;}
                    break;
                }
                //switch
                case 3:{
                    View vv=views.get(s);
                    if(modes){
                        if(!((CheckBox)vv.findViewById(R.id.checboxparam)).isChecked())yap=false;}
                    if(yap){
                        Switch switchparam=(Switch)vv.findViewById(R.id.swicth);
                        yollar.add(s);
                        typeler.add(type);
                        String eklesene=switchparam.isChecked() ? "1" : "0";

                        degerler.add(eklesene);
                        oldumu=true;}
                    break;
                }
                //voltage
                case 4:{
                    View vv=views.get(s);
                    if(modes){
                        if(!((CheckBox)vv.findViewById(R.id.checboxparam)).isChecked())yap=false;}
                    if(yap){
                        String aa="";

                        List<EditText> editler=Utils.getEditTexts((LinearLayout)vv.findViewById(R.id.voltagelinear));
                        for (EditText editText : editler) {
                            aa=aa+editText.getText().toString()+" ";
                        }
                        if(aa.length()>1){
                            aa=aa.substring(0,aa.length()-1);
                        }
                        yollar.add(s);
                        typeler.add(type);
                        degerler.add(aa);
                        oldumu=true;}
                    break;
                }

            }

        }

        int donecekid=-1;
        if(oldumu){


            kernelTask kayitTask=new kernelTask();

            if(modes &&!editmi){isActivetask=kayitlar.getBoolean("mysettings.activeon",false);}
            kayitTask.setActive(isActivetask ? 1 : 0);
            kayitTask.setAd(taskName);
            kayitTask.setAction(actionn);
            kayitTask.setYollar(new JSONArray(yollar).toString());
            kayitTask.setDegerler(new JSONArray(degerler).toString());
            kayitTask.setSolid(solidacaba?1:0);
            kayitTask.setImage(imagePath);

            if(solidacaba){
                kayitTask.setTypes(new JSONArray(typeler).toString());
            }


            if(modes) {
                if (!editmi) {

                    donecekid = SQL.insertTask(kayitTask);


                } else {

                    SQL.upgradeTask(editnum, kayitTask);
                }
            }else{donecekid = SQL.insertTask(kayitTask);
                SettingApplier.Apply(getActivity(),donecekid,true);}









        }



            Log.i("truefalse",""+(modes && !editmi &&oldumu));
        if(getActivity() instanceof activity_parameters){
            ((activity_parameters)getActivity()).geridon(donecekid,modes && !editmi &&oldumu);}

    }






    private String settenAl(String olan){

        for(int b=0;b<jsonArrayyollar.length();b++){
            try {
                if(jsonArrayyollar.get(b).equals(olan)){

                    return jsonArraydegerler.getString(b);}
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";

    }
    private void parametreEkle(String parameter,int type,String olanlar,String ad,String aciklama,LinearLayout vi){

        switch(type){
            case 0:{
                if(new File(parameter).exists() && new File(olanlar).exists()) {
                    String _minvalue = Utils.readText(parameter);
                    String _minvalue2 = settenAl(parameter);
                    if (editmi && !_minvalue2.equals("")) {
                        _minvalue = _minvalue2;
                    }

                    CardView ekle = Utils.createParameter(getActivity(), Utils.CPUSPINNER, Utils.readText(olanlar), _minvalue, modes, ad, aciklama
                            , editmi && !_minvalue2.equals(""));
                    vi.addView(ekle);
                    views.put(parameter, ekle);
                    types.put(parameter, Utils.CPUSPINNER);
                }else if (new File(parameter).exists()){
                    parametreEkle(parameter,TYPE_EDITTEXT,null,ad,aciklama,vi);
                }
            }break;
            case 1:{
                if(new File(parameter).exists()){
                    String _maxvalue="";
                    String _minvalue2=settenAl(parameter);
                    if(editmi && !_minvalue2.equals("")){_maxvalue=_minvalue2;}

                    CardView ekle=Utils.createParameter(getActivity(),Utils.IOSPINNER,Utils.readText(parameter),_maxvalue,modes,ad,aciklama
                            ,editmi && !_minvalue2.equals(""));
                    vi.addView(ekle);
                    views.put(parameter,ekle);
                    types.put(parameter,Utils.IOSPINNER);}}break;
            case 2:{
                if(new File(parameter).exists()){
                    String _minvalue=Utils.readText(parameter);
                    String _minvalue2=settenAl(parameter);
                    if(editmi && !_minvalue2.equals("")){_minvalue=_minvalue2;}
                    CardView ekle=Utils.createParameter(getActivity(),Utils.EDITTEXT,_minvalue,_minvalue,modes,ad,aciklama
                            ,editmi && !_minvalue2.equals(""));
                    vi.addView(ekle);
                    views.put(parameter,ekle);
                    types.put(parameter,Utils.EDITTEXT);}}break;
            case 3:{
                if(new File(parameter).exists()){
                    String _minvalue=Utils.readText(parameter);
                    String _minvalue2=settenAl(parameter);
                    if(editmi&&!_minvalue2.equals("")){_minvalue=_minvalue2;}
                    CardView ekle=Utils.createParameter(getActivity(),Utils.SWITCH,_minvalue,_minvalue,modes,ad,aciklama
                            ,editmi && !_minvalue2.equals(""));
                    vi.addView(ekle);
                    views.put(parameter,ekle);
                    types.put(parameter,Utils.SWITCH);}}break;
            case 4:{
                if(new File(parameter).exists()){
                    String _minvalue2="";
                    String _minvalue=Utils.readText(parameter);

                    if(editmi ){_minvalue2=settenAl(parameter);}

                    CardView ekle=Utils.createParameter(getActivity(),Utils.VOLTAGE,_minvalue,_minvalue2,modes,ad,aciklama
                            ,editmi && !_minvalue2.equals(""));
                    vi.addView(ekle);
                    views.put(parameter,ekle);
                    types.put(parameter,Utils.VOLTAGE);}}break;

        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.menu_activity_change, menu);
        MenuItem itemim=menu.findItem(R.id.optionsolid);

        if(modes ||editmi){
            itemim.setChecked(solidacaba);}else{
            menu.removeItem(R.id.optionsolid);
            menu.removeItem(R.id.optionsetimage);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch eventSystem.exit(0)



        switch (item.getItemId()){
            case R.id.optionprev:
                if(mPager.getCurrentItem()!=0){
                    mPager.setCurrentItem(mPager.getCurrentItem()-1,true);}
                break;
            case R.id.optionnext:
                if(mPager.getCurrentItem()!=myAdapterPager.getCount())
                    mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
                break;
            case R.id.optionsolid:
                solidacaba=!solidacaba;
                item.setChecked(solidacaba);
                return true;
            case R.id.optionsetimage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 15);
                break;


        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView=inflater.inflate(R.layout.fragment_fragment_tweaker, container, false);
        startFragment();
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event




    @Override
    public void onDetach() {
        super.onDetach();

    }



}
