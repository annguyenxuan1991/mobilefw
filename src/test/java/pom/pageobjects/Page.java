package pom.pageobjects;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

public class Page {
    private static final Logger LOGGER = Logger.getLogger(Page.class);

    private static Page instance;

    protected Page() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(AndroidDriver androidDriver, Class<T> clazz) {
        T page = null;
        if (instance == null) {
            instance = new Page();
        }
        try {
            page = clazz.newInstance();
            PageFactory.initElements(new AppiumFieldDecorator(androidDriver), page);
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Error when initializing page object: " + e);
        }
        return page;
    }

}
