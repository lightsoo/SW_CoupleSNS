package swmaestro.lightsoo.game.Event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import swmaestro.lightsoo.game.R;

public class FirstEventActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private EditText et_title;
    private Button btn_add;
    private  String event_title, event_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //이걸로 기존에 뜨는 Title을 안보이게 한다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //백키가 나온다.
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        init();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_title = et_title.getText().toString();
                if(preInspection()){
                    Toast.makeText(FirstEventActivity.this, "이벤트 추가 성공", Toast.LENGTH_SHORT).show();
                    Toast.makeText(FirstEventActivity.this, event_title + event_date, Toast.LENGTH_SHORT).show();


                    /*Call call = NetworkManager.getInstance().getAPI(HyodolAPI.class).addEvent(event_title, event_date);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            Toast.makeText(FirstEventActivity.this, "이벤트 추가 성공", Toast.LENGTH_SHORT).show();
                            Message msg = (Message) response.body();
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });*/

                }


            }
        });


    }


    public void init(){
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        et_title = (EditText)findViewById(R.id.et_title);
        btn_add = (Button)findViewById(R.id.btn_add);


        datePicker.init(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        event_date = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);

                    }
                });

    }

    public boolean preInspection() {
        if(TextUtils.isEmpty(event_title)  ) {
            Toast.makeText(FirstEventActivity.this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

}
