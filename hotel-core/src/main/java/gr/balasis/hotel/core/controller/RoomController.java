package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.core.entity.RoomEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.RoomMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource , RoomEntity> {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Override
    protected BaseService<Room, Long> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseMapper<Room, RoomResource , RoomEntity> getMapper() {
        return roomMapper;
    }
}
