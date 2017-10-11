package com.aldoapps.jsbridge;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {

    private BridgeWebView webView;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    @SuppressWarnings("deprecation")
    // shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
    // will call this method anyway
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) {
            webView.handlerReturnData(url);
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);

        for (Message message : webView.getStartupMessage()) {
            webView.dispatchMessage(message);
        }
        webView.clearStartupMessage();
    }

}