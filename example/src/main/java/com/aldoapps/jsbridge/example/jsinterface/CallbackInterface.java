package com.aldoapps.jsbridge.example.jsinterface;

/**
 * Created by aldo on 9/26/17.
 */

public interface CallbackInterface {

    void receiveMessageFromJs(String message);

    void showAndroidToast(String toastMessage);

}
