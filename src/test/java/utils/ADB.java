package utils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class ADB {
    private static Logger LOGGER = Logger.getLogger(ADB.class);
    private String deviceId;

    public ADB(String deviceId) {
        this.deviceId = deviceId;
    }

    private static String command(String command) {
        LOGGER.debug("Formatting ADB Command: " + command);
        if (!command.startsWith("adb")) throw new RuntimeException("This method is designed to run ADB commands only!");
        LOGGER.debug("Formatted ADB Command: " + command);
        String output = CommonUtils.executeCommand(command);
        LOGGER.debug("Output of the ADB Command: " + output);
        if (output == null) return "";
        else return output.trim();
    }

    public static void killServer() {
        command("adb kill-server");
    }

    public static void startServer() {
        command("adb start-server");
    }

    @SuppressWarnings("unchecked")
    public static ArrayList getConnectedDevices() {
        ArrayList devices = new ArrayList();
        String output = command("adb devices");
        for (String line : output.split("\n")) {
            line = line.trim();
            if (line.endsWith("device")) devices.add(line.replace("device", "").trim());
        }
        return devices;
    }

    public String getForegroundActivity() {
        return command("adb -s " + deviceId + " shell dumpsys window windows | grep mCurrentFocus");
    }

    public String getAndroidVersionAsString() {
        String output = command("adb -s " + deviceId + " shell getprop ro.build.version.release");
        if (output.length() == 3) output += ".0";
        return output;
    }

    public int getAndroidVersion() {
        return Integer.parseInt(getAndroidVersionAsString().replaceAll("\\.", ""));
    }

    public ArrayList getInstalledPackages() {
        ArrayList packages = new ArrayList();
        String[] output = command("adb -s " + deviceId + " shell pm list packages").split("\n");
        for (String packageID : output) packages.add(packageID.replace("package:", "").trim());
        return packages;
    }

    public void openAppsActivity(String packageID, String activityID) {
        command("adb -s " + deviceId + " shell am start -c api.android.intent.category.LAUNCHER -a api.android.intent.action.MAIN -n " + packageID + "/" + activityID);
    }

    public void clearAppsData(String packageID) {
        command("adb -s " + deviceId + " shell pm clear " + packageID);
    }

    public void forceStopApp(String packageID) {
        command("adb -s " + deviceId + " shell am force-stop " + packageID);
    }

    public void installApp(String apkPath) {
        command("adb -s " + deviceId + " install " + apkPath);
    }

    public void uninstallApp(String packageID) {
        command("adb -s " + deviceId + " uninstall " + packageID);
    }

    public void clearLogBuffer() {
        command("adb -s " + deviceId + " shell -c");
    }

    public void pushFile(String source, String target) {
        command("adb -s " + deviceId + " push " + source + " " + target);
    }

    public void pullFile(String source, String target) {
        command("adb -s " + deviceId + " pull " + source + " " + target);
    }

    public void deleteFile(String target) {
        command("adb -s " + deviceId + " shell rm " + target);
    }

    public void moveFile(String source, String target) {
        command("adb -s " + deviceId + " shell mv " + source + " " + target);
    }

    public void takeScreenshot(String target) {
        command("adb -s " + deviceId + " shell screencap " + target);
    }

    public void rebootDevice() {
        command("adb -s " + deviceId + " reboot");
    }

    public String getDeviceModel() {
        return command("adb -s " + deviceId + " shell getprop ro.product.model");
    }

    public String getDeviceSerialNumber() {
        return command("adb -s " + deviceId + " shell getprop ro.serialno");
    }

    public String getDeviceCarrier() {
        return command("adb -s " + deviceId + " shell getprop gsm.operator.alpha");
    }

    @SuppressWarnings("unchecked")
    public ArrayList getLogcatProcesses() {
        String[] output = command("adb -s " + deviceId + " shell top -n 1 | grep -i 'logcat'").split("\n");
        ArrayList processes = new ArrayList();
        for (String line : output) {
            processes.add(line.split(" ")[0]);
            processes.removeAll(Arrays.asList("", null));
        }
        return processes;
    }

    @SuppressWarnings("unchecked")
    public Object startLogcat(final String logID, final String grep) {
        ArrayList pidBefore = getLogcatProcesses();

        Thread logcat = new Thread(new Runnable() {
            public void run() {
                if (grep == null)
                    command("adb -s " + deviceId + " shell logcat -v threadtime > /sdcard/" + logID + ".txt");
                else
                    command("adb -s " + deviceId + " shell logcat -v threadtime | grep -i '" + grep + "'> /sdcard/" + logID + ".txt");
            }
        });
        logcat.setName(logID);
        logcat.start();
        logcat.interrupt();

        ArrayList pidAfter = getLogcatProcesses();
        Timer timer = new Timer();
        timer.start();
        while (!timer.isExpiredIn(5)) {
            if (pidBefore.size() > 0) pidAfter.removeAll(pidBefore);
            if (pidAfter.size() > 0) break;
            pidAfter = getLogcatProcesses();
        }

        if (pidAfter.size() == 1) return pidAfter.get(0);
        else if (pidAfter.size() > 1)
            throw new RuntimeException("Multiple logcat processes were started when only one was expected!");
        else throw new RuntimeException("Failed to start logcat process!");
    }

    public void stopLogcat(Object PID) {
        command("adb -s " + deviceId + " shell kill " + PID);
    }
}
