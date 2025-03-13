package gr.balasis.hotel.context.base.exception;

public class RoomNotAvailableException extends ReservationConflictException{
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
