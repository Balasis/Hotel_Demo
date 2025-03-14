package gr.balasis.hotel.context.base.exception.conflict;

public class RoomAvailabilityConflictException extends ReservationConflictException {
    public RoomAvailabilityConflictException(String message) {
        super(message);
    }
}
