package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.resource.BaseResource;
import gr.balasis.hotel.context.web.validation.data.ResourceDataValidator;


public abstract class BaseResourceValidatorImpl implements BaseResourceValidator {
    public abstract ResourceDataValidator getResourceDataValidator();

    @Override
    public <R extends BaseResource> void onlyDataValidation(R toBeValidatedResource, boolean toCreate) {
        getResourceDataValidator().validateResourceData(toBeValidatedResource, toCreate);
    }
}
