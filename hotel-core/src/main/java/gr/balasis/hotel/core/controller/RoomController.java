package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.core.mapper.resourcedomain.RDbaseMapper;
import gr.balasis.hotel.core.mapper.resourcedomain.RDroomMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource > {
    private final RoomService roomService;
    private final RDroomMapper rDroomMapper;

    @Override
    protected BaseService<Room, Long> getBaseService() {
        return roomService;
    }

    @Override
    protected RDbaseMapper<RoomResource, Room> getMapper() {
        return rDroomMapper;
    }
}
