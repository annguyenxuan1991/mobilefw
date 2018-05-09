package pom.pageactions.android;

import io.appium.java_client.MobileElement;
import org.apache.log4j.Logger;
import pom.pageactions.iface.IElementAction;
import pom.pageactions.iface.IMobileElement;

public class ElementActionImpl implements IElementAction {
    private static final Logger LOGGER = Logger.getLogger(ElementActionImpl.class);

    private IMobileElement mElement;
    private MobileElement mobileElement;

    public ElementActionImpl(MobileElement mobileElement) {
        this.mobileElement = mobileElement;
    }

    public void clearText() {
        LOGGER.info("Clear text on: " + mobileElement.toString());
        mobileElement.clear();
    }

    public void typeText(String text) {
        LOGGER.info("Type text on: " + mobileElement.toString());
        mobileElement.sendKeys(text);
    }

    public void tap() {
        LOGGER.info("Tap on: " + mobileElement.toString());
        mobileElement.click();
    }

    public void clearAndType(String text) {
        LOGGER.info("Clear and type new text on: " + mobileElement.toString());
        clearText();
        typeText(text);
    }
}
