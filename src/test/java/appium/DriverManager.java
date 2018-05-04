package appium;

import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import utils.CommonUtils;

import java.io.File;

public class DriverManager {
    private static final Logger LOGGER = Logger.getLogger(AppiumManager.class);

    private static ThreadLocal<AndroidDriver> androidDriverThreadLocal = new ThreadLocal<>();

    public AndroidDriver createAndroidDriver(DriverService driverService) {
        AndroidDriver androidDriver;
        File app;

        if (CommonUtils.hasText(System.getProperty("apkFile"))) {
            app = new File(System.getProperty("apkFile"));
        } else {
            app = new File(CommonUtils.getApkFile());
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(AppiumConstants.NEW_COMMAND_TIMEOUT, System.getProperty("newCommandTimeout"));
        capabilities.setCapability(AppiumConstants.ANDROID_DEVICE_READY_TIMEOUT, System.getProperty("androidDeviceReadyTimeout"));
        capabilities.setCapability(AppiumConstants.AUTO_GRANT_PERMISSIONS, System.getProperty("autoGrantPermissions"));
        capabilities.setCapability(AppiumConstants.APPLICATION, app.getAbsolutePath());
        capabilities.setCapability(AppiumConstants.APP_PACKAGE, System.getProperty("applicationPackage"));
        capabilities.setCapability(AppiumConstants.APP_ACTIVITY, System.getProperty("applicationActivity"));
        capabilities.setCapability(AppiumConstants.APP_WAIT_ACTIVITY, System.getProperty("applicationWaitActivity"));
        androidDriver = new AndroidDriver(driverService.getUrl(), capabilities);
        androidDriverThreadLocal.set(androidDriver);
        return androidDriver;
    }

    public AndroidDriver getAndroidDriver() {
        if(androidDriverThreadLocal.get() != null) {
            return androidDriverThreadLocal.get();
        } else {
            LOGGER.error("Android driver is not initialized, nothing to get");
        }
        return null;
    }

    public void killDriver() {
        if (androidDriverThreadLocal.get() != null) {
            LOGGER.info("Killing android driver >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            androidDriverThreadLocal.get().quit();
        } else {
            LOGGER.error("Android driver is not initialized, nothing to kill");
        }
    }
}
