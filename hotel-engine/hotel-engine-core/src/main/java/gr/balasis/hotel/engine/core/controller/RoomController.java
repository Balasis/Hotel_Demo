package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.web.mapper.BaseWebMapper;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.resource.RoomResource;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.engine.core.mapper.web.RoomWebMapper;

import gr.balasis.hotel.engine.core.service.RoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource> {
    private final RoomService roomService;
    private final RoomWebMapper roomMapper;

    @Override
    protected BaseService<Room> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseWebMapper<Room, RoomResource> getMapper() {
        return roomMapper;
    }
}
