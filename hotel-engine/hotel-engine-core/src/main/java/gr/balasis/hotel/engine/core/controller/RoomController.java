package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.domain.Room;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.RoomMapper;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.core.app.service.BaseService;
import gr.balasis.hotel.engine.core.service.RoomService;
import gr.balasis.hotel.context.base.entity.RoomEntity;
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
public class RoomController extends BaseController<Room, RoomResource, RoomEntity> {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    public ResponseEntity<List<RoomResource>> findAll() {
        List<RoomResource> resources = roomService.findAll()
                .stream()
                .map(getMapper()::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @Override
    protected BaseService<Room, Long> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseMapper<Room, RoomResource, RoomEntity> getMapper() {
        return roomMapper;
    }
}
