package pom.pageactions.iface;

import pom.pageactions.iface.IElementLocation;

public interface IElementStatus extends IElementLocation {
    default boolean isExists() {
        return false;
    }

    default boolean isChecked() {
        return false;
    }

    default boolean isCheckable() {
        return false;
    }

    default boolean isClickable() {
        return false;
    }

    default boolean isEnabled() {
        return false;
    }

    default boolean isFocusable() {
        return false;
    }

    default boolean isFocused() {
        return false;
    }

    default boolean isScrollable() {
        return false;
    }

    default boolean isLongClickable() {
        return false;
    }

    default boolean isSelected() {
        return false;
    }
}
