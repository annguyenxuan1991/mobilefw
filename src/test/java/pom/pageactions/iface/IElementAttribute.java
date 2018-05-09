package pom.pageactions.iface;

public interface IElementAttribute {
    default String getText() {
        return null;
    }

    default String getResourceId() {
        return null;
    }

    default String getClassName() {
        return null;
    }

    default String getContentDesc() {
        return null;
    }
}
