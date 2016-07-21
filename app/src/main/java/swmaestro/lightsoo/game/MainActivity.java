package swmaestro.lightsoo.game;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Adapter.AnniAdapter;
import swmaestro.lightsoo.game.Data.Anni;
import swmaestro.lightsoo.game.Event.AddEventActivity;
import swmaestro.lightsoo.game.Event.InfoEventActivity;
import swmaestro.lightsoo.game.Handler.BackPressCloseHandler;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.Manager.PropertyManager;
import swmaestro.lightsoo.game.RestAPI.HyodolAPI;
import swmaestro.lightsoo.game.RestAPI.PushService;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";

    Handler mHandler = new Handler(Looper.getMainLooper());

    SwipeRefreshLayout swipeRefreshLayout;
    ListView lv_anni;
    AnniAdapter anniAdapter;

    private BackPressCloseHandler backPressCloseHandler;
    private ImageButton btn_addevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //이걸로 기존에 뜨는 Title을 안보이게 한다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        backPressCloseHandler = new BackPressCloseHandler(this);
//        getHash();
        init();
        getEvents();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getEvents();
                    }
                }, 1500);
            }
        });


        btn_addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddEventActivity();
            }
        });

    }


    private void goAddEventActivity(){
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
//        finish();
    }

    public void init(){
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        lv_anni = (ListView)findViewById(R.id.lv_anni);
//        anniAdapter = new AnniAdapter();
//        lv_anni.setAdapter(anniAdapter);
        lv_anni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object data = lv_anni.getItemAtPosition(position);
                if (data instanceof String) {
                    Toast.makeText(MainActivity.this, "Header : " + (String) data, Toast.LENGTH_SHORT).show();
                } else if (data instanceof Anni) {
                    Anni p = (Anni) data;
                    Toast.makeText(MainActivity.this, "id : " + p.getId(), Toast.LENGTH_SHORT).show();
                    //여기서 아이디를 넘겨줘서 액티비티를 만들고 넘겨받은 아이디를 가지고 해당 이벤트의 상세정보를 볼수가 있다.!!!
                    Intent intent = new Intent(MainActivity.this, InfoEventActivity.class);
                    intent.putExtra("event_id", p.getId());
                    startActivity(intent);


//                    Toast.makeText(MainActivity.this, "title : " + p.getTitle(), Toast.LENGTH_SHORT).show();


                }


            }
        });
//        initData();

        btn_addevent = (ImageButton)findViewById(R.id.btn_addevent);

    }

    public void getEvents(){
        Log.d(TAG,"getEvents()");
        Call<List<Anni>> call = NetworkManager.getInstance().getAPI(HyodolAPI.class).getEvents();
        call.enqueue(new Callback<List<Anni>>() {
            @Override
            public void onResponse(Response<List<Anni>> response, Retrofit retrofit) {
                List<Anni> result = response.body();


//                Anni anni = new Anni(i, title, date1);
//                Log.d(TAG, "result : " + result);
//                Log.d(TAG, "response = " + new Gson().toJson(result));
                anniAdapter = new AnniAdapter();
                anniAdapter.addAll(result);
                lv_anni.setAdapter(anniAdapter);
                //일단 메인에서 한번 테스트 한다음에 로그인이나 스플래쉬로 옮긴다.
//                registerToken();

            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    /*private void initData() {
        Random r = new Random();
        int i =0;
        for (; i < 20; i++) {
            int date = 20 + r.nextInt(20);
            if (date % 3 == 0) {
                date = 0 ;
            }
            String title = "기념일" + i;
            String date1 = "" + date;

            Anni anni = new Anni(i, title, date1);
            anniAdapter.add(anni);
        }
    }*/

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

    public void registerToken(){
        Call call = NetworkManager.getInstance().getAPI(PushService.class).register(PropertyManager.getInstance().getRegistrationToken());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {

                Toast.makeText(MainActivity.this, "토큰 추가 성공", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(MainActivity.this, "토큰 추가 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
