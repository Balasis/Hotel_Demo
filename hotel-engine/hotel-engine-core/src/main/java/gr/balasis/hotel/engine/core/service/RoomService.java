package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Room;

public interface RoomService extends BaseService<Room, Long> {
    boolean existsById(Long guestId);
}
