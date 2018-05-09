import appium.AppiumManager;
import appium.AndroidDriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.service.DriverService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.CommonUtils;

public class TestAndroidDriver extends FatherTest {

    @Test
    public void firstThread() {
        appiumManager.startAppiumServer(Platform.ANDROID);
        DriverService appiumDriverService = appiumManager.getAppiumServerInstance();
        System.out.println("Appium service instance [PORT]: "+ appiumDriverService.getUrl().getPort());
        System.out.println("Appium service instance [USER INFO]: "+ appiumDriverService.getUrl().getUserInfo());
        AndroidDriver androidDriver1 = androidDriverManager.createDriver(appiumManager.getADB(), appiumManager.getAppiumServerInstance());
        AndroidDriver androidDriver2 = androidDriverManager.getDriver();
        boolean isAppiumRunning = appiumManager.isAppiumServerRunning();
        androidDriverManager.killDriver();
        boolean isAppiumRunning2 = appiumManager.isAppiumServerRunning();
        appiumManager.stopAppiumServer();
        boolean isAppiumRunning3 = appiumManager.isAppiumServerRunning();
    }
}
