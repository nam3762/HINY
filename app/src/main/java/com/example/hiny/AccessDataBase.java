package com.example.hiny;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;

import java.util.ArrayList;

public class AccessDataBase{

    private static final ArrayList<convData> convDataSet = new ArrayList<>();
    private static int maxIndex = 0;
    private static class convData{
        private int id;
        private String dou;
        private String name;
        private String tel;
        private String address;
        private int zip_code;
        private double x_coord;
        private double y_coord;
        private double lat;
        private double lng;
    }
    private Context mcontext;
    public AccessDataBase(Context context){
        Log.d("AccessDataBase 생성자", "AccessDataBase: "+context);
        mcontext=context;
    }
    public static int getMaxIndex(){

        return maxIndex;
    }

    public int getId(int index){

        return convDataSet.get(index+1).id;
    }

    public String getDou(int index){
        return convDataSet.get(index+1).dou;

    }

    public static double getLat(int index){
        Log.d("DataBase GetX Cord", "getXCoord: "+index+"  \t ");
        return convDataSet.get(index+1).lat;
    }

    public static double getLng(int index){

        return convDataSet.get(index+1).lng;
    }

    public void loadDataBase() {
        Log.d("AccessDB Load DataBase", "loadDataBase: ");
        DataBaseHelper dbHelper = new DataBaseHelper(mcontext);
        DataBaseHelper dbLocate = new DataBaseHelper(mcontext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteDatabase dbLoc = dbLocate.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM em_store_final", null);
        Cursor latlng = dbLoc.rawQuery("SELECT * FROM em_store_latlng", null);
        //" and name = ?",new String[]{"홍길동"});
        while (cursor.moveToNext() && latlng.moveToNext()) {
            //val += cursor.getString(2) + ", ";
            //System.out.println("------------------------");

            convData Data = new convData();
            Data.id = cursor.getInt(0);
            Data.dou = cursor.getString(1);
            Data.name = cursor.getString(2);
            Data.tel = cursor.getString(3);
            Data.address = cursor.getString(4);
            Data.zip_code = cursor.getInt(5);
            Data.x_coord = cursor.getDouble(6);
            Data.y_coord = cursor.getDouble(7);
            Data.lat = latlng.getDouble(3);
            Data.lng = latlng.getDouble(4);


            convDataSet.add(Data);
            maxIndex += 1;
        }
        latlng.close();
        cursor.close();

        dbLocate.close();
        dbHelper.close();
    }
}
