package com.shuxin.bridge.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-28
 * @description JS或native响应体
 */
public class ResponseBody implements Body, Serializable {
    public String responseId;
    public Data data;
    public String handlerName;

    public ResponseBody(String responseId, String handlerName, Data data) {
        this.responseId = responseId;
        this.data = data;
        this.handlerName = handlerName;
    }

    public ResponseBody() {
    }

    public static class Data implements Serializable {
        public boolean success;
        public String msg;
        public JSONObject values;

        public Data() {
        }

        public Data(boolean success, String msg) {
            this.success = success;
            this.msg = msg;
        }

        public Data(boolean success, JSONObject values) {
            this.success = success;
            this.values = values;
        }
    }
}
