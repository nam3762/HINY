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
    public static final ArrayList<hospitalData> hospitalDataSet = new ArrayList<>();
    public static final ArrayList<pharmacyData> pharmacyDataSet = new ArrayList<>();
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
        private int price;
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

    private static int hospitalIndex = 0;
    private static class hospitalData{
        private int id;
        private String hospitalName;
        private int hospitalZipcode;
        private String hospitalTel;
        private String hospitalAddress;
        private double hospital_lat;
        private double hospital_lng;
        private String hospital_homepage;

    }
    private static int pharmacyIndex = 0;
    private static class pharmacyData{
        private int id;
        private String pharmacyName;
        private int pharmacyZipcode;
        private String pharmacyAddress;
        private String pharmacyTel;
        private double pharmacy_lat;
        private double pharmacy_lng;

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
        //Log.d("DataBase GetX Cord", "getXCoord: "+index+"  \t ");
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
    
    public static int getPrice(int index){
        return medDataSet.get(index).price;
    }

    //hospital_db
    public static int getHospitalIndex(){
        return hospitalIndex;
    }

    public static String getHospitalName(int index){
        return hospitalDataSet.get(index).hospitalName;
    }

    public static int getHospitalZipcode(int index){
        return hospitalDataSet.get(index).hospitalZipcode;
    }

    public static String getHospitalAddress(int index){
        return hospitalDataSet.get(index).hospitalAddress;
    }

    public static double getHospitalLat(int index){
        return hospitalDataSet.get(index).hospital_lat;
    }

    public static double getHospitalLng(int index){
        return hospitalDataSet.get(index).hospital_lng;
    }

    public static String getHospitalHomepage(int index){
        return hospitalDataSet.get(index).hospital_homepage;
    }

    //pharmacy Data
    public static int getPharmacyIndex(){
        return pharmacyIndex;
    }

    public static String getPharmacyName(int index){
        return pharmacyDataSet.get(index).pharmacyName;
    }

    public static int getPharmacyZipcode(int index){
        return pharmacyDataSet.get(index).pharmacyZipcode;
    }

    public static String getPharmacyAddress(int index){
        return pharmacyDataSet.get(index).pharmacyAddress;
    }

    public static String getPharmacyTel(int index){
        return pharmacyDataSet.get(index).pharmacyTel;
    }

    public static double getPharmacyLat(int index){
        return pharmacyDataSet.get(index).pharmacy_lat;
    }

    public static double getPharmacyLng(int index){
        return pharmacyDataSet.get(index).pharmacy_lng;
    }


    public void loadDataBase() {
        DataBaseHelper dbConv = new DataBaseHelper(mcontext);
        SQLiteDatabase db = dbConv.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM em_store_final", null);

        while (cursor.moveToNext()) {
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
            medData.price = medDataCursor.getInt(10);
            medDataSet.add(medData);
            medMaxIndex += 1;
        }

        medDataCursor.close();
        dbLocate.close();


        DataBaseHelper dbHospital = new DataBaseHelper(mcontext);
        SQLiteDatabase dbHospitalPos = dbHospital.getReadableDatabase();
        Cursor hospitalCursor = dbHospitalPos.rawQuery("SELECT * FROM hospital_db", null);
        while (hospitalCursor.moveToNext()){
            hospitalData hospitalData = new hospitalData();
            hospitalData.hospitalName = hospitalCursor.getString(0);
            hospitalData.hospitalZipcode = hospitalCursor.getInt(1);
            hospitalData.hospitalAddress = hospitalCursor.getString(2);
            hospitalData.hospital_lat = hospitalCursor.getDouble(3);
            hospitalData.hospital_lng = hospitalCursor.getDouble(4);
            hospitalData.hospital_homepage = hospitalCursor.getString(5);
            hospitalDataSet.add(hospitalData);
            hospitalIndex += 1;
        }

        hospitalCursor.close();
        dbHospital.close();



        DataBaseHelper dbPharmacy = new DataBaseHelper(mcontext);
        SQLiteDatabase dbPharmacyData = dbPharmacy.getReadableDatabase();
        Cursor pharmacyCursor = dbPharmacyData.rawQuery("SELECT * FROM pharmacy_db", null);
        while (pharmacyCursor.moveToNext()){
            pharmacyData pharmacyData = new pharmacyData();
            pharmacyData.pharmacyName = pharmacyCursor.getString(0);
            pharmacyData.pharmacyZipcode = pharmacyCursor.getInt(1);
            pharmacyData.pharmacyAddress = pharmacyCursor.getString(2);
            pharmacyData.pharmacyTel = pharmacyCursor.getString(3);
            pharmacyData.pharmacy_lat = pharmacyCursor.getDouble(4);
            pharmacyData.pharmacy_lng = pharmacyCursor.getDouble(5);
            pharmacyDataSet.add(pharmacyData);
            pharmacyIndex += 1;
        }

        pharmacyCursor.close();
        dbPharmacy.close();
    }
}
