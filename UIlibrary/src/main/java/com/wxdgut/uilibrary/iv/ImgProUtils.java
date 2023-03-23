package com.wxdgut.uilibrary.iv;

import java.lang.reflect.Method;

/**
 * ImageViewPro的初始化工具类
 */
public class ImgProUtils {
    private static AppConfig appConfig;

    public static synchronized void init(AppConfig config) {
        if (appConfig != null) {
            throw new IllegalStateException("ImageViewPro has already been initialized");
        }

        if (!validateAppConfig(config)) {
            throw new IllegalArgumentException("Invalid AppConfig object");
        }

        appConfig = config;
    }

    public static int getDefaultColor() {
        if (appConfig == null) {
            throw new NullPointerException("ImageViewPro has not been initialized");
        }

        return appConfig.getDefaultColor();
    }

    private static boolean validateAppConfig(AppConfig appConfig) {
        if (appConfig == null) {
            return false;
        }

        try {
            appConfig.getDefaultColor(); // 尝试调用方法以验证是否存在
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}


/*
public class ImgProUtils {
    private static AppConfig appConfig;

    public static void init(AppConfig config) {
        if (!validateAppConfig(config)) {
            throw new IllegalArgumentException("Invalid AppConfig object");
        }

        appConfig = config;

        // 可以在这里执行其他初始化操作
    }

    public static int getDefaultColor() {
        if (appConfig == null) {
            throw new IllegalStateException("ImageViewPro has not been initialized");
        }

        return appConfig.getDefaultColor();
    }

    private static boolean validateAppConfig(AppConfig appConfig) {
        if (appConfig == null) {
            return false;
        }

        try {
            //获取 AppConfig 接口的实现对象中名为 getDefaultColor() 的方法
            Method method = appConfig.getClass().getMethod("getDefaultColor");
            //如果返回的是String类型，则写String.class，boolean类型则写boolean.class
            if (method.getReturnType() != int.class) {
                return false;
            }
        } catch (NoSuchMethodException e) {
            return false;
        }

        return true;
    }
}
 */
