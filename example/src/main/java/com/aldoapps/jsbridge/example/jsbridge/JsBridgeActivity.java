package com.aldoapps.jsbridge.example.jsbridge;

import com.aldoapps.jsbridge.BridgeWebView;
import com.aldoapps.jsbridge.DefaultHandler;
import com.aldoapps.jsbridge.example.BaseActivity;
import com.aldoapps.jsbridge.example.R;
import com.aldoapps.jsbridge.example.jsbridge.pojo.Location;
import com.aldoapps.jsbridge.example.jsbridge.pojo.User;
import com.aldoapps.jsbridge.interfaces.BridgeHandler;
import com.aldoapps.jsbridge.interfaces.CallBackFunction;
import com.squareup.moshi.Moshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import butterknife.BindView;
import butterknife.OnClick;

public class JsBridgeActivity extends BaseActivity {

    private final String TAG = "JsBridgeActivity";

    @BindView(R.id.webView)
    BridgeWebView webView;

    private int RESULT_CODE = 0;

    private ValueCallback<Uri> uploadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType,
                String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                uploadMessage = uploadMsg;
                pickFile();
            }
        });

        webView.loadUrl("file:///android_asset/demo-js-bridge.html");

        webView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });

        User user = new User();
        Location location = new Location();
        location.setAddress("SDU");
        user.setLocation(location);
        user.setName("John");

        webView.callHandler("functionInJs",
            new Moshi.Builder().build().adapter(User.class).toJson(user), new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    Log.d("asdf", "hello: " + data);
                }
            });

        webView.send("hello");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_js_bridge;
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (uploadMessage == null || resultCode != RESULT_OK) return;

        if (requestCode == RESULT_CODE) {
            Uri result = intent.getData();
            uploadMessage.onReceiveValue(result);
            uploadMessage = null;
        }
    }

    @OnClick(R.id.button)
    public void onBtnClick() {
        webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

            @Override
            public void onCallBack(String data) {
                Log.i(TAG, "reponse data from js " + data);
            }

        });
    }

}
