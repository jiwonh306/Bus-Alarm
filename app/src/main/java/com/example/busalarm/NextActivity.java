package com.example.busalarm;

import androidx.appcompat.app.AppCompatActivity;

//string 값부터 다양한 parcelable type까지 mapping 해줌
// parcelable type은 기본 type(int, long,string)이 아닌 class
// 객체를 acitivity간에 주고 받기 위한 type
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.kakao.vectormap.MapView;


public class MainActivity extends AppCompatActivity {

    MapView mapView1;
    RelativeLayout mapViewContainer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView1 = new MapView(this);


        mapViewContainer1 = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer1.addView(mapView1);

        /*지도 중심점,레벨(zoom) 변경*/

        // 중심점 변경 - 세종대학교
        mapView1.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5518018, 127.0736343), true);

        // 줌 레벨 변경
        mapView1.setZoomLevel(1, true);

        // 줌 인
        mapView1.zoomIn(true);

        // 줌 아웃
        mapView1.zoomOut(true);

        /*마커 추가*/

        //마커 찍기 (세종대학교)
        MapPoint MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(37.5518018, 127.0736343);


        // 마커 아이콘 추가하는 함수
        MapPOIItem marker1 = new MapPOIItem();


        // 클릭 했을 때 나오는 호출 값
        marker1.setItemName("세종대 쓰레기통 여기 있음!");

        // 왜 있는지 잘 모르겠음
        marker1.setTag(0);

        // 좌표를 입력받아 현 위치로 출력
        marker1.setMapPoint(MARKER_POINT1);

        //  (클릭 전)기본으로 제공하는 BluePin 마커 모양의 색.
        marker1.setMarkerType(MapPOIItem.MarkerType.BluePin);


        // (클릭 후) 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker1.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);


        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView1.addPOIItem(marker1);


    }
}