package edu.mcw.GeneralSurgery.UI.Topic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.Network.APIHelper;
import edu.mcw.GeneralSurgery.Network.GenericNetworkCallback;
import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Content.ContentActivity;
import edu.mcw.GeneralSurgery.UI.Feedback.FeedbackActivity;
import edu.mcw.GeneralSurgery.UI.SearchActivity.SearchActivity;
import edu.mcw.GeneralSurgery.UI.SettingsPage.SettingActivity;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.DBhelper;
import edu.mcw.GeneralSurgery.models.SharedPreferencesHelper;
import edu.mcw.GeneralSurgery.models.Topic;

public class TopicActivity extends AppCompatActivity implements TopicFragment.OnListFragmentInteractionListener {

    Context context;
    @Inject
    APIHelper apiHelper;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProgressBar progressBar;
    DBhelper dBhelper;
    Intent intent;

    int currentTopicID = -1;// this variable and "topicId" are intertwined to work together.

    Menu myMenu;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        ((MainApplication)getApplicationContext()).getAppComponent().inject(this); //injecting dependencies

        context = this;

        intent = getIntent();
        if (intent.getIntExtra(getResources().getString(R.string.searchID), -1) != -1){ //helps keep track of searchID
            // if you go back to the search activity
            currentTopicID = intent.getIntExtra(getResources().getString(R.string.searchID), -1);
        }
        if (intent.getIntExtra(getResources().getString(R.string.topicID), -1) != -1){ //this is used for coming back from the search or content activity as
            // the recursive fragments are loaded based on topicIDs

            currentTopicID = intent.getIntExtra(getResources().getString(R.string.topicID), -1);
        }

        dBhelper = new DBhelper(context);

        fragmentManager = getSupportFragmentManager();

        progressBar = findViewById(R.id.progressBar);

        requestContent();

    }

    public void requestContent() {
        showLoading();

        apiHelper.downloadLocalData(new GenericNetworkCallback() {
            @Override
            public void onSuccess(String title, String message) {
                hideLoading();

                if(dBhelper.getContentFromTopicID(currentTopicID).size() >= 1) {
                    Intent intent = new Intent(TopicActivity.this, ContentActivity.class);
                    intent.putExtra(getResources().getString(R.string.topicID), currentTopicID);
                    startActivity(intent);

                }else {

                    TopicFragment topicFragment = TopicFragment.newInstance(currentTopicID);

                    fragmentTransaction = fragmentManager.beginTransaction();

                    if(intent.getIntExtra(getResources().getString(R.string.topicID), -1) != -1){
                        //these animations are reversed for going backwards
                        fragmentTransaction.setCustomAnimations( R.anim.pop_enter, R.anim.pop_exit,R.anim.enter, R.anim.exit);

                    }else {
                        //this sets animations as if we are going forwards
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    }
                    fragmentTransaction.add(R.id.topicFragmentContainer, topicFragment);

                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onError(String title, String message) {
                hideLoading();
                alert(title, message, getString(R.string.button_retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestContent();//can request to get content infinitely if error
                    }
                });
            }
        });
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        myMenu = menu;
        menuInflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuFeedbackButton) {

                Intent intent = new Intent(TopicActivity.this, FeedbackActivity.class);
                intent.putExtra(getResources().getString(R.string.ID), currentTopicID); //wherever ID is being used, it is strictly for feedback purposes
                intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.topic));//this is also required for feedback
                startActivity(intent);
                return true;

        }

        if (id == R.id.menuHomeButton) {
            if (currentTopicID != -1){
                currentTopicID = -1;

                TopicFragment topicFragment = TopicFragment.newInstance(currentTopicID);

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.home, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                fragmentTransaction.replace(R.id.topicFragmentContainer, topicFragment);
                fragmentTransaction.commit();

            }

        }

        if (id == R.id.menuSearchButton) {
            Intent intent = new Intent(TopicActivity.this, SearchActivity.class);
            intent.putExtra(getResources().getString(R.string.topicID), currentTopicID);
            startActivity(intent);
            return true;
        }
        if (id == R.id.menuSettingButton) {
            Intent intent = new Intent(TopicActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (currentTopicID!=-1){

            Topic parent = dBhelper.getTopic(dBhelper.getTopic(currentTopicID).getParentID());
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations( R.anim.pop_enter, R.anim.pop_exit,R.anim.enter, R.anim.exit);
            if (parent != null) {
                currentTopicID = parent.getId();

                setTitle(parent.getTitle());
                TopicFragment mainFragment = TopicFragment.newInstance(currentTopicID);
                fragmentTransaction.replace(R.id.topicFragmentContainer, mainFragment).addToBackStack(null).commit();
            }else{
                currentTopicID = -1;
                setTitle(getResources().getString(R.string.application_name));
                TopicFragment mainFragment = TopicFragment.newInstance(currentTopicID);
                fragmentTransaction.replace(R.id.topicFragmentContainer, mainFragment).addToBackStack(null).commit();
            }
        }else{
            vibrate(300);
        }

    }

    public void alert(String title, String message, String buttonText, @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(message);
        builder.setNegativeButton(buttonText, onClickListener);
        builder.create().show();
    }

    private void vibrate(int duration) {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(duration);
    }

    @Override
    public void onNavListFragmentInteraction(Topic item) {

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.exit_r, R.anim.enter_r);
        currentTopicID = item.getId();//this allows the navigation bar to reload the fragment with the one the user clicked on

        setTitle(item.getTitle());
        TopicFragment mainFragment = TopicFragment.newInstance(item.getId());
        fragmentTransaction.replace(R.id.topicFragmentContainer, mainFragment).addToBackStack(null).commit();

    }

    public void initMenu(){//root menu should not display the home button nor feedback button
        myMenu.findItem(R.id.menuHomeButton).setVisible(false);
        myMenu.findItem(R.id.menuFeedbackButton).setVisible(false);

    }

    public void expandMenu(){//show buttons otherwise
        myMenu.findItem(R.id.menuHomeButton).setVisible(true);
        myMenu.findItem(R.id.menuFeedbackButton).setVisible(true);

    }

    @Override
    public void onListFragmentInteraction(Topic item) {

        if (dBhelper.getContentFromTopicID(item.getId()).size() == 0 && dBhelper.getTopicsBasedOnID(item.getId()).size() == 0) {
            vibrate(100);//vibrate if we dont have any content to give feedback to user.
        } else {

            if (dBhelper.getContentFromTopicID(item.getId()).size() >= 1) {

                Intent intent = new Intent(TopicActivity.this, ContentActivity.class);
                intent.putExtra(getResources().getString(R.string.topicID), item.getId());

                startActivity(intent);

            } else {

                fragmentManager = getSupportFragmentManager();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                currentTopicID = item.getId();

                setTitle(item.getTitle());
                TopicFragment mainFragment = TopicFragment.newInstance( item.getId());
                fragmentTransaction.replace(R.id.topicFragmentContainer, mainFragment).addToBackStack(null).commit();

            }
        }
    }

    @Override
    public void onListFeedbackInteraction(Topic mItem) {
        Intent intent = new Intent(TopicActivity.this, FeedbackActivity.class);
        intent.putExtra(getResources().getString(R.string.ID), mItem.getId());
        intent.putExtra(getResources().getString(R.string.type), getResources().getString(R.string.topic));
        startActivity(intent);
    }
}
