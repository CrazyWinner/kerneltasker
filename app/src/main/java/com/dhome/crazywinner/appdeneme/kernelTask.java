package com.dhome.crazywinner.appdeneme;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Mertcan on 01.05.2015.
 */
public class kernelTask {
    public int id=-1;
    String ad;
    public String action;
    JSONArray yollar;
    JSONArray degerler;
    JSONArray types;
    private int active;
    public int solid;
    private String myImage;
    public kernelTask(){
        this.types=new JSONArray();
        this.myImage="";

    }
    public kernelTask(String ad,String action,String yollar,String degerler,int active,int solid){
    this.ad=ad;
        this.action=action;
        this.types=new JSONArray();
        try {
            this.yollar=new JSONArray(yollar);
            this.degerler=new JSONArray(degerler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.active=active;
        this.solid=solid;
        this.myImage="";

    }
    JSONArray getYollar(){
        return this.yollar;
    }
    public void setImage(String im){
        this.myImage=im;
    }
    public String getImagePath(){
        return myImage;
    }
    JSONArray getDegerler(){
        return this.degerler;
    }
    public boolean isActive(){
        return active == 1;
    }
    public boolean isSolid(){
        return solid == 1;
    }
    public void setActive(int active){
        this.active=active;
    }
    public void setSolid(int solid){this.solid=solid;}
    public String getAd(){
        return ad;
    }
    public String getAction(){
        return action;
    }
    void setAd(String ad){
        this.ad=ad;
    }
    public void setAction(String action){
        this.action=action;
    }
    void setYollar(String ici){
        try {
            this.yollar=new JSONArray(ici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void setTypes(String ici){
        try {
            this.types=new JSONArray(ici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    JSONArray getTypes(){
        return types;
    }
    void setDegerler(String ici){
        try {
            this.degerler=new JSONArray(ici);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
