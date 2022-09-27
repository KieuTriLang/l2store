package com.ktl.l2store.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface PagingParam {

    int page() default 1;

    int limited() default 12;

    String sortTar() default "";

    String sortDir() default "asc";
}
