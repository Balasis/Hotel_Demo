package gr.balasis.hotel.engine.core.validation;

public interface GuestValidator {
    void validateEmailUnique(String email);

    void validateGuestExists(Long guestId);

}
