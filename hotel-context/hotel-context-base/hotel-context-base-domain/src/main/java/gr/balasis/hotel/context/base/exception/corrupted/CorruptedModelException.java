package gr.balasis.hotel.context.base.exception.corrupted;

import gr.balasis.hotel.context.base.exception.HotelException;

public class CorruptedModelException extends HotelException {
    public CorruptedModelException(String message) {
        super(message);
    }
}
