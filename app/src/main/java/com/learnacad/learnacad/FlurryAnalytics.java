package com.learnacad.learnacad;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.NotificationList;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Models.Messages;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import org.json.JSONObject;

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

            JSONObject data = notification.payload.additionalData;

            title = notification.payload.title;
            body = notification.payload.body;
            imgurl = notification.payload.bigPicture;
            if (imgurl == null)
                messages = new Messages(title, body, false);
            else
                messages = new Messages(title, body, false, imgurl);
            if (data != null) {

                messages.setIntent(data.optString("intent", null));
                messages.setMinicourse_id(Integer.valueOf(data.optString("MINICOURSE_ID", "0")));
                messages.setProcess_id(Integer.valueOf(data.optString("MINICOURSE_ID", "0")) + "A");
                messages.setMaterial_name(data.optString("MATERIAL_NAME", null));
                messages.setCategory_level_I(data.optString("CATEGORY_1", null));
                messages.setCategory_level_II(data.optString("CATEGORY_2", null));

            }

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

        @SuppressLint("StaticFieldLeak")
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            JSONObject data = result.notification.payload.additionalData;
            String customKey = null;
            String process_id = null;
            String material_name = null;
            Integer minicourse_id = null;
            String category_level_I = null;
            String category_level_II = null;
            final Intent resultIntent;
            if (data != null) {
                customKey = data.optString("intent", null);
                minicourse_id = Integer.valueOf(data.optString("MINICOURSE_ID", "0"));
                process_id = minicourse_id + "A";
                material_name = data.optString("MATERIAL_NAME");
                category_level_I = data.optString("CATEGORY_1");
                category_level_II = data.optString("CATEGORY_2");
            }
            if (customKey != null) {
                resultIntent = new Intent(customKey);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                resultIntent.putExtra("MINICOURSE_ID", minicourse_id);
                if (material_name != null && category_level_I == null) {
                    Material m = new Material(material_name, minicourse_id);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Material", m);
                    resultIntent.putExtras(bundle);
                    resultIntent.putExtra("TO_SHOW", "MATERIAL");
                }
                if (material_name != null && category_level_I != null) {
                    Material m = new Material(material_name, minicourse_id);
                    Bundle bundle = new Bundle();
                    m.setCategory_Level_I(category_level_I);
                    if (category_level_II != null)
                        m.setCategory_Level_II(category_level_II);
                    bundle.putSerializable("Material", m);
                    resultIntent.putExtras(bundle);
                    resultIntent.putExtra("TO_SHOW", "RESOURCE");
                } else {
                    resultIntent.putExtra("PROCESS_ID", process_id);
//                    resultIntent.putExtra("LECTURE",1);

                }
            } else {
                resultIntent = new Intent(getApplicationContext(), NotificationList.class);
            }
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    SugarRecord.executeQuery("Update Messages SET seen = 1 where id = ?", String.valueOf(position));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startActivity(resultIntent);
                }
            }.execute();
        }
    }
}
