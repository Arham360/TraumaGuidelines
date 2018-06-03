package edu.mcw.GeneralSurgery.dagger;

import android.app.Application;

/**
 * Created by cjsampon on 2/6/18.
 */

public class MainApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                        .contextModule(new ContextModule(getApplicationContext()))
                        .sharedPreferencesModule(new SharedPreferencesModule())
                        .aPIModule(new APIModule())
                        .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
