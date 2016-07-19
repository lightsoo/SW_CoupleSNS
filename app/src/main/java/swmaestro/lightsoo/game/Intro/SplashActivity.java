package swmaestro.lightsoo.game.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Data.User;
import swmaestro.lightsoo.game.Event.FirstEventActivity;
import swmaestro.lightsoo.game.MainActivity;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.Manager.PropertyManager;
import swmaestro.lightsoo.game.R;
import swmaestro.lightsoo.game.RestAPI.TestAPI;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    Handler mHandler = new Handler(Looper.getMainLooper());

    //for facebook
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginManager mLoginManager = LoginManager.getInstance();
    AccessTokenTracker mTokenTracker;

    String loginType;
    String userLoginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        goLoginActivity();
//        goFirstEvent();
        goMainActivity();
//        doRealStart();
    }
    //첫 이벤트 설정
    private void goFirstEvent(){
        startActivity(new Intent(this, FirstEventActivity.class));
        finish();
    }


    private void doRealStart(){
        loginType = PropertyManager.getInstance().getLoginType();
        userLoginId = PropertyManager.getInstance().getUserLoginId();
        //로그인 한적이 없을 경우 혹은 로그아웃했을 경우 → 로그인 액티비티로 이동
        if(TextUtils.isEmpty(loginType)){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "로그인 한적이 없어서 로그인페이지로 이동");
                    goLoginActivity();
                }
            }, 500);
        }else {
            switch (loginType){
                case PropertyManager.LOGIN_TYPE_FACEBOOK:
                    //로그인 id가 존재할경우
                    if(!TextUtils.isEmpty(userLoginId)){

                        Log.d(TAG, "id가 있는경우 :!TextUtils.isEmpty(userLoginId))");
                        Log.d(TAG, "userLoginId : " + userLoginId );

                        loginType = PropertyManager.getInstance().getLoginType();
                        User user = new User(userLoginId, loginType);

                        Call call = NetworkManager.getInstance().getAPI(TestAPI.class).authFacebookLogin(userLoginId);
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Response response, Retrofit retrofit) {
                                if (response.isSuccess()) {//이전에 가입되었던 사람이라면 OK,
                                    Toast.makeText(SplashActivity.this, "페이스북 연동 로그인으로 입장 합니다.", Toast.LENGTH_SHORT).show();
                                    goMainActivity();
                                } else {
                                    //아니라면 not registered
                                    mLoginManager.logOut();
                                    goLoginActivity();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(SplashActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                goLoginActivity();
                            }
                        });

                        mLoginManager.logInWithReadPermissions(this, null);
                    }else{//id가 없을경우에 로그인 페이지로 이동!!!
                        Log.d(TAG, "id가 없는경우 : !TextUtils.isEmpty(userLoginId))");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SplashActivity.this, "Welcome! please log-in!", Toast.LENGTH_SHORT).show();
                                goLoginActivity();
                            }
                        }, 1500);
                    }
                    break;
            }
        }
    }

    private void goMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }



}
