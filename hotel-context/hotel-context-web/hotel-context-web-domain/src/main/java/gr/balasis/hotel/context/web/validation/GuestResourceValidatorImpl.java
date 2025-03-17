package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.exception.InvalidResourceException;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.context.web.validation.data.ResourceDataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestResourceValidatorImpl extends BaseResourceValidatorImpl implements GuestResourceValidator {
    private final ResourceDataValidator resourceDataValidator;


    @Override
    public void validateForCreatingGuestReservation(ReservationResource reservationResource, Long guestId) {
        resourceDataValidator.validateResourceData(reservationResource, true);
        if (!reservationResource.getGuest().getId().equals(guestId)) {
            throw new InvalidResourceException("The ID in the request does not match the resource ID.");
        }

    }

    @Override
    public void validateForUpdatingGuest(GuestResource guestResource, Long guestId) {
        resourceDataValidator.validateResourceData(guestResource, false);
        if (!guestResource.getId().equals(guestId)) {
            throw new InvalidResourceException("The guest ID in the request does not match the guest resource ID.");
        }
    }

    @Override
    public void validateForUpdatingGuestReservation(ReservationResource reservationResource, Long guestId) {
        resourceDataValidator.validateResourceData(reservationResource, false);
        if (!reservationResource.getGuest().getId().equals(guestId)) {
            throw new InvalidResourceException("The ID in the request does not match the resource ID.");
        }
    }


    @Override
    public ResourceDataValidator getResourceDataValidator() {
        return resourceDataValidator;
    }
}
