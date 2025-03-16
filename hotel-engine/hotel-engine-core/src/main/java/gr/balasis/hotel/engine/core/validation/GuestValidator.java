package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Guest;

import java.time.LocalDate;

public interface GuestValidator {
    Guest validate(Guest guest);
    void validateEmailUnique(String email);

    void validateBirthDate(LocalDate birthDate);

    void validateGuestExists(Long guestId);
}
