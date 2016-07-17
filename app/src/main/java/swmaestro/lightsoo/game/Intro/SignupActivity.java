package swmaestro.lightsoo.game.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Data.Message;
import swmaestro.lightsoo.game.MainActivity;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.R;
import swmaestro.lightsoo.game.RestAPI.TestAPI;

public class SignupActivity extends AppCompatActivity {

    private EditText et_email, et_pwd, et_repwd, et_name;
    private String email, pwd, repwd, name;
    private Button btn_signup;

    private boolean checkEmail = false; //아이디 중복확인성공 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //백키가 나온다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //백키 이벤트
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        init();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                pwd = et_pwd.getText().toString();
                repwd = et_repwd.getText().toString();
                name = et_name.getText().toString();
                //유효성 검사
                if (preInspection()) {

                    Call call = NetworkManager.getInstance().getAPI(TestAPI.class).join(email,pwd,name);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Message msg = (Message) response.body();

                                Call call1 = NetworkManager.getInstance().getAPI(TestAPI.class).authLocalLogin(email, pwd);
                                call1.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Response response, Retrofit retrofit) {
                                        Toast.makeText(SignupActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });





                            } else {
                                Toast.makeText(SignupActivity.this, "서버전송인데 200ok가 아니야...", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(SignupActivity.this, "서버전송 실패 : ", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });
    }

    public void init() {
        et_email = (EditText) findViewById(R.id.et_signup_email);
        et_pwd = (EditText) findViewById(R.id.et_signup_pwd);
        et_repwd = (EditText) findViewById(R.id.et_signup_repwd);
        et_name = (EditText) findViewById(R.id.et_signup_name);
        btn_signup = (Button) findViewById(R.id.btn_signup);
    }

    public boolean preInspection() {
        if (!email.contains("@")) {
            Toast.makeText(SignupActivity.this, "유효한 아이디가 아닙니다.", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (!checkEmail) {
//            Toast.makeText(SignupActivity.this, "아이디 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
//            return false;
        } else if (!(pwd.equals(repwd))){
            Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pwd)||
                TextUtils.isEmpty(repwd)||
                TextUtils.isEmpty(name)) {
            Toast.makeText(SignupActivity.this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else {

            return true;
        }
    }
}
