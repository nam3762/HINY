package com.example.hiny;

import static com.gun0912.tedpermission.provider.TedPermissionProvider.context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.ImageButton;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.Projection;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    private NaverMap naverMap;
    private ImageButton checkbtn,homebtn;
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

    private HashMap<Marker, Integer> markerData = new HashMap<Marker, Integer>();
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
        homebtn = (ImageButton) findViewById(R.id.home);
        acDB.loadDataBase();
        //AccessDataBase.loadDataBase();
            //loadMarker();
        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CheckActivity.class);
                intent.putExtra("data","Test Popup");
                startActivityForResult(intent,1);
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = true;
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

                if(start){
                    loadMarker();
                    start = false;
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(currentLocation);
                    naverMap.moveCamera(cameraUpdate);
                }

            }
        });

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 화면을 눌렀을 때 처리할 코드
//                break;
//            case MotionEvent.ACTION_MOVE:
//                // 손가락을 움직일 때 처리할 코드
//                break;
//            case MotionEvent.ACTION_UP:
//                // 손가락을 화면에서 떼었을 때 처리할 코드
//                break;
//        }
//        return true;
//    }



    public void loadMarker() {
        for(Marker mark : markerData.keySet() ){
            Log.d("marker", mark.getPosition().toString());
            mark.setMap(null);
        }

//        WindowMetrics window = this.getWindowManager().getCurrentWindowMetrics();
//        window.getBounds().width();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int middleWidth = displayMetrics.widthPixels/2;
        final int middleHeight = displayMetrics.heightPixels/2;


        Projection projection = naverMap.getProjection();
        LatLng pos = projection.fromScreenLocation(new PointF(middleWidth, middleHeight));

        for(int i=0; i< AccessDataBase.getMaxIndex(); i++){
            //if (getDistance(AccessDataBase.getLat(i), AccessDataBase.getLng(i), selflat, selflon) < 4.0) {
            if (getDistance(AccessDataBase.getLat(i), AccessDataBase.getLng(i), selflat, selflon) < 3.0) {
                markerData.put(addMarker(AccessDataBase.getLat(i), AccessDataBase.getLng(i)), i);
            }
            System.out.println(AccessDataBase.getLat(i) + ", "+ AccessDataBase.getLng(i) + "\n");
        }

        AtomicReference<String> informationWindow = new AtomicReference<>("test");
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(context) {


            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {

                return informationWindow.get();
            }
        });


        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker)overlay;
            int dataID = markerData.get(marker);
            informationWindow.set(AccessDataBase.getName(dataID) + "\n"
            + "주소: " + AccessDataBase.getAddress(dataID) + "\n"
            + "전화번호: " + AccessDataBase.getTel(dataID));


            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }

            return true;
        };

        for(Marker mark : markerData.keySet() ){
            Log.d("marker", mark.getPosition().toString());
            mark.setOnClickListener(listener);
        }
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });
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

    private Marker addMarker(double latitude, double longitude) {
        marker=new Marker();
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);

        return marker;
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