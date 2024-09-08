package com.example.busalarm;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;


public class BusActivity  extends AppCompatActivity {
    private NetworkThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bus);

        setOnApplyWindowInsetsListener(findViewById(R.id.bus), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        thread=new NetworkThread();
//        thread.start();

        AssetManager am = getResources().getAssets();
        InputStream is = null;
        ArrayList<BusLocation> locationList = new ArrayList<>();

        try {
            // XML 파일 스트림 열기
            is = am.open("bus_location.xml");
            if(is==null){
                Log.e("eroror","is");
            }
            // XML 파서 초기화
            XmlPullParserFactory  xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();

            // XML 파서에 파일 스트림 지정.
            parser.setInput(is, "UTF-8") ;

//            is = url.openStream();
//            parser.setInput(new InputStreamReader(is, "UTF-8"));
            String tagName = "";

            //event type얻어오기
            int eventType = parser.getEventType();

            //xml태그값들을 담을 객체 클래스 선언
            BusLocation location=null;
            //클래스들을 담을 배열선언

            //xml문서의 끝까지 읽기
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //태그가 시작
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if (parser.getName().equals("busLocationList")) {
                            //객체 생성
                            location=new BusLocation();
                        }
                        break;

                    //태그 안의 텍스트
                    case XmlPullParser.TEXT:

                        switch (tagName) {
                            case "endBus": {
                                location.endBus = parser.getText();
                                break;
                            }
                            case "lowPlate": {
                                location.lowPlate = parser.getText();
                                Log.e("location.lowPlate",location.lowPlate);

                                break;
                            }
                            case "plateNo": {
                                location.plateNo = parser.getText();
                                Log.e("location.plateNo",location.plateNo);

                                break;
                            }
                            case "plateType": {
                                location.plateType = parser.getText();
                                break;
                            }
                            case "remainSeatCnt": {
                                location.remainSeatCnt = parser.getText();
                                break;
                            }
                            case "routeId": {
                                location.routeId = parser.getText();
                                break;
                            }
                            case "stationId": {
                                location.stationId = parser.getText();
                                break;
                            }
                            case "stationSeq": {
                                location.stationSeq = parser.getText();
                                break;
                            }
                        }
                        break;

                    //태그의 끝
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("busLocationList")) {
                            //객체를 리스트에 추가
                            locationList.add(location);

                        }
                        break;
                }
                //다음으로 이동
                eventType = parser.next();
            }//while문 종료


        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //리사이클러뷰 방식으로 화면에 보여주기
//        RecyclerView recyclerView;
//        recyclerView = (RecyclerView) findViewById(R.id.listView);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        MyAdapter2 adapter = new MyAdapter2(getApplicationContext(), locationList);
//        recyclerView.setAdapter(adapter);


        //기본 리스트 뷰 방식으로 데이터 화면에 보여주기
//        ListView listView = (ListView)findViewById(R.id.listView);
//        final MyAdapter myAdapter = new MyAdapter(this,locationList);
//        Log.e("locationList",    myAdapter.getItem(0).getPlateNo());
//        Log.e("locationList",    myAdapter.getItem(1).getPlateNo());
//        listView.setAdapter(myAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                Toast.makeText(getApplicationContext(),
//                       position+"번쩨",
////                        myAdapter.getItem(position).getPlateNo(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });

    }//oncreate


}

