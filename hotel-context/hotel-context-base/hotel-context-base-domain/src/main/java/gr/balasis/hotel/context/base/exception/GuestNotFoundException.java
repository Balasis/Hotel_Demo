package gr.balasis.hotel.context.base.exception;

public class GuestNotFoundException extends EntityNotFoundException {
    public GuestNotFoundException(String message) {
        super(message);
    }
}
