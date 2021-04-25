package io.github.xtls.xray4magisk;

import io.github.xtls.xray4magisk.adapters.AppListAdapter;
import io.github.xtls.xray4magisk.util.MagiskHelper;
import io.github.xtls.xray4magisk.util.module.ProxyUtil;

import java.util.HashSet;

public class ConfigManager {

    public static String getModuleVersionCode() {
        String cmd = "cat /data/adb/modules/xray4magisk/module.prop | grep '^versionCode='";
        if (MagiskHelper.IS_MAGISK_LITE) {
            cmd = "cat /data/adb/lite_modules/xray4magisk/module.prop | grep '^versionCode='";
        }
        String result = MagiskHelper.execRootCmd(cmd);
        return "".equals(result) ? "" : result.split("=")[1];
    }

    public static String getModuleVersion() {
        String cmd = "cat /data/adb/modules/xray4magisk/module.prop | grep '^version='";
        if (MagiskHelper.IS_MAGISK_LITE) {
            cmd = "cat /data/adb/lite_modules/xray4magisk/module.prop | grep '^version='";
        }
        String result = MagiskHelper.execRootCmd(cmd);
        return "".equals(result) ? "" : result.split("=")[1];
    }
}
