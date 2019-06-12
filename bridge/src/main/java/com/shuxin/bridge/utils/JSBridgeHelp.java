package com.shuxin.bridge.utils;

import com.shuxin.bridge.model.Body;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-05-27
 * @description 用于辅助JSBridge
 */
public class JSBridgeHelp {

    public static String buildJSUrl(String jsContent) {
        String jsUrl = "调用JS方法: " + "javascript:_JSNativeBridge._handleMessageFromNative('" + jsContent + "')";
        Logger.logD(jsUrl);
        return jsUrl;
    }

    /**
     * 解析Json到Body
     */
    public static <T extends Body> T convertJson2Body(T newInstance, JSONObject jsonObject) {
        try {
            Field[] fields = newInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isPrimitive() || type.isAssignableFrom(JSONObject.class) || type.isAssignableFrom(JSONArray.class)) {
                    field.set(newInstance, jsonObject.opt(field.getName()));
                }
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
