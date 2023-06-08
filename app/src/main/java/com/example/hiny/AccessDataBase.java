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

    public static final ArrayList<convData> convDataSet = new ArrayList<>();
    public static final ArrayList<medData> medDataSet = new ArrayList<>();
    private static int medMaxIndex = 0;
    public static class medData{
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
    private final Context mcontext;
//    public AccessDataBase(Context context){
//        mcontext=context;
//    }
    public AccessDataBase(Context context){
        mcontext=context;
    }

    //convData
    public static int getMaxIndex(){

        return maxIndex;
    }

    public int getId(int index){
        return convDataSet.get(index).id;
    }

    public static String getDou(int index){
        return convDataSet.get(index).dou;
    }

    public static String getName(int index){
        return convDataSet.get(index).name;
    }

    public static String getAddress(int index){
        return convDataSet.get(index).address;
    }

    public static String getTel(int index){
        return convDataSet.get(index).tel;
    }

    public static double getLat(int index){
        Log.d("DataBase GetX Cord", "getXCoord: "+index+"  \t ");
        return convDataSet.get(index).lat;
    }

    public static double getLng(int index){
        return convDataSet.get(index).lng;
    }


//    //medData
    public static int getMedMaxIndex(){
        return medMaxIndex;
    }
//    public int getMedID(int index){
//        return medDataSet.get(index+1).id;
//    }

    public static String getSymptoms(int index){
        return medDataSet.get(index).symptoms;
    }

    public static String getAge(int index){
        return medDataSet.get(index).age;
    }

    public static String getFirstOption(int index){
        return medDataSet.get(index).first_option;
    }

    public static String getSecondOption(int index){
        return medDataSet.get(index).second_option;
    }

    public static String getMedicine(int index){
        return medDataSet.get(index).medicine;
    }
    public static String getDosage(int index){
        return medDataSet.get(index).dosage;
    }

    public static String getNeedToKnowBeforeUse(int index){
        return medDataSet.get(index).needToKnowBeforeUse;
    }

    public static String getPrecaution(int index){
        return medDataSet.get(index).precaution;
    }

    public static String getFoodsToBeAwareOf(int index){
        return medDataSet.get(index).foodsToBeAwareOf;
    }

    public static String getStorageMethod(int index){
        return medDataSet.get(index).storageMethod;
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
            medDataSet.add(medData);
            medMaxIndex += 1;
        }

        medDataCursor.close();

        dbLocate.close();
    }
}
