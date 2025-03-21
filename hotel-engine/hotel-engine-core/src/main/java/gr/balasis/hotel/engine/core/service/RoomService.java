package gr.balasis.hotel.engine.core.service;


import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.context.base.service.BaseService;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService extends BaseService<Room, Long> {
    List<Room> searchBy(String roomNumber, BigDecimal pricePerNight, String bedType, Integer floor);
}
