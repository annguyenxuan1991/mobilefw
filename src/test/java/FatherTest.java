import appium.AndroidDriverManager;
import appium.AppiumManager;
import org.testng.annotations.BeforeMethod;
import utils.CommonUtils;

abstract class FatherTest {
    AppiumManager appiumManager = AppiumManager.getInstance();
    AndroidDriverManager androidDriverManager = new AndroidDriverManager();

    @BeforeMethod
    public void setupMethod() {
        CommonUtils.loadProperties("/src/test/resources");
    }
}
