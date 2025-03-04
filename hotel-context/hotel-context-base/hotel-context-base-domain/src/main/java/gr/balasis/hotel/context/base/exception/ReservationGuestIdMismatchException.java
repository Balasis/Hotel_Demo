package gr.balasis.hotel.context.base.exception;

public class ReservationGuestIdMismatchException extends RuntimeException {
    public ReservationGuestIdMismatchException(String message) {
        super(message);
    }
}
