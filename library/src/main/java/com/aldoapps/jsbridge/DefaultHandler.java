package com.aldoapps.jsbridge;

import com.aldoapps.jsbridge.interfaces.BridgeHandler;
import com.aldoapps.jsbridge.interfaces.CallBackFunction;

public class DefaultHandler implements BridgeHandler {

    String TAG = "DefaultHandler";

    @Override
    public void handler(String data, CallBackFunction function) {
        if (function != null) {
            function.onCallBack("DefaultHandler response data");
        }
    }

}
