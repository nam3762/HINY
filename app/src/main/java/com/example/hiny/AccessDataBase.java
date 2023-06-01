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
    private static final ArrayList<medData> medDataSet = new ArrayList<>();
    private static int medMaxIndex = 0;
    private static class medData{
        private int id;
        private String symptoms;
        private String age;
        private String first_option;
        private String second_option;
        private String medicine;
        private String dosage;
        private String needToKnowBeforeUse;
        private String precaution;
        private String foodsToBeAwareOf;
        private String storageMethod;
    }

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
        mcontext=context;
    }

    //convData
    public static int getMaxIndex(){

        return maxIndex;
    }

    public int getId(int index){
        return convDataSet.get(index+1).id;
    }

    public String getDou(int index){
        return convDataSet.get(index+1).dou;
    }

    public String getName(int index){
        return convDataSet.get(index+1).name;
    }

    public static double getLat(int index){
        Log.d("DataBase GetX Cord", "getXCoord: "+index+"  \t ");
        return convDataSet.get(index+1).lat;
    }

    public static double getLng(int index){
        return convDataSet.get(index+1).lng;
    }


    //medData
//    public int getMedID(int index){
//        return medDataSet.get(index+1).id;
//    }

    public String getSymptoms(int index){
        return medDataSet.get(index+1).symptoms;
    }

    public String getAge(int index){
        return medDataSet.get(index+1).age;
    }

    public String getFirstOption(int index){
        return medDataSet.get(index+1).first_option;
    }

    public String getSecondOption(int index){
        return medDataSet.get(index+1).second_option;
    }

    public String getMedicine(int index){
        return medDataSet.get(index+1).medicine;
    }
    public String getDosage(int index){
        return medDataSet.get(index+1).dosage;
    }

    public String getNeedToKnowBeforeUse(int index){
        return medDataSet.get(index+1).needToKnowBeforeUse;
    }

    public String getPrecaution(int index){
        return medDataSet.get(index+1).precaution;
    }

    public String getFoodsToBeAwareOf(int index){
        return medDataSet.get(index+1).foodsToBeAwareOf;
    }

    public String getStorageMethod(int index){
        return medDataSet.get(index+1).storageMethod;
    }



    public void loadDataBase() {
        DataBaseHelper dbConv = new DataBaseHelper(mcontext);

        SQLiteDatabase db = dbConv.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM em_store_final", null);

        //" and name = ?",new String[]{"홍길동"});
        while (cursor.moveToNext()) {
            //val += cursor.getString(2) + ", ";

            convData Data = new convData();
            Data.id = cursor.getInt(0);
            Data.dou = cursor.getString(1);
            Data.name = cursor.getString(2);
            Data.tel = cursor.getString(3);
            Data.address = cursor.getString(4);
            Data.zip_code = cursor.getInt(5);
            Data.x_coord = cursor.getDouble(6);
            Data.y_coord = cursor.getDouble(7);
            Data.lat = cursor.getDouble(8);
            Data.lng = cursor.getDouble(9);


            convDataSet.add(Data);
            maxIndex += 1;
        }
        cursor.close();
        dbConv.close();

        DataBaseHelper dbLocate = new DataBaseHelper(mcontext);
        SQLiteDatabase dbLoc = dbLocate.getReadableDatabase();
        Cursor medDataCursor = dbLoc.rawQuery("SELECT * FROM question_list", null);
        while (medDataCursor.moveToNext()){
            medData medData = new medData();
            medData.symptoms = medDataCursor.getString(0);
            medData.age = medDataCursor.getString(1);
            medData.first_option = medDataCursor.getString(2);
            medData.second_option = medDataCursor.getString(3);
            medData.medicine = medDataCursor.getString(4);
            medData.dosage = medDataCursor.getString(5);
            medData.needToKnowBeforeUse = medDataCursor.getString(6);
            medData.precaution = medDataCursor.getString(7);
            medData.foodsToBeAwareOf = medDataCursor.getString(8);
            medData.storageMethod = medDataCursor.getString(9);
        }

        medDataCursor.close();

        dbLocate.close();
    }
}
