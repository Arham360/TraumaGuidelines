package edu.mcw.GeneralSurgery.UI.SettingsPage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.Network.APIHelper;
import edu.mcw.GeneralSurgery.Network.GenericNetworkCallback;
import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Home.MainActivity;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity {

    TextView email;
    TextView name;
    Button sync;
    Switch searchToggle;
    TextView lastUpdated;

    @Inject
    APIHelper apiHelper;
    Context context;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((MainApplication)getApplicationContext()).getAppComponent().inject(this);

        context = this;

        setTitle(getResources().getString(R.string.settings));

        lastUpdated = findViewById(R.id.lastUpdated);
        email = findViewById(R.id.settingEmail);
        name = findViewById(R.id.settingName);
        sync = findViewById(R.id.synnButton);
        searchToggle = findViewById(R.id.searchToggleButton2);

        if (sharedPreferencesHelper.getIsComprehensiveSearch()){
                searchToggle.setChecked(true);
        }else{
                searchToggle.setChecked(false);
        }

        email.setText(sharedPreferencesHelper.getEmail());

        name.setText(sharedPreferencesHelper.getName());

        lastUpdated.setText(getResources().getString(R.string.last_updated,sharedPreferencesHelper.getLastUserFriendlyDate()));

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiHelper.downloadLocalData(new GenericNetworkCallback() {
                    @Override
                    public void onSuccess(String title, String message) {
                        Toast.makeText(context, getResources().getString(R.string.contentUpToDate),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String title, String message) {
                        Toast.makeText(context, getResources().getString(R.string.server_error_message),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        searchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isComprehensive) {
                if(isComprehensive){
                        sharedPreferencesHelper.setIsComprehensiveSearch(true);
                        //comprehensive search activated
                }else{
                        sharedPreferencesHelper.setIsComprehensiveSearch(false);
                         //comprehensive search deactivated
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_settings_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuLogoutButton) {
            sharedPreferencesHelper.setToken("");
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
