package gr.balasis.hotel.context.base.exception.business;

public class RoomAvailabilityConflictException extends BusinessLogicException {
    public RoomAvailabilityConflictException(String message) {
        super(message);
    }
}
