package com.aldoapps.jsbridge.interfaces;

public interface WebViewJavascriptBridge {

    void send(String data);

    void send(String data, CallBackFunction responseCallback);
}
