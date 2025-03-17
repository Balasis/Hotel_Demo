package gr.balasis.hotel.context.web.validation;

import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;

public interface GuestResourceValidator extends BaseResourceValidator {

    void validateForCreatingGuestReservation(ReservationResource reservationResource, Long guestId);

    void validateForUpdatingGuest(GuestResource guestResource, Long guestId);

    void validateForUpdatingGuestReservation(ReservationResource reservationResource, Long guestId);
}
