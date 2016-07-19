package swmaestro.lightsoo.game.View;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import swmaestro.lightsoo.game.Data.Anni;
import swmaestro.lightsoo.game.R;

/**
 * Created by LG on 2016-07-17.
 */
public class AnniView extends FrameLayout {

    private TextView titleView, dateView, placeView;
    private Anni anni;

    public AnniView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_main_anniitem, this);
        titleView = (TextView)findViewById(R.id.view_tv_title);
        dateView = (TextView)findViewById(R.id.view_tv_date);
        placeView = (TextView)findViewById(R.id.view_tv_place);
    }

    public void setAnni(Anni anni){
        this.anni = anni;
        this.titleView.setText(anni.getTitle());
        this.dateView.setText(anni.getDate());
        this.placeView.setText(anni.getPlace());
    }

}
