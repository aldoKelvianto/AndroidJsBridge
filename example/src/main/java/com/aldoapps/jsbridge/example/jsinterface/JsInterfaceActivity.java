package com.aldoapps.jsbridge.example.jsinterface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.aldoapps.jsbridge.example.BaseActivity;
import com.aldoapps.jsbridge.example.R;

import butterknife.BindView;

/**
 * Created by aldo on 9/26/17.
 */

public class JsInterfaceActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/demo-js-interface.html");
        webView.addJavascriptInterface(new CallbackInterface() {
            @Override
            @JavascriptInterface
            public void receiveMessageFromJs(String message) {
                Toast.makeText(JsInterfaceActivity.this, "r: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            @JavascriptInterface
            public void showAndroidToast(String toastMessage) {
                Toast.makeText(JsInterfaceActivity.this, "s: " + toastMessage, Toast.LENGTH_SHORT).show();
            }
        }, "MyJsInterface");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_js_interface;
    }
}
