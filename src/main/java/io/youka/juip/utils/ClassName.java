package io.youka.juip.utils;

import java.util.Objects;

public class ClassName {
    // Constants
    private static final char PACKAGE_SEPARATOR = '.';

    // Attributes
    private final String packageName;
    private final String simpleName;

    // Constructors
    public ClassName(Class<?> clazz) {
        this(clazz.getName());
    }
    public ClassName(String clazzName) {
        final int separatorPosition = clazzName.lastIndexOf(PACKAGE_SEPARATOR);
        if (separatorPosition < 0) {
            throw new IllegalArgumentException("Invalid class without a separator: " + clazzName);
        }
        this.packageName = clazzName.substring(0, separatorPosition);
        this.simpleName = clazzName.substring(separatorPosition + 1);
    }

    // Getters
    public String getPackageName() {
        return this.packageName;
    }
    public String getSimpleName() {
        return this.simpleName;
    }

    // Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ClassName className = (ClassName) o;
        return Objects.equals(this.packageName, className.packageName) && Objects.equals(this.simpleName, className.simpleName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.packageName, this.simpleName);
    }
    @Override
    public String toString() {
        return this.packageName + PACKAGE_SEPARATOR + this.simpleName;
    }
}
