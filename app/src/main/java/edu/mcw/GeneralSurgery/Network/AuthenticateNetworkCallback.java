package edu.mcw.GeneralSurgery.Network;

/**
 * Created by arham on 2/8/18.
 */

public interface AuthenticateNetworkCallback {

    void onSuccess(int id, String name, String email, String token, String title, String message);

    void onError(String title, String message);

}
