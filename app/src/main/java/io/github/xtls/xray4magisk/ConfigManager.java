package io.github.xtls.xray4magisk;

import io.github.xtls.xray4magisk.adapters.AppListAdapter;

import java.util.Collection;
import java.util.HashSet;

public class ConfigManager {
    public static String getModuleVersionCode() {
        // TODO: 2021/4/20
        return "20210514";
    }

    public static boolean setProxyList(HashSet<AppListAdapter.AppInfo> checkedList, boolean whiteListMode) {
        // TODO: 2021/4/20
        return true;
    }

    public static HashSet<AppListAdapter.AppInfo> getProxyList() {
        // TODO: 2021/4/20
        return new HashSet<AppListAdapter.AppInfo>();
    }
}
