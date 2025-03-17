package gr.balasis.hotel.context.web.exception;

import gr.balasis.hotel.context.base.exception.HotelException;

public class InvalidResourceException extends HotelException {
    public InvalidResourceException(String message) {
        super(message);
    }
}
