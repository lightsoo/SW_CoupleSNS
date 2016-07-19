package swmaestro.lightsoo.game.Event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import swmaestro.lightsoo.game.Data.Anni;
import swmaestro.lightsoo.game.Manager.NetworkManager;
import swmaestro.lightsoo.game.R;
import swmaestro.lightsoo.game.RestAPI.HyodolAPI;

public class InfoEventActivity extends AppCompatActivity {

    public static final String TAG = "InfoEventActivity";
    private int event_id;
    private TextView tv_title, tv_place, toolbar_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        이걸로 기존에 뜨는 Title을 안보이게 한다.
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        getEventInfo();

    }
    public void init(){
        Intent intent = new Intent(getIntent());
        event_id = intent.getExtras().getInt("event_id");
        tv_title = (TextView)findViewById(R.id.tv_info_event_title);
        tv_place=(TextView)findViewById(R.id.tv_info_event_place);
        toolbar_date = (TextView)findViewById(R.id.toolbar_info_event_date);


//        Log.d(TAG, "event_id : ", "" + event_id);
    }
    public void getEventInfo(){
        Call call = NetworkManager.getInstance().getAPI(HyodolAPI.class).getEventInfo(event_id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                Anni anni = (Anni)response.body();
                //이미지는 Fitta 꺼 참고 하자
                //이외에 것은 Retrofit_Tutorial_master 참고
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
