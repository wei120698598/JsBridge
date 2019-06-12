package com.shuxin.bridge.interfaces;

public interface JsonConvert {
    <T> T json2Object(String jsonString, Class<T> t);

    <T> String object2Json(T jsonObject);
}
