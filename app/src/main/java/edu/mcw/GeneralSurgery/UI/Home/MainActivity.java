package edu.mcw.GeneralSurgery.UI.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.Network.APIHelper;
import edu.mcw.GeneralSurgery.Network.AuthenticateNetworkCallback;
import edu.mcw.GeneralSurgery.Network.GenericNetworkCallback;
import edu.mcw.GeneralSurgery.R;
import edu.mcw.GeneralSurgery.UI.Topic.TopicActivity;
import edu.mcw.GeneralSurgery.dagger.MainApplication;
import edu.mcw.GeneralSurgery.models.SharedPreferencesHelper;

public class MainActivity extends FragmentActivity implements  LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, ForgotPassword.OnFragmentInteractionListener{

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Inject //this is used for dependency injection. Once an injected object is instantiated, it can be used elsewhere without another initialization.
    APIHelper apiHelper;
    Context context;
    ProgressBar progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ((MainApplication)getApplicationContext()).getAppComponent().inject(this);

        context = this;

        progressBar = findViewById(R.id.mainProgressBar);

        fragmentManager = getSupportFragmentManager();

        sharedPreferencesHelper.putString(getResources().getString(R.string.lastSearch),"");

        Login();

    }


    @Override
    public void onBackPressed() {
    //this helps navigate back from a register or forgot password fragment back to the login fragment.
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public void loadLoginFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit); //this sets our custom transition animations.

        fragmentTransaction.replace(R.id.homeFragmentContainer, new LoginFragment());

        fragmentTransaction.commit();

    }

    public void loadRegisterFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        fragmentTransaction.replace(R.id.homeFragmentContainer, new RegisterFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }

    public void loadForgotPasswordFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        fragmentTransaction.replace(R.id.homeFragmentContainer, new ForgotPassword()).addToBackStack(null);

        fragmentTransaction.commit();
    }


    @Override
    public void onLoginButtonInteraction(String email, String password) {
        showLoading();
        apiHelper.Authenticate(email, password, new AuthenticateNetworkCallback() {
            @Override
            public void onSuccess(int id, String name, String email, String token, String title, String message) {

                sharedPreferencesHelper.setID(id);
                sharedPreferencesHelper.setName(name);
                sharedPreferencesHelper.setEmail(email);
                sharedPreferencesHelper.setToken(token);

                sharedPreferencesHelper.putString(getResources().getString(R.string.api_token), token);

                hideLoading();

                Intent intent = new Intent(MainActivity.this, TopicActivity.class);

                startActivity(intent);

            }

            @Override
            public void onError(String title, String message) {
                hideLoading();
                toast(getResources().getString(R.string.ok));
            }
        });

    }

    @Override
    public void onLoginToRegisterInteraction() {
        loadRegisterFragment();
    }

    @Override
    public void onLoginToForgotInteraction() {
        loadForgotPasswordFragment();
    }

    @Override
    public void onRegisterButtonInteraction(String email, String name) {

        apiHelper.Register(email, name, new GenericNetworkCallback() {
            @Override
            public void onSuccess(String title, String message) {
                alert(title, message, getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onRegisterToLoginInteraction();
                    }
                });
            }

            @Override
            public void onError(String title, String message) {
                toast(title);
            }
        });

    }

    public void onRegisterToLoginInteraction() {
         loadLoginFragment();
    }

    public void toast(String title) {
        Toast.makeText(this, title,
                Toast.LENGTH_LONG).show();
    }

    public void alert(String title, String message, String buttonText, @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title).setMessage(message);
        builder.setNegativeButton(buttonText, onClickListener);
        builder.create().show();
    }

    @Override
    public void onForgotButtonInteraction(String email) {
        apiHelper.Forgot(email, new GenericNetworkCallback() {
            @Override
            public void onSuccess(String title, String message) {
                toast(title);
            }

            @Override
            public void onError(String title, String message) {
                toast(title);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void Login() {
        if (sharedPreferencesHelper.getToken().isEmpty()) {//this checks if we have a token onboard and if we dont, just load the login fragment.
            loadLoginFragment();
            return;

        } else {
            //if token is detected on device, we try to authenticate the user and if successful, we log them in.
            String token = sharedPreferencesHelper.getToken();
            apiHelper.Authenticate(token, new AuthenticateNetworkCallback() {
                @Override
                public void onSuccess(int id, String name, String email, String token, String title, String message) {
                    sharedPreferencesHelper.setToken(token);
                    Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String title, String message) {
                    toast(title);
                    loadLoginFragment();
                }
            });

        }
    }
}
