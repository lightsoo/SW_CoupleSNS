package swmaestro.lightsoo.game;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Handler.BackPressCloseHandler;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.RestAPI.TestAPI;

public class MainActivity extends AppCompatActivity {


    private BackPressCloseHandler backPressCloseHandler;
    private Button btn_myinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getHash();



        //이걸로 기존에 뜨는 Title을 안보이게 한다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        backPressCloseHandler = new BackPressCloseHandler(this);



        btn_myinfo = (Button)findViewById(R.id.btn_myinfo);
        btn_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = NetworkManager.getInstance().getAPI(TestAPI.class).getMyInfo();
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Response response, Retrofit retrofit) {
                        Toast.makeText(MainActivity.this, "서버전송 성공", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });

    }



    @Override
    public void onBackPressed() {backPressCloseHandler.onBackPressed();}



    public void getHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }




}
