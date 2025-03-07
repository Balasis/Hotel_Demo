package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.engine.core.mapper.RoomMapper;
import gr.balasis.hotel.engine.core.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource> {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Override
    protected BaseService<Room> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseMapper<Room, RoomResource> getMapper() {
        return roomMapper;
    }
}
