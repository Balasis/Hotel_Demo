package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.base.component.BaseComponent;
import gr.balasis.hotel.context.web.resource.BaseResource;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

@Component
public class ResourceDataValidator extends BaseComponent {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public <T extends BaseResource> void validateResourceData(T resourceToBeValidated) {
        var constraintViolations = validator.validate(resourceToBeValidated);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

}
