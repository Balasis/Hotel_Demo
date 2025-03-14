package gr.balasis.hotel.context.base.exception.notfound;

import gr.balasis.hotel.context.base.exception.HotelException;

public class EntityNotFoundException extends HotelException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
