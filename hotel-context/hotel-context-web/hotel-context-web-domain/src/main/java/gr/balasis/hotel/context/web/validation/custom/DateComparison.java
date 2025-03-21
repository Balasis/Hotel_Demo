package gr.balasis.hotel.context.web.validation.custom;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateComparisonValidator.class)
public @interface DateComparison {

    String message() default "Invalid date comparison";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String comparedField();
    String condition() default "before";
}