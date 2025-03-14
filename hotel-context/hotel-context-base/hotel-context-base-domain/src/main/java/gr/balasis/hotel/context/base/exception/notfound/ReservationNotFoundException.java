package gr.balasis.hotel.context.base.exception.notfound;

public class ReservationNotFoundException extends EntityNotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
