package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.validation.ResourceDataValidator;
import gr.balasis.hotel.engine.core.mapper.RoomMapper;
import gr.balasis.hotel.engine.core.service.RoomService;

import gr.balasis.hotel.engine.core.validation.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource> {
    private final RoomService roomService;
    private final RoomValidator roomValidator;
    private final ResourceDataValidator resourceDataValidator;
    private final RoomMapper roomMapper;

    @Override
    @PostMapping
    public ResponseEntity<RoomResource> create(
            @RequestBody RoomResource roomResource) {

        roomResource.setId(null);
        resourceDataValidator.validateResourceData(roomResource);
        Room room = roomValidator.validate(roomMapper.toDomain(roomResource));
        System.out.println("pass through here 2");
        return ResponseEntity.ok(
                roomMapper.toResource(
                        roomService.create(room))
        );
    }

    @Override
    @PutMapping("{roomId}")
    public ResponseEntity<Void> update(
            @PathVariable Long roomId,
            @RequestBody RoomResource roomResource) {

        roomResource.setId(roomId);
        resourceDataValidator.validateResourceData(roomResource);
        roomService.update(roomValidator.validate(roomMapper.toDomain(roomResource)));
        return ResponseEntity.noContent().build();
    }



    @Override
    protected BaseService<Room,Long> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseMapper<Room, RoomResource> getMapper() {
        return roomMapper;
    }
}
