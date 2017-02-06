package com.dhome.crazywinner.appdeneme;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;





public class Utils{
    static int ONCHARGE=0;
    static int ONSCREENOFF=1;
    static int ONBATTERYLOW=2;
    static int TIMESCHEDULERFT=3;
    static int TIMESCHEDULER=4;
    static int ONLAUNCHING=5;
    static int ONSCREENON=6;
    static int ALWAYSON=7;
    private static String[] actionss={"onCharge","onScreenOff","onBatteryLow-","timeSchedulerFT-","timeScheduler-","onLaunch-","onScreenOn","AlwaysOn","onActivate","onBootComplete"};

    static List<kernelTask> arrangeList(List<kernelTask> arrangeme){
        ArrayList<ArrayList<kernelTask>> tasks=new ArrayList<>();
        for(int i=0;i<8;i++){
            tasks.add(new ArrayList<kernelTask>());
        }
        for(kernelTask myTask:arrangeme){
            int myaction=getAction(myTask.getAction());
            if(myaction<tasks.size()){
            tasks.get(myaction).add(myTask);}

        }
        arrangeme.clear();
        for(ArrayList<kernelTask> taskk:tasks){
            arrangeme.addAll(taskk);
            taskk.clear();
        }
        tasks.clear();

        return arrangeme;

    }
    static int getAction(String action){

        for (int i = 0; i < actionss.length; i++) {
            String actiondene=actionss[i];
            if(action.length()>=actiondene.length()){
                if(action.substring(0,actiondene.length()).equals(actiondene)){
                    return i;
                }
            }
        }
        return -1;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    static Boolean checkPermission(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            @SuppressLint("InlinedApi") int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    static boolean loadProfile(Context context,String yol){
        String dosya=getStringFromFile(yol);
        String[] degers=dosya.split("\n");
        if(degers.length!=5){return false;}
        kernelTask task=new kernelTask();
        task.ad=degers[0];
        task.action=degers[1];
        try {
            task.types=new JSONArray(degers[2]);
            task.yollar=new JSONArray(degers[3]);
            task.degerler=new JSONArray(degers[4]);

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        if(task.degerler.length()!=task.yollar.length()){
            return false;
        }
        sqLite sqHelper=new sqLite(context);
        sqHelper.insertTask(task);
        return true;
    }
    // working

    static void saveProfile(kernelTask task){

        String kayit=task.ad+"\n"+task.action+"\n"+task.getTypes()+"\n"+task.getYollar()+"\n"+task.getDegerler();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String filename=task.getAd()+"-"+timeStamp+".kts";
        File myDir=new File(Environment.getExternalStorageDirectory()+"/Kernel Tasker");
        if(!myDir.exists()){
            myDir.mkdir();
        }
        File yazilacak=new File(Environment.getExternalStorageDirectory()+"/Kernel Tasker/"+filename);
        if(!yazilacak.exists()){
            try {
                yazilacak.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos=new FileOutputStream(yazilacak);
            fos.write(kayit.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static String getStringFromFile (String filename)  {
        File file = new File(filename);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }
static String readText(String file){
    BufferedReader buffered_reader=null;
    StringBuilder s=new StringBuilder();
    if(new File(file).exists()){
        try
        {
            //InputStream istream = Runtime.getRuntime().exec("cat /sys/kernel/abb-chargalg/eoc_status").getInputStream();
            //InputStreamReader istream_reader = new InputStreamReader(istream);
            //buffered_reader = new BufferedReader(istream_reader);
            buffered_reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = buffered_reader.readLine()) != null)
            {
                s.append(line);
                s.append('\n');
            }

        }
        catch (IOException e)
        {
            // TODO
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (buffered_reader != null)
                    buffered_reader.close();
            }
            catch (IOException ex)
            {
                // TODO
                ex.printStackTrace();
            }
        }
        String selam=s.toString();

        if(selam.length()<1){selam=RootUtils.runCommandCallback("cat "+file)+" ";}
        return selam.substring(0,selam.length()-1);
    }else{return "";}

}
    static void runCommand(String command){


        RootUtils.runCommand(command);
    }
    private static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
    static int CPUSPINNER=2;
    static int IOSPINNER=1;
    static int EDITTEXT=0;
    static int SWITCH=3;
    static int VOLTAGE=4;

static CardView createParameter(final Context context,int mode,String text,String nerde,Boolean cholsunmu,String ad,String aciklama,boolean isChecked){


    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE);
    View parameterview=null;
    LinearLayout donecek = null;
   if(mode!=4){
   parameterview = mInflater.inflate(R.layout.parameterview, null);
 donecek= (LinearLayout) parameterview.findViewById(R.id.linear1);
    TextView degis=(TextView)parameterview.findViewById(R.id.textViewparam);

    degis.setText(ad);
    degis=(TextView)parameterview.findViewById(R.id.textViewdeparam);
    degis.setText(aciklama);

    CheckBox v=(CheckBox)donecek.findViewById(R.id.checboxparam);
    if(!cholsunmu){

         donecek.removeView(v);}else{
         v.setChecked(isChecked);
    }}
    switch (mode){
        case 1:{
            final Spinner IOspinner=(Spinner)donecek.findViewById(R.id.spinner);
            final List<String> IOdata = new ArrayList<>();
            int IOnerde=0;

            String[] IOlardeger=text.split(" ");

            for (int baba = 0; baba < IOlardeger.length; baba++) {
                String canim=IOlardeger[baba];
                IOdata.add(canim);


                   if(canim.length()>2){
                if(canim.charAt(0)=='[' && canim.charAt(canim.length()-1)==']'){
                    IOdata.set(baba,canim.substring(1,canim.length()-1));
                    canim =canim.substring(1,canim.length()-1);
                    IOnerde = baba;
                }}
               if(canim.equals(nerde)){
                    IOnerde = baba;

                }
            }


            ArrayAdapter adapterIO = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, IOdata);
            adapterIO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            IOspinner.setAdapter(adapterIO);
            IOspinner.setSelection(IOnerde);
            donecek.removeView(donecek.findViewById(R.id.swicth));
            donecek.removeView(donecek.findViewById(R.id.edittext));
            CardView donecek2=(CardView)parameterview.findViewById(R.id.cardView);

            donecek2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IOspinner.performClick();
                }
            });

            return donecek2;

        }

        case 0:{
            final EditText edit=(EditText)donecek.findViewById(R.id.edittext);
            edit.setText(text);
            if(cholsunmu&& !nerde.equals("")){
                edit.setText(nerde);
            }
            donecek.removeView(donecek.findViewById(R.id.spinner));
            donecek.removeView(donecek.findViewById(R.id.swicth));


            CardView donecek2=(CardView)parameterview.findViewById(R.id.cardView);
            donecek2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(
                    edit.requestFocus()){
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);}
                }
            });

            return donecek2;

        }
        case 2:{
            final Spinner CpuSpinner =(Spinner)donecek.findViewById(R.id.spinner);
            final List<String> freqs = new ArrayList<String>();
            final String[] freqdeger=text.split(" ");

            int spinnernerde=0;
            for (int baba = 0; baba < freqdeger.length; baba++) {

                final String xss=freqdeger[baba];
                freqs.add(xss);

             if(xss.equals(nerde)){spinnernerde=baba;}

            }
            ArrayAdapter adapter1 = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, freqs);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            CpuSpinner.setAdapter(adapter1);
            CpuSpinner.setSelection(spinnernerde);
            donecek.removeView(donecek.findViewById(R.id.swicth));
            donecek.removeView(donecek.findViewById(R.id.edittext));
            CardView donecek2=(CardView)parameterview.findViewById(R.id.cardView);

            donecek2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CpuSpinner.performClick();
                }
            });

            return donecek2;


        }
        case 3:{

            donecek.removeView(donecek.findViewById(R.id.spinner));
            donecek.removeView(donecek.findViewById(R.id.edittext));
            if(cholsunmu&& !nerde.equals("")){
               text=nerde;
            }
            final Switch sttt=(Switch)(donecek.findViewById(R.id.swicth));
            if(text.equals("1")){sttt.setChecked(true);}
            CardView donecek2=(CardView)parameterview.findViewById(R.id.cardView);

            donecek2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sttt.setChecked(!sttt.isChecked());
                }
            });
            return donecek2;

        }
        case 4:{
            String[] values=nerde.split(" ");
            final View donsen = mInflater.inflate(R.layout.parameterviewvoltage, null);
            CheckBox vvs=(CheckBox)donsen.findViewById(R.id.checboxparam);
            if(!cholsunmu){

                LinearLayout dsad=(LinearLayout)donsen.findViewById(R.id.linearnaber2);
                dsad.removeView(vvs);}else{
                vvs.setChecked(isChecked);
            }
            TextView deneme=(TextView)donsen.findViewById(R.id.textViewdeneme);
            deneme.setText(ad);
            ImageButton arttir=(ImageButton)donsen.findViewById(R.id.button_arttir);
            arttir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                            final EditText editlenecek=new EditText(context);
                            editlenecek.setInputType(InputType.TYPE_CLASS_NUMBER);
                            AlertDialog diyalogarttir=new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.increase))
                                    .setView(editlenecek).setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int eklenecek=0;
                                            try{
                                            eklenecek=parseInt(editlenecek.getText().toString());


                                            }catch(NumberFormatException exception){

                                            }
                                            for (EditText editText : getEditTexts((LinearLayout)donsen.findViewById(R.id.voltagelinear))) {
                                                editText.setText(""+(parseInt(editText.getText().toString())+eklenecek));}
                                        }
                                    }).create();
                            diyalogarttir.show();





                }
            });
            ImageButton azalt=(ImageButton)donsen.findViewById(R.id.button_azalt);
            azalt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final EditText editlenecek=new EditText(context);
                    editlenecek.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog diyalogarttir=new AlertDialog.Builder(context).setTitle(context.getResources().getString(R.string.decrease))
                            .setView(editlenecek).setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int eklenecek=0;
                                    try{
                                        eklenecek=parseInt(editlenecek.getText().toString());


                                    }catch(NumberFormatException exception){

                                    }
                                    for (EditText editText : getEditTexts((LinearLayout)donsen.findViewById(R.id.voltagelinear))) {
                                        editText.setText(""+(parseInt(editText.getText().toString())-eklenecek));
                                    }
                                }
                            }).create();
                    diyalogarttir.show();





                }
            });
             LinkedHashMap<String,String> voltageTable=new LinkedHashMap<>();
            String CPUvoltDosya=text;
            int abisi=0;
                        while (CPUvoltDosya.contains(":")) {

                            int dd = CPUvoltDosya.indexOf(":");
                            int ss = CPUvoltDosya.indexOf('\n');
                            if (CPUvoltDosya.contains(""+'\n')) {
                                String eklenecek=CPUvoltDosya.substring(0, dd);
                                int babk=1;
                                String eklenecek2=CPUvoltDosya.substring(dd + 1, ss );
                                if(cholsunmu && values.length>1){
                                    String as=values[abisi];
                                    if(as!=null && !as.equals("")){
                                    eklenecek2=as;}}
                                while(voltageTable.containsKey(eklenecek)){
                                    eklenecek=eklenecek+repeat(" ",babk);
                                }
                                voltageTable.put(eklenecek, eklenecek2);

                                CPUvoltDosya = CPUvoltDosya.substring(ss + 1, CPUvoltDosya.length());
                            } else {
                                String eklenecek2=CPUvoltDosya.substring(dd + 1, CPUvoltDosya.length() );
                                if(cholsunmu && values.length>1){
                                    String as=values[abisi];
                                    if(as!=null && !as.equals("")){
                                        eklenecek2=as;}}

                                voltageTable.put(CPUvoltDosya.substring(0, dd), eklenecek2);


                                break;
                            }
                            abisi++;
                        }
                        LinearLayout voltagelin = (LinearLayout) donsen.findViewById(R.id.voltagelinear);

                        for (String s : voltageTable.keySet()) {
                            LinearLayout lin = new LinearLayout(context);
                            lin.setOrientation(LinearLayout.HORIZONTAL);
                            TextView xdd = new TextView(context);
                            EditText xdd2 = new EditText(context);
                            xdd2.setInputType(InputType.TYPE_CLASS_NUMBER);

                            xdd.setText(s);
                            xdd2.setText(voltageTable.get(s).replaceAll("\\D+", ""));



                            lin.addView(xdd);
                            View vvi = new View(context);
                            vvi.setLayoutParams(new LinearLayout.LayoutParams(
                                    0,
                                    0, 1));
                            lin.addView(vvi);
                            lin.addView(xdd2);
                            voltagelin.addView(lin);

                        }

            return (CardView)donsen.findViewById(R.id.cardView);

        }

    }

