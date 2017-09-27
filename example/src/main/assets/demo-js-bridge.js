function showHtml() {
    document.getElementById("show").innerHTML = document.getElementsByTagName("html")[0].innerHTML;
}

function callAndroidMethod() {
    var usernameText = document.getElementById("usernameText").value;
    var passwordText = document.getElementById("passwordText").value;

    //send message to native
    var data = {id: 1, content: "This is a picture <img src=\"a.png\"/> test\r\nhahaha"};
    window.WebViewJavascriptBridge.send(
        data
        , function(responseData) {
            document.getElementById("show").innerHTML = "repsonseData from java, data = " + responseData
        }
    );
}

function callAndroidMethodWithParams() {
    var usernameText = document.getElementById("usernameText").value;
    var passwordText = document.getElementById("passwordText").value;

    //call native method
    window.WebViewJavascriptBridge.callHandler(
        'submitFromWeb'
        , {'param': '中文测试'}
        , function(responseData) {
            document.getElementById("show").innerHTML = "send get responseData from java, data = " + responseData
        }
    );
}

function bridgeLog(logContent) {
    document.getElementById("show").innerHTML = logContent;
}

function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener(
            'WebViewJavascriptBridgeReady'
            , function() {
                callback(WebViewJavascriptBridge)
            },
            false
        );
    }
}

connectWebViewJavascriptBridge(function(bridge) {
    bridge.init(function(message, responseCallback) {
        console.log('JS got a message', message);
        var data = {
            'Javascript Responds': '测试中文!'
        };
        console.log('JS responding with', data);
        responseCallback(data);
    });

    bridge.registerHandler("functionInJs", function(data, responseCallback) {
        document.getElementById("show").innerHTML = ("data from Java: = " + data);
        var responseData = "Javascript Says Right back aka!";
        responseCallback(responseData);
    });
})