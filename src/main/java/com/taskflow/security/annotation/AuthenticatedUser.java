package com.taskflow.security.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface AuthenticatedUser {
	
}
