package com.example.wolf_z.bookingroom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;


public class AlarmReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int bookingid = intent.getExtras().getInt("bookingid");
        int notify_id = intent.getExtras().getInt("notify_id");
        String subject = intent.getExtras().getString("subject");

        Intent notificationIntent = new Intent(context, BookingDetail_Offline.class);
        notificationIntent.putExtra("bookingid", bookingid);
        notificationIntent.putExtra("subject", subject);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(BookingDetail_Offline.class);
        stackBuilder.addNextIntent(notificationIntent);

        // index intent as notify_id - 1
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notify_id - 1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Uri notif_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = builder.setContentTitle("Demo App Notification")
                .setContentText("content ....  " + intent.getExtras().getString("content"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(notif_sound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // index notify as notify_id - 1
        notificationManager.notify(notify_id - 1, notification);

    }
}
