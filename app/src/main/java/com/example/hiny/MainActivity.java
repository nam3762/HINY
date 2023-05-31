package com.example.hiny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    private NaverMap naverMap;
    private ImageButton checkbtn;
    private FusedLocationSource locationSource;
    private Marker marker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public AccessDataBase acDB  = new AccessDataBase(this);
    private boolean start = true;

    private Double selflat, selflon, distance;
    public LatLng currentLocation;


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
        checkbtn = (ImageButton) findViewById(R.id.check);
        acDB.loadDataBase();


        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CheckActivity.class);
                intent.putExtra("data","Test Popup");
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);


        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                selflat=location.getLatitude();
                selflon=location.getLongitude();

                currentLocation = new LatLng(selflat,selflon);
//
//                if(start){
//                    loadMarker();
//                    start = false;
//                }
            }

        });



    }


    public void loadMarker() {
        Log.d("Main Activity", "loadMarker: ");
//        for(int i=0; i< AccessDataBase.getMaxIndex()-1; i++){
//            LatLng pos = new LatLng(AccessDataBase.getLat(i), AccessDataBase.getLng(i));
//            distance = currentLocation.distanceTo(pos);
//            if (distance<=2000){
//                addMarker(AccessDataBase.getLat(i), AccessDataBase.getLng(i));
//            }
//            System.out.println(AccessDataBase.getLat(i) + ", "+ AccessDataBase.getLng(i) + "\n");
//        }
        for(int i=0; i< AccessDataBase.getMaxIndex()-1; i++){
            if (getDistance(AccessDataBase.getLat(i), AccessDataBase.getLng(i), selflat, selflon) < 4.0) {
                Log.d("addMarker", "addMarker");
                addMarker(AccessDataBase.getLat(i), AccessDataBase.getLng(i));
            }
            System.out.println(AccessDataBase.getLat(i) + ", "+ AccessDataBase.getLng(i) + "\n");
            System.out.println("ff" + getDistance(AccessDataBase.getLat(i), AccessDataBase.getLng(i), selflat, selflon));
        }

        System.out.println("eeeeeeeee" + selflat);
        //" and name = ?",new String[]{"홍길동"});
//        while (cursor.moveToNext()) {
//
//            AccessDataBase.getXCoord()
//            //val += cursor.getString(2) + ", ";
//            //System.out.println("------------------------");
//            System.out.println(cursor.getDouble(6) + ", " + cursor.getDouble(7));
//            Tm128 utmk = new Tm128(cursor.getDouble(6), cursor.getDouble(7));
//            LatLng latlng = utmk.toLatLng();
////            LatLng latlng = new LatLng(cursor.getDouble(6), cursor.getDouble(7));
//            //System.out.println(latlng.latitude + ", " + latlng.longitude);
//            addMarker(latlng.latitude + 1.0211492839123241822559878276595, latlng.longitude + 0.98207200616958801313686771346171);
//            //addMarker(latlng.latitude, latlng.longitude);
//        }
    }


    private static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double distance;
        double radian = Math.PI / 180;
        double radius = 6371.0;     //지구 반지름

        double deltaLat = Math.abs(lat1 - lat2) * radian;
        double deltaLng = Math.abs(lng1 - lng2) * radian;

        double sinDeltaLat = Math.sin(deltaLat / 2);
        double sinDeltaLng = Math.sin(deltaLng / 2);
        double squareRoot = Math.sqrt(sinDeltaLat * sinDeltaLat +
                Math.cos(lat1 * radian) * Math.cos(lat2 * radian) * sinDeltaLng * sinDeltaLng);
        distance = 2 * radius * Math.asin(squareRoot);
        return distance;

    }

    private void addMarker(double latitude, double longitude) {
        // 기존 마커가 있을 경우 제거

        // 새로운 마커 생성
        marker=new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);


        // 마커가 추가된 위치로 카메라 이동
//        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latitude, longitude));
//        naverMap.moveCamera(cameraUpdate);
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