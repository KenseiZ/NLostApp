package entreprise.nlost.nlostapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

/**
 * Created by Bedino-Tom on 23/03/2018.
 */

public class ParamActivity extends AppCompatActivity implements
        View.OnClickListener {
    private Switch notif;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.param_main);

        notif = (Switch)findViewById(R.id.switch1);

        ok = (Button)findViewById(R.id.button3);


        ok.setOnClickListener(this);

        if(MainActivity.configapp.GetActiveNotification() == true)
        {
            notif.setChecked(true);
        }
        else
        {
            notif.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button3:
                MainActivity.configapp.SetActiveNotification(notif.isChecked());
                finish();
                break;
        }
    }

}
