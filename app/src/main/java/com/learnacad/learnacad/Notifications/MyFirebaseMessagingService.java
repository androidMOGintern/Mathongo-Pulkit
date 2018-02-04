package com.learnacad.learnacad.Notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.learnacad.learnacad.Activities.LoginActivity;
import com.learnacad.learnacad.Models.Notifications;
import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sahil Malhotra on 14-10-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationHandler notificationHandler;
    private String TAG = "firebaseNotification";
    private boolean isClickableNotification;



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage == null){
            return;
        }


        Log.d("firebaseNotification","From: - "+remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null){
            Log.d("firebaseNotification","Notification body: " + remoteMessage.getNotification().getBody());

            handleNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d("FirebaseNotifications", "Data Payload: " + remoteMessage.getData().toString());
                Notifications  notifications = new Notifications("datapayload",remoteMessage.getData().toString());
            SugarRecord.save(notifications);
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.d("FirebaseNotifications", "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String body,String title) {

        if(!NotificationHandler.isAppInBackground(getApplicationContext())){
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("body",body);
            pushNotification.putExtra("title",title);
            Notifications notifications = new Notifications(title,body);
            SugarRecord.save(notifications);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }else{
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("body",body);
            pushNotification.putExtra("title",title);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            Notifications notifications = new Notifications(title,body);
            SugarRecord.save(notifications);

        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e("firebaseNotification", "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e("firebaseNotification", "title: " + title);
            Log.e("firebaseNotification", "message: " + message);
            Log.e("firebaseNotification", "isBackground: " + isBackground);
            Log.e("firebaseNotification", "payload: " + payload.toString());
            Log.e("firebaseNotification", "imageUrl: " + imageUrl);
            Log.e("firebaseNotification", "timestamp: " + timestamp);


            if (!NotificationHandler.isAppInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e("firebaseNotification", "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e("firebaseNotification", "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationHandler = new NotificationHandler(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHandler.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationHandler = new NotificationHandler(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationHandler.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


}
