package gr.balasis.hotel.context.base.exception;

public class ReservationNotFoundException extends EntityNotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
