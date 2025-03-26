package gr.balasis.hotel.context.base.exception.business;

import gr.balasis.hotel.context.base.exception.HotelException;

public class BusinessLogicException extends HotelException {
    public BusinessLogicException(String message) {
        super(message);
    }
}
