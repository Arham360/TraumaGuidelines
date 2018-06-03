package edu.mcw.GeneralSurgery.dagger;

import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import edu.mcw.GeneralSurgery.Network.APIHelper;

/**
 * Created by arham on 2/9/18.
 */


@Module
public class APIModule {
    @MainScope
    @Provides
    @Inject
    APIHelper provideAPIHelper(Context context) {
        return new APIHelper(context);
    }
}
