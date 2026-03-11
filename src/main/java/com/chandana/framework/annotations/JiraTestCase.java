package com.chandana.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to map a test method to a Jira test case ID
 * Usage: @JiraTestCase(id = "TEST-101")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JiraTestCase {
    String id();
    String summary() default "";
}
