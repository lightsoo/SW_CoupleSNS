package swmaestro.lightsoo.game;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
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
import java.util.Random;

import swmaestro.lightsoo.game.Adapter.AnniAdapter;
import swmaestro.lightsoo.game.Data.Anni;
import swmaestro.lightsoo.game.Event.AddEventActivity;
import swmaestro.lightsoo.game.Handler.BackPressCloseHandler;

public class MainActivity extends AppCompatActivity {

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
//        getHash();
        init();
        //이걸로 기존에 뜨는 Title을 안보이게 한다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        backPressCloseHandler = new BackPressCloseHandler(this);

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
        lv_anni = (ListView)findViewById(R.id.lv_anni);
        anniAdapter = new AnniAdapter();
        lv_anni.setAdapter(anniAdapter);
        lv_anni.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object data = lv_anni.getItemAtPosition(position);
                if (data instanceof String) {
                    Toast.makeText(MainActivity.this, "Header : " + (String) data, Toast.LENGTH_SHORT).show();
                } else if (data instanceof Anni) {
                    Anni p = (Anni) data;
                    Toast.makeText(MainActivity.this, "title : " + p.getTitle(), Toast.LENGTH_SHORT).show();


                }


            }
        });
        initData();

        btn_addevent = (ImageButton)findViewById(R.id.btn_addevent);

    }

    private void initData() {
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
