package com.dhome.crazywinner.appdeneme;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class sqLite extends SQLiteOpenHelper {
    private final static String DATABASE_NAME="KernelTasker.db";
    private final static int version=3;
    private final static String TABLE_NAME="Tasks";
    SharedPreferences kayitlar;
    public sqLite(Context context) {


        super(context, DATABASE_NAME, null, version);
        kayitlar=context.getSharedPreferences("com.dhome.crazywinner.appdeneme",Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY,task_name TEXT,task_action TEXT,task_yollar TEXT,task_degerler TEXT,task_active INTEGER,task_solid INTEGER,task_types TEXT,task_image TEXT" + ")";
        Log.d("KernelTaskerDB", "SQL : " + sql);

        db.execSQL(sql);

    }
    boolean deleteTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean slm= db.delete(TABLE_NAME, "id" + "=" + id, null) > 0;
        db.close();
        kayitlar.edit().putBoolean("refreshProfiles",true).apply();
        return slm;
    }
    public void upgradeTask(int id,kernelTask task){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " SET " + "task_name" + " = '" + task.getAd() + "' WHERE " + "id" + " = " + id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_action"+ " = '"+task.getAction()+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_yollar"+ " = '"+task.getYollar()+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_degerler"+ " = '"+task.getDegerler()+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_active"+ " = '"+(task.isActive()?1:0)+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_solid"+ " = '"+(task.isSolid()?1:0)+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_types"+ " = '"+task.getTypes()+"' WHERE "+"id"+ " = "+id);
        db.execSQL("UPDATE "+TABLE_NAME +" SET " + "task_image"+ " = '"+task.getImagePath()+"' WHERE "+"id"+ " = "+id);
        db.close();
        kayitlar.edit().putBoolean("refreshProfiles",true).apply();


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        JSONArray json=new JSONArray();

        if(oldVersion<2){
            db.execSQL("ALTER TABLE "
                    + TABLE_NAME + " ADD COLUMN " + "task_solid" + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE "
                    + TABLE_NAME + " ADD COLUMN " + "task_types" + " TEXT DEFAULT "+"'"+json.toString()+"'");
            db.execSQL("ALTER TABLE "
                    + TABLE_NAME + " ADD COLUMN " + "task_image" + " TEXT DEFAULT "+"''");
        }
        if(oldVersion<3){
            db.execSQL("ALTER TABLE "
                    + TABLE_NAME + " ADD COLUMN " + "task_image" + " TEXT DEFAULT "+"''");
        }
    }
    int insertTask(kernelTask task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("task_name", task.getAd());
        values.put("task_action",task.getAction());
        values.put("task_yollar",task.getYollar().toString());
        values.put("task_degerler",task.getDegerler().toString());
        values.put("task_active",task.isActive());
        values.put("task_solid",task.isSolid());
        values.put("task_types",task.getTypes().toString());
        values.put("task_image",task.getImagePath());
        db.insert(TABLE_NAME, null, values);
        db.close();
        List<kernelTask> taskss=getAllTasks();
        kayitlar.edit().putBoolean("refreshProfiles",true).apply();
        return taskss.get(taskss.size()-1).id;
    }
    List<kernelTask> getAllTasks() {
        List<kernelTask> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        // String sqlQuery = "SELECT  * FROM " + TABLE_COUNTRIES;
        // Cursor cursor = db.rawQuery(sqlQuery, null);

        Cursor cursor = db.query(TABLE_NAME, new String[]{"id", "task_name", "task_action","task_yollar","task_degerler","task_active","task_solid","task_types","task_image"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            kernelTask task = new kernelTask();
           task.id=cursor.getInt(0);
            task.setAd(cursor.getString(1));
            task.setAction(cursor.getString(2));
            task.setYollar(cursor.getString(3));
            task.setDegerler(cursor.getString(4));
            task.setActive(cursor.getInt(5));
            task.setSolid(cursor.getInt(6));
            task.setTypes(cursor.getString(7));
            task.setImage(cursor.getString(8));
            tasks.add(task);
        }

          cursor.close();

        return tasks;
    }
    public kernelTask getTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "task_name", "task_action","task_yollar","task_degerler","task_active","task_solid","task_types" ,"task_image"},
                "id" + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();

            kernelTask task = new kernelTask();

            if(cursor.getCount()>0){
            task.id=cursor.getInt(0);
            task.setAd(cursor.getString(1));
            task.setAction(cursor.getString(2));
            task.setYollar(cursor.getString(3));
            task.setDegerler(cursor.getString(4));
            task.setActive(cursor.getInt(5));
                task.setSolid(cursor.getInt(6));
                task.setTypes(cursor.getString(7));
                task.setImage(cursor.getString(8));
                cursor.close();
            return task;}
            cursor.close();
        }else{

        // String sqlQuery = "SELECT  * FROM " + TABLE_COUNTRIES;
        // Cursor cursor = db.rawQuery(sqlQuery, null);


        kernelTask donecek=new kernelTask("","",new JSONArray().toString(),new JSONArray().toString(),0,0);
        donecek.id=id;
        return donecek;}
        return null;

    }
}
