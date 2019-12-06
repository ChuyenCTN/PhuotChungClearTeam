package com.clearteam.phuotnhom.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.clearteam.phuotnhom.fragment.TourAllFragment;
import com.clearteam.phuotnhom.model.Notifi;
import com.clearteam.phuotnhom.notification.Data;
import com.clearteam.phuotnhom.notification.OreoNotification;
import com.clearteam.phuotnhom.ui.DetailNotify;
import com.clearteam.phuotnhom.ui.DetailNotifyAllMember;
import com.clearteam.phuotnhom.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Notifi notifi;
    private Data data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");
        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        String currentUser = preferences.getString("currentuser", "none");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
            if (!currentUser.equals(user)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreNotification(remoteMessage);
//                } else {
//                    sendNotification(remoteMessage);
//                }
                }
            }
        }
    }

    private void sendOreNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String nameSender = remoteMessage.getData().get("nameSender");
        String dataJson = remoteMessage.getData().get("dataJson");


        try {
            JSONObject json = new JSONObject(dataJson);
            String id = json.getString("id");
            String idGroup = json.getString("idGroup");
            String sender = json.getString("sender");
            String receiver = json.getString("receiver");
            String message = json.getString("message");
            String date = json.getString("date");
            String hour = json.getString("hour");
            String nameSender1 = json.getString("nameSender");
            String status = json.getString("status");
            notifi = new Notifi(id, idGroup, sender, receiver, message, date, hour, nameSender1, status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonAll = new JSONObject(dataJson);
            String content = jsonAll.getString("content");
            String title1 = jsonAll.getString("title");
            String nameSender1 = jsonAll.getString("nameSender");
            data = new Data(title1, nameSender1, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        if (title.equals("Thông báo mới")) {
            Intent intent = new Intent(MyFirebaseMessagingService.this, DetailNotify.class);
            intent.putExtra(Const.KEY_NOTIFYCATION, notifi);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon);
            int i = 0;
            if (j > 0) {
                i = j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        } else {
            Intent intent = new Intent(MyFirebaseMessagingService.this, DetailNotifyAllMember.class);
            intent.putExtra(Const.KEY_NOTIFY_ALL_MEMBER, data);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon);
            int i = 0;
            if (j > 0) {
                i = j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }

    }

//    private void sendNotification(RemoteMessage remoteMessage) {
//        String user = remoteMessage.getData().get("user");
//        String icon = remoteMessage.getData().get("icon");
//        String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("body");
//        String dataJson = remoteMessage.getData().get("dataJson");
//        try {
//            JSONObject json = new JSONObject(dataJson);
//            String id = json.getString("id");
//            String idGroup = json.getString("idGroup");
//            String sender = json.getString("sender");
//            String receiver = json.getString("receiver");
//            String message = json.getString("message");
//            String date = json.getString("date");
//            String hour = json.getString("hour");
//            String nameSender = json.getString("nameSender");
//            String status = json.getString("status");
//            notifi = new Notifi(id,idGroup,sender,receiver,message,date,hour,nameSender,status);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
//        Intent intent = new Intent(MyFirebaseMessagingService.this, DetailNotify.class);
//        intent.putExtra(Const.KEY_NOTIFYCATION,notifi);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(Integer.parseInt(icon))
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true)
//                .setSound(defaultSound)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int i = 0;
//        if (j > 0) {
//            i = j;
//        }
//        notificationManager.notify(i, builder.build());
//    }

}
