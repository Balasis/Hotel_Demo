package gr.balasis.hotel.engine.core.validation;


import gr.balasis.hotel.context.base.exception.HotelException;
import gr.balasis.hotel.context.base.exception.dublicate.DuplicateEmailException;
import gr.balasis.hotel.context.base.exception.notfound.GuestNotFoundException;
import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class GuestValidatorImpl implements GuestValidator {

    private final GuestRepository guestRepository;


    @Override
    public Guest validate(Guest guest){
        validateEmailUnique(guest.getEmail());
        validateBirthDate(guest.getBirthDate());
        return guest;
    }

    @Override
    public void validateEmailUnique(String email) {
        if (guestRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }
    }

    @Override
    public void validateBirthDate(LocalDate birthDate){
        if (birthDate.isAfter(LocalDate.now())) {
            throw new HotelException("Birth date cannot be in the future");
        }
    }

    @Override
    public void validateGuestExists(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new GuestNotFoundException("Guest not found");
        }
    }
}
