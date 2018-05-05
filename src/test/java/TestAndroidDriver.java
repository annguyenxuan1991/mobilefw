import appium.AppiumManager;
import appium.AndroidDriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Platform;
import utils.CommonUtils;

public class TestAndroidDriver {
    public static void main(String[] arg) {
        CommonUtils.loadProperties("/src/test/resources");
        AppiumManager appiumManager = new AppiumManager();
        appiumManager.startAppiumServer(Platform.ANDROID);
        AndroidDriverManager driverManager = new AndroidDriverManager();
        AndroidDriver androidDriver1 = driverManager.createDriver(appiumManager.getUsedDeviceName(), appiumManager.getAppiumServerInstance());
        AndroidDriver androidDriver2 = driverManager.getDriver();
        boolean isAppiumRunning = appiumManager.isAppiumServerRunning();
        driverManager.killDriver();
        boolean isAppiumRunning2 = appiumManager.isAppiumServerRunning();
        appiumManager.stopAppiumServer();
        boolean isAppiumRunning3 = appiumManager.isAppiumServerRunning();

    }
}
