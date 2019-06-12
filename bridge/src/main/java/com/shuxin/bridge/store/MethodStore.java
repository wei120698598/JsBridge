package com.shuxin.bridge.store;

import android.util.ArrayMap;

import com.shuxin.bridge.annotation.JavaInterface4JS;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuxin.wei
 * @version v1.0.0
 * @date 2019-06-10
 * @description 缓存注册的Method
 */
public class MethodStore {
    private ArrayMap<String, MethodModel> javaMethods = new ArrayMap<>();
    private List<Class> javaInterfaceClass = new ArrayList<>();

    public List<Class> getJavaInterfaceClass() {
        return javaInterfaceClass;
    }

    public MethodModel findMethodByName(String methodName) {
        if (methodName == null) {
            return null;
        }
        MethodModel memoryMethod = javaMethods.get(methodName);
        if (memoryMethod != null) {
            return memoryMethod;
        }
        //只能遍历，因为不能确定入参，无法调用getMethod(name)
        for (Class interfaceClass : javaInterfaceClass) {
            Method[] methods = interfaceClass.getDeclaredMethods();
            for (Method method : methods) {
                JavaInterface4JS annotation = method.getAnnotation(JavaInterface4JS.class);
                if (annotation != null) {
                    MethodModel methodModel = new MethodModel(method, interfaceClass);
                    javaMethods.put(methodName, methodModel);
                    if (methodName.equals(annotation.value())) {
                        return methodModel;
                    }
                }
            }
        }
        return null;
    }
}
