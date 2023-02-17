package com.gangnam.portal.annotation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD) // 1
@Retention(RetentionPolicy.RUNTIME) // 2
@Constraint(validatedBy = { KakaoEmailValidator.class }) // 3
public @interface KakaoEmail {
    String message() default "message"; // 4
    Class[] groups() default {};
    Class[] payload() default {};
}
