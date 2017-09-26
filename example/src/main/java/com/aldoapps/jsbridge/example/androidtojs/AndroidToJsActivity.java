package com.aldoapps.jsbridge.example.androidtojs;

import com.aldoapps.jsbridge.example.BaseActivity;
import com.aldoapps.jsbridge.example.R;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by aldo on 9/26/17.
 */

public class AndroidToJsActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.et_message)
    EditText etMessage;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/demo-android-to-js.html");
    }

    @OnClick(R.id.btn_submit_with_loadurl)
    void onSubmitWithLoadUrlClick() {
        String message = etMessage.getText().toString();
        webView.loadUrl("javascript:showMessageFromActivity(\"" + message + "\")");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_submit_with_evaljs)
    void onSubmitWithEvalJsClick() {
        String message = etMessage.getText().toString();
        webView.evaluateJavascript("showMessageFromActivityAndReturnValue(\"" + message + "\")",
            new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Toast.makeText(AndroidToJsActivity.this, "Received Value: " + value,
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_android_to_js;
    }
}
