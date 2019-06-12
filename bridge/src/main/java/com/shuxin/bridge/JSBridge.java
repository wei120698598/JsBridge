package com.shuxin.bridge;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;

import com.shuxin.bridge.interfaces.JsonConvert;
import com.shuxin.bridge.model.RequestBody;
import com.shuxin.bridge.model.ResponseBody;
import com.shuxin.bridge.store.MethodModel;
import com.shuxin.bridge.store.MethodStore;
import com.shuxin.bridge.utils.Constants;
import com.shuxin.bridge.utils.JSBridgeHelp;
import com.shuxin.bridge.utils.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-28
 * @description JsBridge 核心处理库
 */
public class JSBridge {
    private List<Pair<String, String>> supportProtocols;
    private MethodStore methodStore;
    private JsonConvert jsonConvert;

    private JSBridge() {
    }

    public static class Builder {
        private ArrayList<Pair<String, String>> supportProtocols = new ArrayList<>();

        public JSBridge build() {
            JSBridge jsBridge = new JSBridge();
            jsBridge.supportProtocols = Collections.unmodifiableList(supportProtocols);
            return jsBridge;
        }

        public Builder addProtocol(String scheme, String host) {
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(host)) {
                supportProtocols.add(new Pair<>(scheme, host));
            }
            return this;
        }
    }

    public boolean dispatchJsMessage(String jsMessage) {
        Logger.logD("JS Message ：" + jsMessage);
        try {
            Uri jsMessageUri = Uri.parse(jsMessage);
            if (jsMessageUri != null && checkProtocol(jsMessageUri.getScheme(), jsMessageUri.getHost())) {
                handleJsMessage(jsMessageUri);
            }
        } catch (Exception e) {
            new IllegalArgumentException("Unexpected jsMessage \n" + e.getMessage()).printStackTrace();
        }
        return false;
    }

    private boolean checkProtocol(String scheme, String host) {
        if (supportProtocols != null && !TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(host)) {
            for (Pair<String, String> protocol : supportProtocols) {
                if (protocol.first.equals(scheme) && protocol.second.equals(host)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleJsMessage(Uri jsMessageUri) throws Exception {
        String query = jsMessageUri.getQuery();
        JSONObject jsonQuery = new JSONObject(query);
        if (!TextUtils.isEmpty(jsonQuery.optString(Constants.FIELD_METHOD_NAME, null))) {
            throw new IllegalArgumentException("not found " + Constants.FIELD_METHOD_NAME + " field from js message ");
        }
        if (jsonQuery.has(Constants.FIELD_REQUEST_ID) && !TextUtils.isEmpty(jsonQuery.optString(Constants.FIELD_REQUEST_ID))) {
            try {
                performJsRequestMessage(query);
            } catch (Exception e) {
                e.printStackTrace();
                ResponseBody.Data data = new ResponseBody.Data(false, e.getMessage());
                ResponseBody responseBody = new ResponseBody(jsonQuery.optString(Constants.FIELD_METHOD_NAME), jsonQuery.optString(Constants.FIELD_REQUEST_ID), data);
                String responseContent = JSBridgeHelp.buildJSUrl(jsonConvert.object2Json(responseBody));
            }
        } else if (jsonQuery.has(Constants.FIELD_RESPONSE_ID) && !TextUtils.isEmpty(jsonQuery.optString(Constants.FIELD_RESPONSE_ID))) {
            performJsResponseMessage(jsonQuery);
        } else {
            throw new IllegalArgumentException("unexpected js message for request body or response body :" + query);
        }
    }

    private void performJsRequestMessage(String jsRequestMessage) throws Exception {
        RequestBody requestBody = jsonConvert.json2Object(jsRequestMessage, RequestBody.class);
        if (requestBody == null) {
            throw new IllegalArgumentException("js message convert to request body failed");
        }
        MethodModel methodModel = methodStore.findMethodByName(requestBody.handlerName);
        if (methodModel == null) {
            throw new IllegalArgumentException(requestBody.handlerName + " method name not found");
        }
        Object[] args = {};
        methodModel.method.invoke(methodModel.instance.newInstance(), args);
    }

    private void performJsResponseMessage(JSONObject jsRequestMessage) {
        ResponseBody responseBody = JSBridgeHelp.convertJson2Body(new ResponseBody(), jsRequestMessage);
    }

}
