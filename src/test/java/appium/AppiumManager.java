package appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import utils.ADB;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class AppiumManager {
    private static final Logger LOGGER = Logger.getLogger(AppiumManager.class);

    private static AppiumManager instance;

    private static final ThreadLocal<DriverService> serviceThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<ADB> adbThreadLocal = new ThreadLocal<>();
    private static HashMap<String, Boolean> deviceMap;

    private AppiumManager() {
        if (deviceMap == null) {
            LOGGER.info("Get all connecting devices using adb >>>>>>>>>>>>>>> " + Thread.currentThread().getName());
            deviceMap = getConnectedDevices();
        }
    }

    public static AppiumManager getInstance() {
        if(instance == null) {
            instance = new AppiumManager();
        }
        return instance;
    }

    public DriverService startAppiumServer(Platform platform) {
        ADB adb = new ADB(getFreeDevice());
        DesiredCapabilities cap = new DesiredCapabilities();
        adbThreadLocal.set(adb);

        cap.setPlatform(platform);
        cap.setVersion(adb.getAndroidVersionAsString());
        cap.setCapability(AppiumConstants.DEVICE_NAME, adb.getDeviceModel());

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, AppiumConstants.ERROR)
                .usingAnyFreePort();
        return startAppiumServer(cap, builder);
    }

    public DriverService startAppiumServer(DesiredCapabilities cap, AppiumServiceBuilder builder) {
        cap.setCapability(AppiumConstants.NO_RESET, AppiumConstants.FALSE);
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder
                .usingDriverExecutable(new File(System.getProperty("nodeJsUrl")))
                .withIPAddress(System.getProperty("appiumServerUrl")));
        serviceThreadLocal.set(service);
        LOGGER.info("Start Appium server for device " + getUsedDeviceName() + " " + getUsedDeviceId() + " >>>>>>>>>>>>>>>>>>>");
        service.start();
        return service;
    }

    public DriverService getAppiumServerInstance() {
        return serviceThreadLocal.get();
    }

    public boolean isAppiumServerRunning() {
        return serviceThreadLocal.get().isRunning();
    }

    public int getUsedPort() {
        return serviceThreadLocal.get().getUrl().getPort();
    }

    public String getUsedDeviceId() {
        return adbThreadLocal.get().getDeviceId();
    }

    public String getUsedDeviceName() {
        return adbThreadLocal.get().getDeviceModel();
    }

    public void stopAppiumServer() {
        if (serviceThreadLocal.get() != null) {
            LOGGER.info("Uninstall application and stopping appium server for device " + getUsedDeviceName() + getUsedDeviceId() + " >>>>>>>>>>>>>>>>>>>");
            adbThreadLocal.get().uninstallApp(System.getProperty("unlockPackage"));
            adbThreadLocal.get().uninstallApp(System.getProperty("applicationPackage"));
            serviceThreadLocal.get().stop();
        } else {
            LOGGER.error("Appium server is not initialized, nothing to stop");
        }
    }

    public void removeAllAppiumServer() {
        LOGGER.info("Removing all appium server >>>>>>>>>>>>>>>>>>>>>>");
        adbThreadLocal.get().removeAllDeviceId();
        adbThreadLocal.remove();
        serviceThreadLocal.remove();
    }

    /**
     * Get all connected devices then put it into a hash map with Key = "device id"
     * AND Value = "true/false"
     *
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, Boolean> getConnectedDevices() {
        ArrayList<String> deviceList = ADB.getConnectedDevices();
        LOGGER.info("Getting all connected devices >>>>>>>>>>>>>>>>>>>>>>>");
        if (deviceList == null || deviceList.isEmpty()) {
            LOGGER.error("Not a single device is connected, please check your connection >>>>>>>>>>>>>>>>>>>");
            throw new NullPointerException();
        }
        LOGGER.info("We have " + deviceList.size() + " connected >>>>>>>>>>>>>>>>>>>>>>");
        HashMap<String, Boolean> deviceMap = new HashMap<>();
        for (String device : deviceList) {
            deviceMap.put(device, true);
        }
        return deviceMap;
    }

    private String getFreeDevice() {
        LOGGER.info("Get free device for thread: " + Thread.currentThread().getName() + " >>>>>>>>>>>>>>>>>>>>>");
        for (HashMap.Entry<String, Boolean> entry : deviceMap.entrySet()) {
            if (entry.getValue()) {
                entry.setValue(false);
                return entry.getKey();
            }
        }
        LOGGER.error("No device is free... so appium server of this thread could not start >>>>>>>>>>>>>>");
        throw new NullPointerException();
    }

    public void killProcess(String serviceName) {
        while (true) {
            try {
                if (isProcessRunning(serviceName)) {
                    String commandLine = "taskkill /f /IM " + serviceName;
                    LOGGER.debug("Execute command line: [" + commandLine + "]");
                    Runtime.getRuntime().exec(commandLine);
                    continue;
                }
            } catch (IOException var2) {
                LOGGER.error("Kill Process Error: ", var2);
            }
            return;
        }
    }

    private boolean isProcessRunning(String serviceName) {
        try {
            Process p = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            do {
                if ((line = reader.readLine()) == null) {
                    return false;
                }
            } while (!line.contains(serviceName));

            LOGGER.info(serviceName + " is running >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            return true;
        } catch (IOException var4) {
            LOGGER.error("Kill Process Error: ", var4);
            return false;
        }
    }

    public boolean checkIfServerIsRunning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        }
        return isServerRunning;
    }
}
