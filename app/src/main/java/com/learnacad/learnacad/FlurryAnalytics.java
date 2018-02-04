package com.learnacad.learnacad;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by sahil on 31/12/17.
 */

public class FlurryAnalytics extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this,"3PP9B84K8TFR8T4N7W7D");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
