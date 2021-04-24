package io.github.xtls.xray4magisk.util;

import java.util.HashSet;

public class ProxyListUtil {
    public static boolean isWhiteListMode() {
        String cmd = "cat /data/adb/xray/appid.list | grep -o 'pick'";
        return "pick".equals(MagiskHelper.execRootCmd(cmd));
    }

    public static HashSet<Integer> getAppidList() {
        HashSet<Integer> s = new HashSet<>();
        String cmd = "cat /data/adb/xray/appid.list | grep -vE 'pick|bypass|ALL'";
        String result = MagiskHelper.execRootCmd(cmd);
        if("".equals(result)){
            return s;
        }
        String[] appIds = result.split("\\s+");
        for (String i : appIds) {
            s.add(Integer.parseInt(i));
        }
        return s;
    }

    public static boolean setAppidList() {
        if (MagiskHelper.execRootCmdSilent("echo ALL" + " > /data/adb/xray/appid.list") == -1) {
            return false;
        }
        return true;
    }

    public static boolean setAppidList(HashSet<Integer> s, String mode) {
        if (MagiskHelper.execRootCmdSilent("echo " + mode + " > /data/adb/xray/appid.list") == -1) {
            return false;
        }
        for (int i : s) {
            if (MagiskHelper.execRootCmdSilent("echo " + i + " >> /data/adb/xray/appid.list") == -1) {
                return false;
            }
        }
        return true;
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
        String cmd = "if [ -f /data/adb/modules/xray4magisk/disable ] ; then exit 1;fi";
        if (MagiskHelper.isMagiskLite) {
            cmd = "if [ -f /data/adb/lite_modules/xray4magisk/disable ] ; then exit 1;fi";
        }
        return MagiskHelper.execRootCmdSilent(cmd) != 1;
    }
}
