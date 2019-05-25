package com.shuxin.bridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-25
 * @description BridgeWebViewClient是通过拦截onJsPrompt方法来实现Js调用Native方法的
 */
public class BridgeWebViewClient extends WebChromeClient {

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        /*必须得有这行代码，否则会阻塞当前h5页面*/
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
