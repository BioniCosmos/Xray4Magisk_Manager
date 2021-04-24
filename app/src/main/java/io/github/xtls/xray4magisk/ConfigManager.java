package io.github.xtls.xray4magisk;

import io.github.xtls.xray4magisk.adapters.AppListAdapter;
import io.github.xtls.xray4magisk.util.MagiskHelper;
import io.github.xtls.xray4magisk.util.ProxyListUtil;

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
            return ProxyListUtil.setAppidList();
        }
        for (AppListAdapter.AppInfo info : checkedList) {
            s.add(info.applicationInfo.uid);
        }
        return whiteListMode ? ProxyListUtil.setAppidList(s, "pick") : ProxyListUtil.setAppidList(s, "bypass");
    }

    public static HashSet<Integer> getProxyList() {
        return ProxyListUtil.getAppidList();
    }

    public static boolean renewTproxy() {
        return ProxyListUtil.renewTproxy();
    }

    public static boolean isProxying() {
        return ProxyListUtil.isProxying();
    }

    public static boolean startProxy() {
        return ProxyListUtil.start();
    }

    public static boolean stopProxy() {
        return ProxyListUtil.stop();
    }
}
