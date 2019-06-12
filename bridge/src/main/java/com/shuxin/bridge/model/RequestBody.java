package com.shuxin.bridge.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-28
 * @description JS或者Native发起请求体
 */
public class RequestBody implements Body, Serializable {
    public String callbackId;
    public JSONObject params;
    public String handlerName;
}
