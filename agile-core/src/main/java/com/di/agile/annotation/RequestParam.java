package com.di.agile.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

	public abstract String value() default "";

	public abstract String name() default "";

	public abstract boolean required() default false;

	public abstract String defaultValue() default "";
}