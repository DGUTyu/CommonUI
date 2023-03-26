package com.wxdgut.uilibrary.iv;

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

    public static int getDefaultColorId() {
        if (appConfig == null) {
            throw new NullPointerException("ImageViewPro has not been initialized");
        }

        return appConfig.getDefaultColorId();
    }

    private static boolean validateAppConfig(AppConfig appConfig) {
        if (appConfig == null) {
            return false;
        }

        try {
            appConfig.getDefaultColorId(); // 尝试调用方法以验证是否存在
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}