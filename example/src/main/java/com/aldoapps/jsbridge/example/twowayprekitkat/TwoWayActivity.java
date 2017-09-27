package com.aldoapps.jsbridge.example.twowayprekitkat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.aldoapps.jsbridge.example.BaseActivity;
import com.aldoapps.jsbridge.example.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by aldo on 9/27/17.
 */

public class TwoWayActivity extends BaseActivity implements TwoWayInterface {

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.et_number)
    EditText etNumber;

    @BindView(R.id.tv_result)
    TextView tvResult;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/demo-two-way-prekitkat.html");
        webView.addJavascriptInterface(this, "TwoWayInterface");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_two_way;
    }

    @OnClick(R.id.btn_calculate)
    void onCalculateClick() {
        String number = etNumber.getText().toString();
        webView.loadUrl("javascript:calculateNumber(\"" + number + "\")");
    }

    @Override
    @JavascriptInterface
    public void onCalculationFinished(String result) {
        tvResult.setText(result);
    }

}
