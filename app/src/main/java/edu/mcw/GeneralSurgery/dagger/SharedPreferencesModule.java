package edu.mcw.GeneralSurgery.dagger;

/**
 * Created by cjsampon on 2/6/18.
 */

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import edu.mcw.GeneralSurgery.R;

@Module
public class SharedPreferencesModule {
    @MainScope
    @Provides
    @Inject
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.preferences_name), Context.MODE_PRIVATE);
    }
}
