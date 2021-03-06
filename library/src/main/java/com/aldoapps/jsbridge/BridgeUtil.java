package com.aldoapps.jsbridge;

import android.content.Context;
import android.webkit.WebView;

import com.aldoapps.jsbridge.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BridgeUtil {

    public final static String JAVASCRIPT_STR = "javascript:";

    final static String YY_OVERRIDE_SCHEMA = "yy://";
    // yy://return/{function}/returncontent

    final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/"; // Format

    final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";

    final static String EMPTY_STR = "";

    final static String UNDERLINE_STR = "_";

    final static String SPLIT_MARK = "/";

    final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";

    final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge" +
            "._handleMessageFromNative('%s');";

    final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge" +
            "._fetchQueue();";

    public static String parseFunctionName(String jsUrl) {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    public static String getDataFromReturnUrl(String url) {
        if (url.startsWith(YY_FETCH_QUEUE)) {
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
        }

        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);

        if (functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            return sb.toString();
        }
        return null;
    }

    public static String getFunctionFromReturnUrl(String url) {
        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);
        if (functionAndData.length >= 1) {
            return functionAndData[0];
        }
        return null;
    }

    /**
     * The js file will be injected as the first script reference
     */
    public static void webViewLoadJs(WebView view, String url) {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + url + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        view.loadUrl("javascript:" + js);
    }

    public static void webViewLoadLocalJs(WebView view, String path) {
        String jsContent = convertAssetsToString(view.getContext(), path);
        view.loadUrl("javascript:" + jsContent);
    }

    private static String convertAssetsToString(Context context, String urlStr) {
        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            stream = context.getAssets().open(urlStr);
            reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.matches("^\\s*//.*")) builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(stream, reader);
        }

        return builder.toString();
    }
}
