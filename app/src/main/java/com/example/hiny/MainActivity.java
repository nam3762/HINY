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
import android.view.View;
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
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    private NaverMap naverMap;
    private ImageButton checkbtn,homebtn,hosbtn,drugbtn;
    private FusedLocationSource locationSource;
    private Marker marker,marker2,marker3;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public AccessDataBase acDB  = new AccessDataBase(this);
    private boolean start = true;
    private boolean start2 = false;
    private boolean start3 = false;
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
        drugbtn = (ImageButton) findViewById(R.id.drug);
        hosbtn = (ImageButton) findViewById(R.id.hospital);

        acDB.loadDataBase();
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
            public void onClick(View view) {start = true;}
        });

        drugbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start2 = true;
            }
        });

        hosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start3 = true;
            }
        });


    }

    /* ***********************
     * 함수명 : onMapReady
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 네이버 지도 api를 준비하는 함수
     * ************************/
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
                    loadMarker(1);
                    start = false;
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(currentLocation);
                    naverMap.moveCamera(cameraUpdate);
                }

                if(start2){
                    loadMarker(2);
                    start2 = false;
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(currentLocation);
                    naverMap.moveCamera(cameraUpdate);
                }

                if(start3){
                    loadMarker(3);
                    start3 = false;
                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(currentLocation);
                    naverMap.moveCamera(cameraUpdate);
                }

            }
        });

    }


    /* ***********************
     * 함수명 : onTouchEvent
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 터치이벤트 관리하는 함수
     * ************************/
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


    /* ***********************
     * 함수명 : loadMarker
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 네이버지도의 마커 오버레이를 로딩하는 함수
     * ************************/
    public void loadMarker(int def) {
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
        if (def == 1){
            for(int i=0; i< AccessDataBase.getMaxIndex(); i++){
                if (getDistance(AccessDataBase.getLat(i), AccessDataBase.getLng(i), selflat, selflon) < 3.0) {
                    markerData.put(addMarker(AccessDataBase.getLat(i), AccessDataBase.getLng(i)), i);
                }
                System.out.println(AccessDataBase.getLat(i) + ", "+ AccessDataBase.getLng(i) + "\n");
            }
        }
        if (def == 2){
            for(int i=0; i< AccessDataBase.getPharmacyIndex(); i++){
                if (getDistance(AccessDataBase.getPharmacyLat(i), AccessDataBase.getPharmacyLng(i), selflat, selflon) < 3.0) {
                    markerData.put(addMarker2(AccessDataBase.getPharmacyLat(i), AccessDataBase.getPharmacyLng(i)), i);
                }
                System.out.println(AccessDataBase.getPharmacyLat(i) + ", "+ AccessDataBase.getPharmacyLng(i) + "\n");
            }
        }
        if (def == 3){
            for(int i=0; i< AccessDataBase.getHospitalIndex(); i++){
                if (getDistance(AccessDataBase.getHospitalLat(i), AccessDataBase.getHospitalLng(i), selflat, selflon) < 3.0) {
                    markerData.put(addMarker3(AccessDataBase.getHospitalLat(i), AccessDataBase.getHospitalLng(i)), i);
                }
                System.out.println(AccessDataBase.getHospitalLat(i) + ", "+ AccessDataBase.getHospitalLng(i) + "\n");
            }
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

            if (def == 1) {
                informationWindow.set(AccessDataBase.getName(dataID) + "\n"
                        + "주소: " + AccessDataBase.getAddress(dataID) + "\n"
                        + "전화번호: " + AccessDataBase.getTel(dataID));
            }if(def == 2){
                informationWindow.set(AccessDataBase.getPharmacyName(dataID) + "\n"
                        + "주소: " + AccessDataBase.getPharmacyAddress(dataID) + "\n"
                        + "전화번호: " + AccessDataBase.getPharmacyTel(dataID));
            }if(def == 3){
                informationWindow.set(AccessDataBase.getHospitalName(dataID) + "\n"
                        + "주소: " + AccessDataBase.getHospitalAddress(dataID) + "\n"
                        + "전화번호: " + AccessDataBase.getHospitalTel(dataID));
            }

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

    /* ***********************
     * 함수명 : getDistance
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 네이버지도의 오버레이를 표시할 때 사용할 두 위도, 경도의
     *       사이의 거리를 구하는 함수
     * ************************/
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
    /* ***********************
     * 함수명 : addMarker
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 네이버지도의 오버레이를 표시할 때 사용할 두 위도, 경도의
     *       사이의 거리를 구하는 함수
     * ************************/
    private Marker addMarker(double latitude, double longitude) {

        // 새로운 마커 생성
        marker=new Marker();
        marker.setIcon(OverlayImage.fromResource(R.drawable.sangbi));
        marker.setWidth(60);
        marker.setHeight(60);
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setMap(naverMap);

        return marker;
    }

    private Marker addMarker2(double latitude, double longitude){
        marker2=new Marker();
        marker2.setIcon(OverlayImage.fromResource(R.drawable.drug));
        marker2.setWidth(60);
        marker2.setHeight(60);
        marker2.setPosition(new LatLng(latitude, longitude));
        marker2.setMap(naverMap);

        return marker2;
    }

    private Marker addMarker3(double latitude, double longitude){
        marker3=new Marker();
        marker3.setIcon(OverlayImage.fromResource(R.drawable.hospital_icon));
        marker3.setWidth(60);
        marker3.setHeight(60);
        marker3.setPosition(new LatLng(latitude, longitude));
        marker3.setMap(naverMap);

        return marker3;
    }
    /* ***********************
     * 함수명 : onRequestPermissionsResult
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 네이버지도의 현재위치에 조회 권한관련 함수
     * ************************/
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