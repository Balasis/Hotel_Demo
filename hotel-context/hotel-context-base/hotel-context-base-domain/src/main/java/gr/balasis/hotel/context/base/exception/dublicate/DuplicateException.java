package gr.balasis.hotel.context.base.exception.dublicate;

import gr.balasis.hotel.context.base.exception.HotelException;

public class DuplicateException extends HotelException {
    public DuplicateException(String message) {
        super(message);
    }
}