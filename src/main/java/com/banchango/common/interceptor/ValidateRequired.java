package com.banchango.common.interceptor;

import com.banchango.domain.users.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRequired {
    UserRole[] roles() default { UserRole.USER, UserRole.ADMIN };
}
