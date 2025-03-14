package gr.balasis.hotel.context.base.exception.conflict;

import gr.balasis.hotel.context.base.exception.HotelException;

public class ReservationConflictException extends HotelException {
    public ReservationConflictException(String message) {
        super(message);
    }
}
