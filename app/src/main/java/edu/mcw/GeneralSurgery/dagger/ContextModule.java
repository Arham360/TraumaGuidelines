package edu.mcw.GeneralSurgery.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cjsampon on 2/6/18.
 */

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @MainScope
    @Provides
    Context provideContext() {
        return context;
    }
}
