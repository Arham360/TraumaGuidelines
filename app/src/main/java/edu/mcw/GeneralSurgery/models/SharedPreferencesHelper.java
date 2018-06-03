package edu.mcw.GeneralSurgery.models;

import android.content.SharedPreferences;

import java.text.ParseException;

import javax.inject.Inject;

import edu.mcw.GeneralSurgery.Utilities.TimestampUtils;

/**
 * Created by cjsampon on 2/6/18.
 */

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void putInt(String key, int data) {
        sharedPreferences.edit().putInt(key,data).apply();
    }

    public void putString(String key, String data) {
        sharedPreferences.edit().putString(key,data).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key,0);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key,"");
    }

    public void setID(int id) {
        putInt("id", id);
    }

    public void setName(String name) {
        putString("name", name);
    }

    public void setEmail(String email) {
        putString("email", email);
    }

    public void setToken(String token) {
        putString("token", token);
    }

    public String getToken() {
        return getString("token");
    }

    public String getLastUpdatedDate() {
        return getString("lastUpdated");
    }

    public void setLastUpdatedDate(String time) {
        putString("lastUpdated", time);
    }

    public String getName() {
        return getString("name");
    }

    public String getEmail() {
        return getString ("email");
    }

    public String getLastUserFriendlyDate() {
        try {
            return TimestampUtils.friendlyDateFormat.format(TimestampUtils.dateFormat.parse(getLastUpdatedDate()));
        } catch (ParseException e) {
            return null;
        }
    }

    public void setIsComprehensiveSearch(boolean b) {
        sharedPreferences.edit().putBoolean("isComprehensive",b).apply();
    }

    public boolean getIsComprehensiveSearch() {
        return sharedPreferences.getBoolean("isComprehensive",false);
    }


}
