package com.shuxin.bridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.shuxin.bridge.utils.Logger;


/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-25
 * BridgeWebViewClient是通过拦截onJsPrompt方法来实现Js调用Native方法的
 * <p>
 * JS调用Native示例
 * JS请求信息：icourt://alpha?{"handlerName":"test1","params":{"age":10,"name":"wangwu"},"callbackId":"cb_3_1558956496148"}
 * Native响应消息：javascript:_JSNativeBridge._handleMessageFromNative('{"responseId":"cb_3_1558956496148","data":{"success":true,"values":{"content":[{"age":8,"name":"sdf"},{"age":5,"name":"sdf"}]}}}'
 * <p>
 * Native调用JS示例
 * Native请求信息：javascript:_JSNativeBridge._handleMessageFromNative('{"callbackId":"2_1558959159905","handlerName":"exam","params":{"test":"你好啊js","id":7}}')
 * JS响应消息：icourt://alpha?{"responseId":"2_1558959834158","data":{"success":false,"msg":"ok","values":{"msg":"js回调native"}}}
 */
public class BridgeWebViewClient extends WebChromeClient {

    private final JSBridge jsBridge;

    public BridgeWebViewClient() {
        jsBridge = new JSBridge.Builder()
                .addProtocol("icourt", "alpha")
                .build();
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Logger.logI("onJsPrompt url: " + message);
        Logger.logI("onJsPrompt message: " + message);
        if (jsBridge.dispatchJsMessage(message)) {
            /*必须得有这行代码，否则会阻塞当前h5页面*/
            result.cancel();
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
