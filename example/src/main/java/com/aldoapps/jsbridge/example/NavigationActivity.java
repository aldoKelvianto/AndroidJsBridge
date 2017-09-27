package com.aldoapps.jsbridge.example;

import android.content.Intent;

import com.aldoapps.jsbridge.example.androidtojs.AndroidToJsActivity;
import com.aldoapps.jsbridge.example.jsbridge.JsBridgeActivity;
import com.aldoapps.jsbridge.example.jsinterface.JsInterfaceActivity;
import com.aldoapps.jsbridge.example.twowayprekitkat.TwoWayActivity;

import butterknife.OnClick;

/**
 * Created by aldo on 9/26/17.
 */

public class NavigationActivity extends BaseActivity {

    @OnClick(R.id.btn_js_interface)
    void onJsInterfaceClick() {
        start(JsInterfaceActivity.class);
    }

    @OnClick(R.id.btn_js_bridge)
    void onJsBridgeClick() {
        start(JsBridgeActivity.class);
    }

    @OnClick(R.id.btn_android_to_js)
    void onAndroidToJsClick() {
        start(AndroidToJsActivity.class);
    }

    @OnClick(R.id.btn_two_way)
    void onTwoWayClick() {
        start(TwoWayActivity.class);
    }

    private void start(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_navigation;
    }
}
