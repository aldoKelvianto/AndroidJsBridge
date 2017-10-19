package com.aldoapps.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.aldoapps.jsbridge.interfaces.BridgeHandler;
import com.aldoapps.jsbridge.interfaces.CallBackFunction;
import com.aldoapps.jsbridge.interfaces.WebViewJavascriptBridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge {

    public static final String toLoadJs = "WebViewJavascriptBridge.js";

    private Map<String, CallBackFunction> responseCallbacks = new HashMap<>();

    private Map<String, BridgeHandler> messageHandlers = new HashMap<>();

    private BridgeHandler defaultHandler = new DefaultHandler();

    private List<Message> startupMessage = new ArrayList<>();

    private long uniqueId = 0;

    private CallBackFunction jsFetchQueueFromJavaCallback = new CallBackFunction() {
        @Override
        public void onCallBack(String data) {
            // deserializeMessage
            List<Message> messageList = Message.toArrayList(data);
            if (messageList.isEmpty()) return;

            for (Message message : messageList) {
                String responseId = message.getResponseId();
                // 是否是response
                if (!TextUtils.isEmpty(responseId)) {
                    CallBackFunction function = responseCallbacks.get(responseId);
                    String responseData = message.getResponseData();
                    function.onCallBack(responseData);
                    responseCallbacks.remove(responseId);
                } else {
                    final String callbackId = message.getCallbackId();

                    CallBackFunction responseFunction = !TextUtils.isEmpty(callbackId) ? new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            Message responseMsg = new Message();
                            responseMsg.setResponseId(callbackId);
                            responseMsg.setResponseData(data);
                            queueMessage(responseMsg);
                        }
                    } : new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {

                        }
                    };


                    BridgeHandler handler = !TextUtils.isEmpty(message.getHandlerName()) ?
                            messageHandlers.get(message.getHandlerName()) : defaultHandler;

                    handler.handler(message.getData(), responseFunction);
                }
            }
        }
    };

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BridgeWebView(Context context) {
        super(context);
        init();
    }

    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    public void clearStartupMessage() {
        startupMessage.clear();
    }

    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     *                if js message has handler name, it will be handled by named handlers
     *                registered by native
     */
    public void setDefaultHandler(BridgeHandler handler) {
        this.defaultHandler = handler;
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setWebViewClient(new BridgeWebViewClient(this));
    }

    void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        CallBackFunction f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.onCallBack(data);
            responseCallbacks.remove(functionName);
        }
    }

    @Override
    public void send(String data) {
        send(data, null);
    }

    @Override
    public void send(String data, CallBackFunction responseCallback) {
        doSend(null, data, responseCallback);
    }

    private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
        Message message = new Message();
        if (!TextUtils.isEmpty(data)) {
            message.setData(data);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT,
                    ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            responseCallbacks.put(callbackStr, responseCallback);
            message.setCallbackId(callbackStr);
        }
        if (!TextUtils.isEmpty(handlerName)) {
            message.setHandlerName(handlerName);
        }
        queueMessage(message);
    }

    private void queueMessage(Message message) {
        if (startupMessage != null) {
            startupMessage.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    void dispatchMessage(Message message) {
        String messageJson = message.toJson();
        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = String
                .format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        loadUrl(javascriptCommand);
    }

    void flushMessageQueue() {
        loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, jsFetchQueueFromJavaCallback);
    }

    public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
        loadUrl(jsUrl);
        responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
    }

    /**
     * register handler,so that javascript can call it
     */
    public void registerHandler(String handlerName, @NonNull BridgeHandler handler) {
        messageHandlers.put(handlerName, handler);
    }

    /**
     * call javascript registered handler
     */
    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
    }
}
