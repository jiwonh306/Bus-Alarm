package com.example.busalarm;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.KakaoMapSdk;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.sdk.common.KakaoSdk;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

public class NextActivity extends AppCompatActivity {
    MapView mapView;
    KakaoMap kakaoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        KakaoSdk.init(this, "3f9d0ec283a8053661dec680ebb42802");

        KakaoMapSdk.init(this, "3f9d0ec283a8053661dec680ebb42802");

        mapView = findViewById(R.id.map_view);
        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy: ");
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "onMapError: ", error);
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull KakaoMap map) {
                // 정상적으로 인증이 완료되었을 때 호출
                // KakaoMap 객체를 얻어 옵니다.
                kakaoMap = map;
            }
        });
    }

}