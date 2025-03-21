package gr.balasis.hotel.context.web.validation.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateComparisonValidator implements ConstraintValidator<DateComparison, LocalDate> {

    private String comparedField;
    private String condition;

    @Override
    public void initialize(DateComparison annotation) {
        this.comparedField = annotation.comparedField();
        this.condition = annotation.condition();

    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        System.out.println("the value is : " + value);
        System.out.println("the string field is " + comparedField);


        try {
            if (value == null) {
                return true;
            }

            Field comparedFieldObj = value.getClass().getDeclaredField(comparedField);

            comparedFieldObj.setAccessible(true);
            LocalDate comparedDate = (LocalDate) comparedFieldObj.get(value);
            System.out.println("the value is : " + value);
            System.out.println("the compare date is :" +comparedDate);

            if (comparedDate == null) {
                return true;
            }

            return switch (condition) {
                case "before" -> value.isBefore(comparedDate);
                case "after" -> value.isAfter(comparedDate);
                case "equal" -> value.isEqual(comparedDate);
                case "beforeOrEqual" -> value.isBefore(comparedDate) || value.isEqual(comparedDate);
                default -> false;
            };
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}

