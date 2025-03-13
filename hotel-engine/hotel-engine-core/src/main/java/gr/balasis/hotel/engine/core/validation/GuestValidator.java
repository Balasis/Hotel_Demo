package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Guest;

public interface GuestValidator {
    Guest validate(Guest guest);
    void validateEmailUnique(String email);
    void validateGuestExists(Long guestId);
}
