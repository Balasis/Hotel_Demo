package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.validation.data.ResourceDataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationResourceValidatorImpl extends BaseResourceValidatorImpl implements ReservationResourceValidator {
    private final ResourceDataValidator resourceDataValidator;

    @Override
    public ResourceDataValidator getResourceDataValidator() {
        return resourceDataValidator;
    }
}
