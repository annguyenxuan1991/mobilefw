package pom.pageactions.iface;

import org.openqa.selenium.Point;

public interface IElementLocation {
    default Point getLocation() {
        return null;
    }
}
