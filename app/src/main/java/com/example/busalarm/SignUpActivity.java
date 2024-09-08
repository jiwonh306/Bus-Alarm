package com.example.busalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();

        // 뒤로 가기 버튼 클릭 리스너 설정
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // MainActivity로 이동
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        });

        // 버튼 클릭 리스너 설정
        findViewById(R.id.check).setOnClickListener(onClickListener);
    }

    // 버튼 클릭 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.check) {
                signUp();
            }
        }
    };

    // 회원가입 메소드
    private void signUp() {
        // EditText에서 입력값 가져오기
        String id = ((EditText) findViewById(R.id.user_id)).getText().toString();
        String password = ((EditText) findViewById(R.id.user_password)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.user_password_check)).getText().toString();

        // 입력값 검증
        if (id.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                // Firebase Auth를 사용한 회원가입
                mAuth.createUserWithEmailAndPassword(id, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공 시 메시지 표시
                                    Toast.makeText(SignUpActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    // MainActivity로 이동
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // 현재 액티비티 종료
                                } else {
                                    // 회원가입 실패 시 처리
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        // 이미 가입된 계정인 경우
                                        Toast.makeText(SignUpActivity.this, "이미 가입된 계정입니다.", Toast.LENGTH_SHORT).show();
                                        // 이메일로 로그인 시도
                                        signInExistingUser(id, password);
                                    } else {
                                        // 일반적인 회원가입 실패 처리
                                        Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else {
                // 비밀번호 불일치 시 메시지 표시
                Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 입력값 부족 시 메시지 표시
            Toast.makeText(SignUpActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // 이미 저장된 이메일로 로그인 시도
    private void signInExistingUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공 시 MainActivity로 이동
                            Toast.makeText(SignUpActivity.this, "이미 가입된 계정으로 로그인되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        } else {
                            // 로그인 실패 시 처리
                            Toast.makeText(SignUpActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            // 실패 후 MainActivity로 이동
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        }
                    }
                });
    }
}