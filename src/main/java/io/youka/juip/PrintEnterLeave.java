package io.youka.juip;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.CLASS)
@Target({METHOD, CONSTRUCTOR})
public @interface PrintEnterLeave { }
