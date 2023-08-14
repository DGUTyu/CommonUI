package com.wxdgut.uilibrary.iv;

import com.wxdgut.uilibrary.R;
import com.wxdgut.uilibrary.dialog.CommonDialog;

/**
 * ImageViewPro的初始化工具类
 */
public class UIConfigUtils {
    private static UIConfig uiConfig;

    public static synchronized boolean init(UIConfig config) {
        if (uiConfig != null) {
            //throw new IllegalStateException("ImageViewPro has already been initialized");
            return false;
        }

        if (!validateAppConfig(config)) {
            //throw new IllegalArgumentException("Invalid AppConfig object");
            return false;
        }

        uiConfig = config;
        return true;
    }

    private static boolean isInitialized() {
        return uiConfig != null;
    }

    public static int getDefaultColorId() {
        if (!isInitialized()) {
            return R.color.theme_color;
        }
        return uiConfig.getDefaultColorId();
    }

    public static int getDefaultImgColorId() {
        if (!isInitialized()) {
            return R.color.theme_img;
        }
        return uiConfig.getDefaultImgColorId();
    }

    public static int getDefaultSwitchOnColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_on;
        }
        return uiConfig.getDefaultSwitchOnColorId();
    }

    public static int getDefaultSwitchOffColorId() {
        if (!isInitialized()) {
            return R.color.theme_switch_off;
        }
        return uiConfig.getDefaultSwitchOffColorId();
    }

    public static int getDefaultDialogLayoutId() {
        if (!isInitialized()) {
            return R.layout.dialog_fingerprint;
        }
        return uiConfig.getDefaultDialogLayoutId();
    }

    public static int getDefaultDialogAnimId() {
        if (!isInitialized()) {
            return CommonDialog.DEFAULT_ANIM;
        }
        return uiConfig.getDefaultDialogAnimId();
    }

    private static boolean validateAppConfig(UIConfig uiConfig) {
        if (uiConfig == null) {
            return false;
        }

        try {
            // 尝试调用方法以验证是否存在
            uiConfig.getDefaultColorId();
            uiConfig.getDefaultImgColorId();
            uiConfig.getDefaultSwitchOnColorId();
            uiConfig.getDefaultSwitchOffColorId();
            uiConfig.getDefaultDialogLayoutId();
            uiConfig.getDefaultDialogAnimId();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}