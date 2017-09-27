package com.aldoapps.jsbridge;

import com.aldoapps.jsbridge.interfaces.BridgeHandler;
import com.aldoapps.jsbridge.interfaces.CallBackFunction;

import android.support.annotation.NonNull;

public class DefaultHandler implements BridgeHandler {

    @Override
    public void handler(String data, @NonNull CallBackFunction function) {
        function.onCallBack("DefaultHandler response data");
    }

}
