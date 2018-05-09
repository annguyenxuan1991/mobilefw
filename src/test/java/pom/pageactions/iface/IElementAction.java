package pom.pageactions.iface;

public interface IElementAction {

    default void clearText() {
    }

    default void typeText(String text) {
    }

    default void tap() {
    }

    default void tapByJs() {
    }

    default void scrollTo() {
    }

    default void clearAndType(String text) {

    }
}
