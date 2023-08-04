package com.wxdgut.uilibrary.iv;

import com.wxdgut.uilibrary.R;

/**
 * ImageViewPro的初始化工具类
 */
public class ColorConfigUtils {
    private static AppConfig appConfig;

    public static synchronized boolean init(AppConfig config) {
        if (appConfig != null) {
            //throw new IllegalStateException("ImageViewPro has already been initialized");
            return false;
        }

        if (!validateAppConfig(config)) {
            //throw new IllegalArgumentException("Invalid AppConfig object");
            return false;
        }

        appConfig = config;
        return true;
    }

    private static boolean isInitialized() {
        return appConfig != null;
    }

    public static int getDefaultColorId() {
        if (!isInitialized()) {
            return R.color.theme_color;
        }
        return appConfig.getDefaultColorId();
    }

    public static int getDefaultImgColorId() {
        if (!isInitialized()) {
            return R.color.theme_img;
        }
        return appConfig.getDefaultImgColorId();
    }

    public static int getDefaultSwitchOnColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_on;
        }
        return appConfig.getDefaultSwitchOnColorId();
    }

    public static int getDefaultSwitchOffColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_off;
        }
        return appConfig.getDefaultSwitchOffColorId();
    }

    private static boolean validateAppConfig(AppConfig appConfig) {
        if (appConfig == null) {
            return false;
        }

        try {
            // 尝试调用方法以验证是否存在
            appConfig.getDefaultColorId();
            appConfig.getDefaultImgColorId();
            appConfig.getDefaultSwitchOnColorId();
            appConfig.getDefaultSwitchOffColorId();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}