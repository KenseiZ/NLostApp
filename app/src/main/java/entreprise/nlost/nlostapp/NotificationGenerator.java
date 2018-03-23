package entreprise.nlost.nlostapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;

import java.nio.channels.Channel;

import static android.app.Notification.PRIORITY_MAX;

/**
 * Created by Kevin on 22/03/2018.
 */

public class NotificationGenerator {
    private static final int NOTIFICATION_ID_OPENACTIVITY = 9;

    public static void OpenActivityNotification(Context context) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context, "Chanel");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notify = new Intent(context, MainActivity.class);

        notify.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK) | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, notify, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent1);
        nc.setSmallIcon(R.drawable.logo);
        nc.setAutoCancel(true);
        nc.setContentTitle("NLost Clé USB Oubliée");
        nc.setAutoCancel(true).setContentTitle("NLost Clé USB Oubliée");
        nc.setStyle(new NotificationCompat.BigTextStyle().bigText("Attention vous avez sans doute oublié votre clé sur un poste informatique !"));
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
        nc.setPriority(Notification.PRIORITY_MAX);
        long[] pattern = { 0, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500};
        nc.setVibrate(pattern);
        nc.setDefaults(Notification.DEFAULT_SOUND);
        nm.notify(NOTIFICATION_ID_OPENACTIVITY, nc.build());
    }
}