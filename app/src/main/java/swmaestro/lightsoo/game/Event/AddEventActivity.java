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

public class AddEventActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private EditText et_title;
    private Button btn_addevent;
    private  String event_title, event_date;
    private boolean checkDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
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

        btn_addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_title = et_title.getText().toString();
                if (preInspection()) {

                    Toast.makeText(AddEventActivity.this, "이벤트 추가 성공", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }


    public void init(){
        datePicker = (DatePicker)findViewById(R.id.datepicker_addevent);
        et_title = (EditText)findViewById(R.id.et_title_addevent);
        btn_addevent = (Button)findViewById(R.id.btn_addevent);

        datePicker.init(datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        event_date = String.format("%d / %d / %d", year, monthOfYear + 1, dayOfMonth);
                        checkDate = true;
                    }
                });
    }

    public boolean preInspection() {
        if(TextUtils.isEmpty(event_title)  ) {
            Toast.makeText(AddEventActivity.this, "제목을 적으세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!checkDate) {
            Toast.makeText(AddEventActivity.this, "날짜를 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}
