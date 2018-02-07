package com.learnacad.learnacad;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Models.Messages;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import static android.content.ContentValues.TAG;

/**
 * Created by sahil on 31/12/17.
 */

public class FlurryAnalytics extends Application {

    String title, body, imgurl;
    Long position;
    Messages messages;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "3PP9B84K8TFR8T4N7W7D");
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

        @Override
        public void notificationReceived(OSNotification notification) {


            title = notification.payload.title;
            body = notification.payload.body;
            imgurl = notification.payload.bigPicture;
            if (imgurl == null)
                messages = new Messages(title, body, false);
            else
                messages = new Messages(title, body, false, imgurl);

            addtodatabase();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addtodatabase() {

        new AsyncTask<Void, Void, Long>() {


            @Override
            protected Long doInBackground(Void... voids) {
                return SugarRecord.save(messages);

            }

            @Override
            protected void onPostExecute(Long id) {

                position = id;

                Intent local = new Intent();

                local.setAction("com.hello.action");
                Log.i(TAG, "onPostExecute: notificationrecieved");
                getApplicationContext().sendBroadcast(local);

            }
        }.execute();
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

        }
    }
}
