package com.example.busalarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import org.xmlpull.v1.XmlPullParser;

import org.xmlpull.v1.XmlPullParserException;

import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {
    private ImageButton kakao_login_btn;
    private Button login_button; // 로그인 버튼
    private Button signup_button; // 회원가입 버튼 추가
    private EditText login_id, login_pw;
    private FirebaseAuth mAuth; // FirebaseAuth 추가
    private NetworkThread thread;
    private Button bus_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thread=new NetworkThread();
        thread.start();

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();

        // Kakao SDK 초기화
        KakaoSdk.init(this, "3f9d0ec283a8053661dec680ebb42802"); // Kakao App Key로 교체

        kakao_login_btn = findViewById(R.id.kakao_btn);
        login_button = findViewById(R.id.btn); // 로그인 버튼 초기화
        signup_button = findViewById(R.id.signup_btn); // 회원가입 버튼 초기화
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);

        if (kakao_login_btn == null) {
            Log.e("MainActivity", "버튼 초기화 오류: 버튼이 null입니다.");
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        kakao_login_btn.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
            }
        });

        // 로그인 버튼 클릭 리스너 설정
        login_button.setOnClickListener(v -> {
            // 이메일과 비밀번호로 로그인 시도
            String email = login_id.getText().toString();
            String password = login_pw.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공 시 NextActivity로 이동
                                    Intent intent = new Intent(MainActivity.this, NextActivity.class);
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 종료
                                } else {
                                    // 로그인 실패 시 메시지 표시
                                         String errorMessage;
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthException e) {
                                        if (e.getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                                            errorMessage = "없는 계정입니다.";
                                        } else {
                                            errorMessage = "로그인 실패: " + e.getMessage();
                                        }
                                    } catch (Exception e) {
                                        errorMessage = "로그인 실패: " + e.getMessage();
                                    }
                                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 버튼 클릭 리스너 설정
        signup_button.setOnClickListener(v -> {
            // 회원가입 버튼 클릭 시 SignUpActivity로 이동
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        bus_btn=findViewById(R.id.busBtn);
        bus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bus click");
                Intent intent = new Intent(MainActivity.this, ApiExplorer.class);
                startActivity(intent);
            }
        });
    }


    private void updateKaIKaoLoginUi() {
        UserApiClient.getInstance().me((user, throwable) -> {

            if (throwable != null) {
                Log.e("MainActivity", "사용자 정보 가져오기 실패: " + throwable.getMessage());
            }
            if (user != null) {
                // 로그인 성공 시 NextActivity로 이동
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