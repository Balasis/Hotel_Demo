package gr.balasis.hotel.context.base.exception.unauthorized;

import gr.balasis.hotel.context.base.exception.HotelException;

public class UnauthorizedAccessException extends HotelException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
