package gr.balasis.hotel.context.web.validation.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateComparisonValidator implements ConstraintValidator<DateComparison, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String condition;

    @Override
    public void initialize(DateComparison annotation) {
        this.firstFieldName = annotation.firstField();
        this.secondFieldName = annotation.secondField();
        this.condition = annotation.condition();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(firstFieldName);
            Field secondField = value.getClass().getDeclaredField(secondFieldName);
            firstField.setAccessible(true);
            secondField.setAccessible(true);

            LocalDate firstDate = (LocalDate) firstField.get(value);
            LocalDate secondDate = (LocalDate) secondField.get(value);

            if (firstDate == null || secondDate == null) {
                return true;
            }

            return switch (condition) {
                case "before" -> firstDate.isBefore(secondDate);
                case "after" -> firstDate.isAfter(secondDate);
                case "equal" -> firstDate.isEqual(secondDate);
                case "beforeOrEqual" -> firstDate.isBefore(secondDate) || firstDate.isEqual(secondDate);
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }
}