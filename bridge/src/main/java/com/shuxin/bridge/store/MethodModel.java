package com.shuxin.bridge.store;

import java.lang.reflect.Method;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-06-10
 * @description 方法、实体、参数
 */
public class MethodModel {
    public Method method;
    public Class instance;

    public MethodModel(Method method, Class instance) {
        this.method = method;
        this.instance = instance;
    }
}
