package edu.mcw.GeneralSurgery.dagger;

import dagger.Component;
import edu.mcw.GeneralSurgery.Network.APIHelper;
import edu.mcw.GeneralSurgery.UI.Feedback.FeedbackActivity;
import edu.mcw.GeneralSurgery.UI.Home.MainActivity;
import edu.mcw.GeneralSurgery.UI.SearchActivity.SearchActivity;
import edu.mcw.GeneralSurgery.UI.SettingsPage.SettingActivity;
import edu.mcw.GeneralSurgery.UI.Topic.TopicActivity;

/**
 * Created by cjsampon on 2/6/18.
 */

@Component(modules = {
        ContextModule.class,
        SharedPreferencesModule.class,
        APIModule.class
    })
@MainScope
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(APIHelper apiHelper);

    void inject (TopicActivity topicActivity);

    void inject (FeedbackActivity feedbackActivity);

    void inject (SettingActivity settingActivity);

    void inject (SearchActivity searchActivity);
}
