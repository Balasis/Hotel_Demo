package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Guest;

import java.time.LocalDate;

public interface GuestValidator extends BaseValidator<Guest>{

    void validateEmailUnique(String email);

    void validateBirthDate(LocalDate birthDate);

    void validateGuestExists(Long guestId);
}
