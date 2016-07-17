package swmaestro.lightsoo.game.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Data.User;
import swmaestro.lightsoo.game.Handler.BackPressCloseHandler;
import swmaestro.lightsoo.game.MainActivity;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.R;
import swmaestro.lightsoo.game.RestAPI.TestAPI;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //server response code
    private static final int CODE_ID_PASS_INCORRECT = 531;

    //for facebook
    CallbackManager callbackManager;
    LoginManager mLoginManager;
    AccessTokenTracker tracker;
    private Button btn_fb;

    //    for local
    private Button btn_local;

    //    String loginType;
    String accessToken;
    User user;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        backPressCloseHandler = new BackPressCloseHandler(this);

        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrLogout2();
            }
        });



        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignupActivity();
            }
        });


        //이렇게 생성해주기만하면 트래킹이 작동한다. 그래서 액티비티 종료되면 트랙킹도 종료해야한다.
        //로그인 매니저에서 콜밷 등록을 해서 작업이 종료되면 호출된다!!!
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d(TAG, "트랙커 토큰 체인지!");

                final AccessToken token = AccessToken.getCurrentAccessToken();
//                Log.d(TAG, "token : " + token.getToken());
                if(token != null){

//                    userLoginId = token.getUserId();
                    accessToken = token.getToken();
//                    Log.d(TAG, "userLoginId : " + userLoginId);

//                    user = new User(userLoginId, PropertyManager.LOGIN_TYPE_FACEBOOK);
                    Call call = NetworkManager.getInstance().getAPI(TestAPI.class).authFacebookLogin(accessToken);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            if(response.isSuccess()){
                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
//                                PropertyManager.getInstance().setUserLoginId(userLoginId);
//                                PropertyManager.getInstance().setLoginType(PropertyManager.LOGIN_TYPE_FACEBOOK);

                                goSignupActivity();

                            } else {
                                if(response.code() == CODE_ID_PASS_INCORRECT){
                                    Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }
        };
    }

    public void init(){
        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        btn_fb = (Button)findViewById(R.id.btn_fb);
        btn_local = (Button)findViewById(R.id.btn_local);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //트랙킹 종료
        tracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이걸 반드시해줘야한다. 얘가 있어야 콜백이 호출된다. 액티비티가 받은 결과를 callbackmanager로 토스!!!
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //로그인 성공하면 메인으로 이동하고 이전액티비티는 종료한다.
    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //로그인 성공하면 메인으로 이동하고 이전액티비티는 종료한다.
    private void goSignupActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        tracker.stopTracking();
        finish();
    }

//    for facebook
    private void loginOrLogout2(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token == null) {
            mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
            mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
            mLoginManager.logInWithReadPermissions(this, null);
        } else {
            mLoginManager.logOut();
        }
    }

    @Override
    public void onBackPressed() {backPressCloseHandler.onBackPressed();}

}