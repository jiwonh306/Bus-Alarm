package com.example.busalarm;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import com.kakao.sdk.common.util.Utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private ImageButton kakao_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // setContentView 먼저 호출

        kakao_login_btn = findViewById(R.id.kakao_btn);

        if (kakao_login_btn == null) {
            Log.e("MainActivity", "버튼 초기화 오류: 버튼이 null입니다.");
            return;
        }

        setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SDK 초기화 시 올바른 앱 키 사용
        KakaoSdk.init(this, "3f9d0ec283a8053661dec680ebb42802");

        kakao_login_btn.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
            }
        });
    }

    private void updateKaIKaoLoginUi() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                Log.e("MainActivity", "사용자 정보 가져오기 실패: " + throwable.getMessage());
            }
            if (user != null) {
                // 로그인 성공 시 다음 액티비티로 이동
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            } else {
                Log.i("MainActivity", "로그인 안되어있음");
            }
            return null;
        });
    }

    private final Function2<OAuthToken, Throwable, Unit> callback = (oAuthToken, throwable) -> {
        if (throwable != null) {
            Log.e("MainActivity", "로그인 실패: " + throwable.getMessage());
        } else {
            updateKaIKaoLoginUi();
        }
        return null;
    };
}