return null;
}
    static boolean saatbuyukmu(int h1,int m1,int h2,int m2){
    if(h1>h2){return true;}
        if(h1==h2){if(m1>m2)return true;}
    return false;
    }
static List<EditText> getEditTexts(LinearLayout layout){
    List<EditText> listedit=new ArrayList<>();
    int childcount = layout.getChildCount();

    for (int i=0; i < childcount; i++){
        View v = layout.getChildAt(i);

        if(v instanceof LinearLayout){
            LinearLayout laycik=(LinearLayout)v;
          for(int ss=0;ss<laycik.getChildCount();ss++){
              View sd=laycik.getChildAt(ss);
              if(sd instanceof EditText){
                  listedit.add((EditText)sd);

              }

          }


        }

    }
    return listedit;

}
   static boolean checkSolidTask(Context context,kernelTask task){

        JSONArray jsonArrayyollar;
        JSONArray jsonArraydegerler;
        JSONArray jsontypes;
        jsonArrayyollar = task.getYollar();
        jsonArraydegerler = task.getDegerler();
        jsontypes=task.getTypes();
        for(int i=0;i<jsonArrayyollar.length();i++){
            String deger="";
            String yol="";
            int type=-1;
            try {
                yol = jsonArrayyollar.getString(i);
                deger=jsonArraydegerler.getString(i);
                type=jsontypes.getInt(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             String deger2=convertTaskString(yol, type);

            if(!deger.equals(deger2)){

                SettingApplier.Apply(context,task.id,false);
                return true;
            }

        }
          return false;
    }
    private static String convertTaskString(String yol,int type){
        String text =readText(yol);
        switch (type){
            case 1:{
                String[] IOlardeger=text.split(" ");
                for (int baba = 0; baba < IOlardeger.length; baba++) {
                    String canim=IOlardeger[baba];
                    if(canim.charAt(0)=='[' && canim.charAt(canim.length()-1)==']'){
                        return canim.substring(1,canim.length()-1);
                    }
                }
                return "";
            }

            case 0:{
             return text;

            }
            case 2:{
               return text;


            }
            case 3:{
                return text;

            }
            case 4:{
                String slm="";
                String CPUvoltDosya=text;
                while (CPUvoltDosya.contains(":")) {
                    int dd = CPUvoltDosya.indexOf(":");
                    int ss = CPUvoltDosya.indexOf('\n');
                    if (CPUvoltDosya.contains(""+'\n')) {

                        String eklenecek2=CPUvoltDosya.substring(dd + 1, ss );
                            slm=slm+" "+eklenecek2;

                        CPUvoltDosya = CPUvoltDosya.substring(ss + 1, CPUvoltDosya.length());
                    } else {
                        String eklenecek2=CPUvoltDosya.substring(dd + 1, CPUvoltDosya.length() );
                        slm=slm+" "+eklenecek2;
                        break;
                    }
                }
                slm=slm.substring(1,slm.length());
                return slm;




            }

        }
       return "";
    }
    static boolean setFragment(Context context,Fragment fragment2){
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();


                 trans
                .replace(R.id.content_frame, fragment2).addToBackStack(null)
                .commit();
        return true;
    }

    static void setStatusBarColor(Context context,View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = ((Activity)context).getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusBar.getLayoutParams().height = getStatusBarHeight(context);
            statusBar.setBackgroundColor(color);
        }
    }
    static void drawerHazirla(final Context context,final int num,ActionBarDrawerToggle drawerToggle){
        final SharedPreferences kayitlar=context.getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);
        final DrawerLayout drawerLayout=(DrawerLayout)((Activity) context).findViewById(R.id.DrawerLayout);
        final DrawerView drawer=(DrawerView)((Activity) context).findViewById(R.id.drawerView);
         View statusBAr=((Activity)context).findViewById(R.id.statusBarBackground);


        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(drawer);
        sqLite sqlite=new sqLite(context);
        DrawerProfile profileadd=new DrawerProfile().setId(2147483647).setBackground(context.getResources().getDrawable(R.drawable.back_poly))
                .setName("None").setDescription("None");
        profileadd.setRoundedAvatar((BitmapDrawable) context.getResources().getDrawable(R.drawable.icon4));
        drawer.addProfile(profileadd);
        boolean tamamyap=kayitlar.getBoolean("mysettings.forceset",false);
        int bunuyap=kayitlar.getInt("mysettings.forcesetnum",2147483647);
        for (kernelTask kerneltask : sqlite.getAllTasks()) {
            DrawerProfile profile=new DrawerProfile().setId(kerneltask.id).setBackground(context.getResources().getDrawable(R.drawable.back_poly))
                    .setName(kerneltask.getAd()).setDescription(kerneltask.getAction());
            if(kerneltask.getImagePath()!=null &&!kerneltask.getImagePath().equals("")){

                profile.setRoundedAvatar(getBitmapFromPath(context,kerneltask.getImagePath(),78));
            }else{profile.setRoundedAvatar((BitmapDrawable)context.getResources().getDrawable(R.drawable.icon4));}
            drawer.addProfile(profile);
            if(bunuyap==profile.getId() && tamamyap){
                drawer.selectProfile(profile);
            }

        }

        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.home12)).setTextPrimary("Home").setTextSecondary(
                "Home screen"
        ));
        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.tweaker)).setTextPrimary("Tweaker").setTextSecondary(
                "Change your kernel settings"
        ));
        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.tasker)).setTextPrimary("Tasker")
        .setTextSecondary("Create or edit tasks"));
        drawer.addItem(new DrawerHeaderItem().setTextPrimary("Info"));
        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.info)).setTextPrimary("Kernel info").setTextSecondary("Your kernel information"));
        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.info)).setTextPrimary("About").setTextSecondary("About us"));
         drawer.addItem(new DrawerHeaderItem().setTextPrimary("Settings"));
        drawer.addItem(new DrawerItem().setImage(context.getResources().getDrawable(R.drawable.settings)).setTextPrimary("Settings").setTextSecondary("App settings"));


        drawer.setOnItemClickListener(
                new DrawerItem.OnItemClickListener() {
                    @Override
                    public void onClick(DrawerItem drawerItem, long l, int index) {

                        switch (index) {
                            case 0:
                                drawerLayout.closeDrawers();
                                ((FragmentHolderActivity)context).setSiradaki(new FragmentActivityMain());
                                drawer.selectItem(0);


                                break;
                            case 1:
                                drawerLayout.closeDrawers();
                                ((FragmentHolderActivity)context).setSiradaki(FragmentTweaker.newInstance(-1, false, "", ""));
                                drawer.selectItem(1);

                                break;
                            case 2:
                                drawerLayout.closeDrawers();
                                ((FragmentHolderActivity)context).setSiradaki(new fragmentTasker());

                                drawer.selectItem(2);
                                break;
                            case 4:
                                drawerLayout.closeDrawers();
                                new AlertDialog.Builder(context).setTitle("Kernel info").setMessage(Utils.readText("/proc/version")).show();
                                break;
                            case 5:
                                drawerLayout.closeDrawers();
                                new AlertDialog.Builder(context).setTitle("About us").setMessage("Created by Mertcan Ozdemir\n\nVersion: " + BuildConfig.VERSION_NAME
                                        + "\n\nUsed:\n\nappcompat-v7 by Google\n\nandroid-weak-handler by BadooMobile\n\ncardview-v7 by Google\n\nfloatingactionbutton by melnykov\n\n" +
                                        "recyclerview-v7 by Google\n\nmaterialdrawer by heinrichreimersoftware\n\ncircularimageview by pkmmte\n\nSu code by Grarak").show();
                                break;
                            case 7:
                                drawerLayout.closeDrawers();
                                Intent ii = new Intent(context, settingactivity.class);
                                context.startActivity(ii);
                                break;
                        }

                    }
                }
        );
        drawer.selectItem(num);

        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile drawerProfile, long l, DrawerProfile drawerProfile1, long l1) {
                int yapilacak=(int)l1;
                if (l == l1) {

                    for (DrawerProfile profile : drawer.getProfiles()) {
                        if (profile.getId() != l) {

                            drawer.selectProfile(profile);
                            yapilacak=(int)profile.getId();
                        }
                    }

                }

                if(yapilacak!=2147483647){
                    if(  kayitlar.getInt("mysettings.forcesetnum",-1)!=yapilacak ){
                        SettingApplier.Apply(context,yapilacak,false);
                        kayitlar.edit().putBoolean("mysettings.forceset",true).apply();
                        kayitlar.edit().putInt("mysettings.forcesetnum",yapilacak).apply();
                    }
                }else{kayitlar.edit().putBoolean("mysettings.forceset",false).apply();
                kayitlar.edit().putInt("mysettings.forcesetnum",-1).apply();}


            }
        });
        setStatusBarColor(context,statusBAr,Color.parseColor("#B71C1C"));

    }
    public static BitmapDrawable getBitmapFromPath(Context context,String path,int size){
        BitmapDrawable d;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        // Get the cursor
        Cursor cursor =context.getContentResolver().query(Uri.parse(path),
                filePathColumn, null, null, null);
        if(cursor!=null &&cursor.getCount()>0){
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap=decodeFile(new File(imgDecodableString),size);
        d = new BitmapDrawable(context.getResources(), bitmap);}else{
            d=(BitmapDrawable)context.getResources().getDrawable(R.drawable.icon4);

        }
        // Set the Image in ImageView after decoding the String
        return d;


    }
    private static Bitmap decodeFile(File f,final int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to


            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result =context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    static boolean recoverProfiles(Context context){

        SharedPreferences kayitlar=context.getSharedPreferences("com.dhome.crazywinner.appdeneme", Context.MODE_PRIVATE);

        if(kayitlar.contains("num")){
            SharedPreferences.Editor editor=kayitlar.edit();
            sqLite SQL=new sqLite(context);
            for(int i=0;i<kayitlar.getInt("num",0);i++){
                kernelTask task=new kernelTask();
                task.setAd(kayitlar.getString("ad" + i, ""));
                task.setActive(kayitlar.getBoolean("activee" + i, false) ? 1 : 0);
                task.setAction(kayitlar.getString("action" + i, ""));
                task.setYollar(kayitlar.getString("ayarlarsetyollar" + i, new JSONArray().toString()));
                task.setDegerler(kayitlar.getString("ayarlarsetdegerler" + i, new JSONArray().toString()));
                task.setSolid(kayitlar.getBoolean("solid" + i, false) ? 1 : 0);
                editor.remove("ad" + i);
                editor.remove("activee" + i);
                editor.remove("action" + i);
                editor.remove("ayarlarsetyollar" + i);
                editor.remove("ayarlarsetdegerler"+i);
                SQL.insertTask(task);
            }
            editor.remove("num");
            editor.apply();
            return true;
        }
        return false;
    }
    static int parseInt(String pars){
        int donecek=0;
        try{
            donecek=Integer.parseInt(pars);
        }catch (NumberFormatException ex){
            return 0;
        }
        return donecek;
    }
    static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
