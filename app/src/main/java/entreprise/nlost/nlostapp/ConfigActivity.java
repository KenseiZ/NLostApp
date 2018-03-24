package entreprise.nlost.nlostapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Bedino-Tom on 23/03/2018.
 */

public class ConfigActivity {
    private SharedPreferences config;

    public ConfigActivity(Context a, String file)
    {
        config = a.getSharedPreferences(file, Context.MODE_PRIVATE);

        if(config == null)
        {
            Toast.makeText(a, "Impossible de lire la configuration !",Toast.LENGTH_LONG).show();
        }
    }

    public boolean GetActiveNotification()
    {
        return config.getBoolean("NOTIFICATION_ACTIVATION", false);
    }

    public void SetActiveNotification(boolean a)
    {
        SharedPreferences.Editor edit = config.edit();

        edit.putBoolean("NOTIFICATION_ACTIVATION", a);

        edit.commit();
    }

}
