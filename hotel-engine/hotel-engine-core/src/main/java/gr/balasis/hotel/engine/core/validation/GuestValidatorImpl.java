package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.engine.core.repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuestValidatorImpl implements GuestValidator {

    private final GuestRepository guestRepository;

    @Override
    public void validateEmailUnique(String email) {
        if (guestRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    @Override
    public void validateGuestExists(Long guestId) {
        if (!guestRepository.existsById(guestId)) {
            throw new IllegalArgumentException("Guest not found");
        }
    }
}
