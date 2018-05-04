import appium.AppiumManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.service.DriverService;
import utils.ADB;
import utils.CommonUtils;

import java.util.Properties;

public class Runner1 {
    public static void main(String[] args) {
        CommonUtils.loadProperties("/src/test/resources");
        AppiumManager appiumManager = new AppiumManager();
        DriverService service = appiumManager.startAppiumServer(Platform.ANDROID);
        int port = appiumManager.getUsedPort();
        boolean a0 = service.isRunning();
        boolean a = appiumManager.isAppiumServerRunning();
        boolean b = appiumManager.checkIfServerIsRunning(service.getUrl().getPort());

        appiumManager.killProcess("node.exe");
        boolean c = appiumManager.isAppiumServerRunning();

        appiumManager.stopAppiumServer();

        boolean d = appiumManager.isAppiumServerRunning();
    }
}
