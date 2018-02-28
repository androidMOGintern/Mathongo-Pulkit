package com.learnacad.learnacad;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.NotificationList;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Models.Messages;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.orm.SugarApp;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
import static com.orm.SugarRecord.listAll;
import static java.lang.Boolean.FALSE;

/**
 * Created by sahil on 31/12/17.
 */

public class FlurryAnalytics extends Application {

    String title, body, imgurl;
    Long position;
    Messages messages;
    ArrayList<Lecture> lectures;
    static boolean savetodatabase = true;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        lectures = new ArrayList<>();
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
                messages.setLecture_id(data.optBoolean("ISLECTURE", false));
                if (data.optBoolean("ISLECTURE"))
                    fetchdata(Integer.valueOf(data.optString("MINICOURSE_ID", "0")));
            }
            Log.i(TAG, "notificationReceived: " + savetodatabase);
            if (!data.optBoolean("ISLIVE"))
                if (savetodatabase)
                    addtodatabase();
        }
    }

    private void fetchdata(Integer minicourse_id) {

        Log.i(TAG, "fetchdata: " + minicourse_id);
        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses/" + minicourse_id)
                .addHeaders("Authorization", "bearer" + session.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray lessons = response.getJSONArray("lessons");

                            for (int i = 0; i < lessons.length(); ++i) {

                                JSONObject lesson = lessons.getJSONObject(i);
                                String lesson_name = lesson.getString("name");
                                String url = lesson.getString("videoUrl");
                                String lesson_duration = lesson.getString("duration");
                                int lesson_id = lesson.getInt("id");
                                String lesson_description = lesson.getString("description");
                                int upvotes = lesson.getInt("upvotes");

                                Lecture lecture = new Lecture(lesson_id, lesson_name, url, lesson_duration, lesson_description, upvotes, false, false);
                                lectures.add(lecture);

                            }

                        } catch (JSONException e) {
                            Log.i(TAG, "fetchdata: catch");
                            new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_LF_MI)")
                                    .setTitleText("Oops..!!")
                                    .show();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i(TAG, "fetchdata: error");
                        savetodatabase=FALSE;

                    }
                });

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
            final JSONObject data = result.notification.payload.additionalData;
            String customKey = null;
            String process_id = null;
            String material_name = null;
            Integer minicourse_id = null;
            String category_level_I = null;
            String category_level_II = null;
            Boolean lecture_id = false;
            Boolean islive = false;
            String video_id = null;
            final Intent resultIntent;
            if (data != null) {
                customKey = data.optString("intent", null);
                minicourse_id = Integer.valueOf(data.optString("MINICOURSE_ID", "0"));
                process_id = minicourse_id + "A";
                material_name = data.optString("MATERIAL_NAME");
                category_level_I = data.optString("CATEGORY_1");
                category_level_II = data.optString("CATEGORY_2");
                lecture_id = data.optBoolean("ISLECTURE");
                islive = data.optBoolean("ISLIVE");
                video_id = data.optString("VIDEO_ID");
            }
            if (customKey != null) {
                if ((customKey.equals("com.learnacad.learnacad.Material") || customKey.equals("com.learnacad.learnacad.Lecture") || customKey.equals("com.learnacad.learnacad.Library")) && savetodatabase)
                    resultIntent = new Intent(customKey);
                else
                    resultIntent = new Intent(getApplicationContext(), NotificationList.class);
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
                }
                if (lecture_id && savetodatabase) {
                    resultIntent.putExtra("lectureList", lectures);
                    resultIntent.putExtra("selectedPosition", lectures.size() - 1);
                    resultIntent.putExtra("selectedLecture", lectures.get(lectures.size() - 1));
                } else {
                    resultIntent.putExtra("PROCESS_ID", process_id);
//                    resultIntent.putExtra("LECTURE",1);

                }
            } else if (islive) {
                resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_id));
                resultIntent.putExtra("VIDEO_ID", video_id);
            } else

            {
                resultIntent = new Intent(getApplicationContext(), NotificationList.class);
            }
            new AsyncTask<Void, Void, Void>()

            {
                @Override
                protected Void doInBackground(Void... voids) {
                    SugarRecord.executeQuery("Update Messages SET seen = 1 where id = ?", String.valueOf(position));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    startActivity(resultIntent);
                }
            }.

                    execute();
        }
    }
}
