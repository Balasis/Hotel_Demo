package gr.balasis.hotel.context.base.exception;

public class RoomNotFoundException extends EntityNotFoundException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}
