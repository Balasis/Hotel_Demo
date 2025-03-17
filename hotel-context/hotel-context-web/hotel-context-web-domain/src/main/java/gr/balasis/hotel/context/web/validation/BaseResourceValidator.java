package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.resource.BaseResource;

public interface BaseResourceValidator {

    <R extends BaseResource> void onlyDataValidation(R toBeValidatedResource, boolean toCreate);
}
