package com.example.wolf_z.bookingroom.Custom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.example.wolf_z.bookingroom.BookingDetail_Offline;
import com.example.wolf_z.bookingroom.R;

import java.util.Objects;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        /** check command to action notification*/
        if (Objects.equals(intent.getExtras().getString("command"), "clear_all_notification")) {
            NotificationManager notificationManager_clearall = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager_clearall.cancelAll();

        } else if (Objects.equals(intent.getExtras().getString("command"), "create_notification")) {

            int bookingid = intent.getExtras().getInt("bookingid");
            int notify_id = intent.getExtras().getInt("notify_id");
            String subject = intent.getExtras().getString("subject");
            String detail = intent.getExtras().getString("detail");
            String meetingtype = intent.getExtras().getString("meetingtype");
            String date = intent.getExtras().getString("date");
            String starttime = intent.getExtras().getString("starttime");
            String endtime = intent.getExtras().getString("endtime");
            int roomid = intent.getExtras().getInt("roomid");
            int projector = intent.getExtras().getInt("projector");

            Intent notificationIntent = new Intent(context, BookingDetail_Offline.class);
            notificationIntent.putExtra("bookingid", bookingid);
            notificationIntent.putExtra("subject", subject);
            notificationIntent.putExtra("detail", detail);
            notificationIntent.putExtra("meetingtype", meetingtype);
            notificationIntent.putExtra("date", date);
            notificationIntent.putExtra("starttime", starttime);
            notificationIntent.putExtra("endtime", endtime);
            notificationIntent.putExtra("roomid", roomid); //int
            notificationIntent.putExtra("projector", projector); //int

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(BookingDetail_Offline.class);
            stackBuilder.addNextIntent(notificationIntent);

            // index intent as notify_id - 1
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(notify_id - 1, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Uri notif_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //create bitmap from setLargeIcon
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.bookinglogo);

            Notification notification = builder.setContentTitle(intent.getExtras().getString("subject"))
                    .setContentText("Your have a meeting at " + intent.getExtras().getString("date") + " , " + intent.getExtras().getString("starttime"))
                    .setLargeIcon(bm)
                    .setSound(notif_sound)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // index notify as notify_id - 1
            notificationManager.notify(notify_id - 1, notification);
        }
    }
}
