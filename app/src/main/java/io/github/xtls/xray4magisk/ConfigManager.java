package io.github.xtls.xray4magisk;

import io.github.xtls.xray4magisk.adapters.AppListAdapter;
import io.github.xtls.xray4magisk.util.MagiskHelper;
import io.github.xtls.xray4magisk.util.module.ProxyUtil;

import java.util.HashSet;

public class ConfigManager {
    public static String getModuleVersionCode(String moduleDir) {
        String cmd = "cat /data/adb/modules/" + moduleDir + "/module.prop | grep '^versionCode='";
        if (MagiskHelper.isMagiskLite) {
            cmd = "cat /data/adb/lite_modules/" + moduleDir + "/module.prop | grep '^versionCode='";
        }
        String result = MagiskHelper.execRootCmd(cmd);
        return "".equals(result) ? "" : result.split("=")[1];
    }

    public static String getModuleVersion(String moduleDir) {
        String cmd = "cat /data/adb/modules/" + moduleDir + "/module.prop | grep '^version='";
        if (MagiskHelper.isMagiskLite) {
            cmd = "cat /data/adb/lite_modules/" + moduleDir + "/module.prop | grep '^version='";
        }
        String result = MagiskHelper.execRootCmd(cmd);
        return "".equals(result) ? "" : result.split("=")[1];
    }

    public static boolean setProxyList(HashSet<AppListAdapter.AppInfo> checkedList, boolean whiteListMode) {
        HashSet<Integer> s = new HashSet<>();
        if(checkedList.isEmpty()){
            return ProxyUtil.setAppidList();
        }
        for (AppListAdapter.AppInfo info : checkedList) {
            s.add(info.applicationInfo.uid);
        }
        return whiteListMode ? ProxyUtil.setAppidList(s, "pick") : ProxyUtil.setAppidList(s, "bypass");
    }

    public static HashSet<Integer> getProxyList() {
        return ProxyUtil.getAppidList();
    }

    public static boolean renewTproxy() {
        return ProxyUtil.renewTproxy();
    }

    public static boolean isProxying() {
        return ProxyUtil.isProxying();
    }

    public static boolean startProxy() {
        return ProxyUtil.start();
    }

    public static boolean stopProxy() {
        return ProxyUtil.stop();
    }
}
