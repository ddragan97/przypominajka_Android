package pl.wsiz.przypominajka;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.Objects;

public class NoticeBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String titl = Objects.requireNonNull(intent.getExtras()).getString("titll");
        String date = intent.getExtras().getString("datee");
        String time = intent.getExtras().getString("timee");

        int idBC = (int) System.currentTimeMillis();
        Intent ii = new Intent(context, HelloActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyChannel");
        builder.setSmallIcon(R.drawable.notify_ico);
        builder.setContentTitle(titl);
        builder.setContentText(date + " " + time);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(idBC, builder.build());
    }
}
