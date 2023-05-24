package com.example.hiny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Tm128;
import com.naver.maps.geometry.Utmk;
import com.naver.maps.geometry.WebMercatorCoord;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.Projection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback{



    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    List<LatLng> lstLatLng = new ArrayList<>();

    String val = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        getVal();

//        naverMap.setOnMapClickListener((point, coord) -> {
//            addMarker(coord.latitude, coord.longitude);
//        });
        addMarker(127.4890319, 36.6424341);
    }

    public void getVal() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM em_store_final", null);
        System.out.println("------------------ffffffffffffffffffffff-----------");
        //" and name = ?",new String[]{"홍길동"});
        while (cursor.moveToNext()) {
            //val += cursor.getString(2) + ", ";
            //System.out.println("------------------------");
            System.out.println(cursor.getDouble(6) + ", " + cursor.getDouble(7));
            Tm128 utmk = new Tm128(cursor.getDouble(6), cursor.getDouble(7));
            LatLng latlng = utmk.toLatLng();
            //System.out.println(latlng.latitude + ", " + latlng.longitude);
            addMarker(latlng.latitude * 1.0211492839123241822559878276595, latlng.longitude + 0.98207200616958801313686771346171);
        }
        cursor.close();
        dbHelper.close();
    }

    private void addMarker(double latitude, double longitude) {
        // 기존 마커가 있을 경우 제거

        // 새로운 마커 생성
        Marker marker=new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if(!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}