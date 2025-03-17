package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Room;

public interface RoomValidator {
    Room validate(Room room);
    Room validateForUpdate(Room room);
}
