package io.github.xtls.xray4magisk.util.module;

import io.github.xtls.xray4magisk.util.MagiskHelper;

import java.util.HashSet;

public class ProxyUtil {
    public static boolean isWhiteListMode() {
        return "pick".equals(MagiskHelper.execRootCmd("cat /data/adb/xray/appid.list | grep -o 'pick'"));
    }

    public static HashSet<Integer> getAppidList() {
        HashSet<Integer> s = new HashSet<>();
        String cmd = "cat /data/adb/xray/appid.list | grep -vE 'pick|bypass|ALL'";
        String result = MagiskHelper.execRootCmd(cmd);
        if ("".equals(result)) {
            return s;
        }
        String[] appIds = result.split("\\s+");
        for (String i : appIds) {
            s.add(Integer.parseInt(i));
        }
        return s;
    }

    public static boolean setAppidList() {
        return MagiskHelper.execRootCmdSilent("echo ALL" + " > /data/adb/xray/appid.list") != -1;
    }

    public static boolean setAppidList(HashSet<Integer> s, String mode) {
        StringBuilder cmd = new StringBuilder("echo \"");
        cmd.append(mode).append("\\n");
        for (int i : s) {
            cmd.append(i).append("\\n");
        }
        cmd.append("\" > /data/adb/xray/appid.list");
        return MagiskHelper.execRootCmdSilent(cmd.toString().trim()) != -1;
    }

    public static boolean restartXrayService() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.service restart") != -1;
    }

    public static boolean startXrayService() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.service start") != -1;
    }

    public static boolean stopXrayService() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.service stop") != -1;
    }

    public static boolean renewTproxy() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.tproxy renew") != -1;
    }

    public static boolean enableTproxy() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.tproxy enable") != -1;
    }

    public static boolean disableTproxy() {
        return MagiskHelper.execRootCmdSilent("/data/adb/xray/scripts/xray.tproxy disable") != -1;
    }

    public static boolean start() {
        String cmd = "rm -rf /data/adb/modules/xray4magisk/disable";
        if (MagiskHelper.isMagiskLite) {
            cmd = "rm -rf /data/adb/lite_modules/xray4magisk/disable";
        }
        return MagiskHelper.execRootCmdSilent(cmd) != -1;
    }

    public static boolean stop() {
        String cmd = "touch /data/adb/modules/xray4magisk/disable";
        if (MagiskHelper.isMagiskLite) {
            cmd = "touch /data/adb/lite_modules/xray4magisk/disable";
        }
        return MagiskHelper.execRootCmdSilent(cmd) != -1;
    }

    public static boolean isProxying() {
        return MagiskHelper.execRootCmdSilent("if [ -f /data/adb/xray/run/xray.pid ] ; then exit 1;fi") == 1;
    }
}
