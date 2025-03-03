package gr.balasis.hotel.core.app.service;

import gr.balasis.hotel.context.base.domain.domains.Room;

public interface RoomService extends BaseService<Room,Long>{
    boolean existsById(Long guestId);
}
