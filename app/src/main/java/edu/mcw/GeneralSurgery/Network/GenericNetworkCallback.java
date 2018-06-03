package edu.mcw.GeneralSurgery.Network;

/**
 * Created by arham on 2/8/18.
 */

public interface GenericNetworkCallback {

    void onSuccess(String title, String message);

    void onError(String title, String message);

}
