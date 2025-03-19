package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.model.Room;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.RoomResource;
import gr.balasis.hotel.context.web.validation.RoomResourceValidator;
import gr.balasis.hotel.engine.core.mapper.RoomMapper;
import gr.balasis.hotel.engine.core.service.RoomService;
import gr.balasis.hotel.engine.core.validation.RoomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController extends BaseController<Room, RoomResource> {
    private final RoomService roomService;
    private final RoomResourceValidator roomResourceValidator;
    private final RoomValidator roomValidator;
    private final RoomMapper roomMapper;


    @GetMapping()
    public ResponseEntity<List<RoomResource>> findAll(@RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) BigDecimal pricePerNight,@RequestParam(required = false) String bedType,
            @RequestParam(required = false) Integer floor) {

        List<Room> rooms = roomService.searchBy(roomNumber, pricePerNight,bedType,floor);
        return ResponseEntity.ok(roomMapper.toResources(rooms));
    }

    @Override
    @PostMapping
    public ResponseEntity<RoomResource> create(@RequestBody RoomResource roomResource) {

        roomResourceValidator.onlyDataValidation(roomResource, true);
        Room room = roomValidator.validate(roomMapper.toDomain(roomResource));
        return ResponseEntity.ok(
                roomMapper.toResource(
                        roomService.create(room))
        );
    }

    @Override
    @PutMapping("{roomId}")
    public ResponseEntity<Void> update(@PathVariable Long roomId,@RequestBody RoomResource roomResource) {

        roomResourceValidator.onlyDataValidation(roomResource, false);
        roomService.update(roomValidator.validate(roomMapper.toDomain(roomResource)));
        return ResponseEntity.noContent().build();
    }


    @Override
    protected BaseService<Room, Long> getBaseService() {
        return roomService;
    }

    @Override
    protected BaseMapper<Room, RoomResource> getMapper() {
        return roomMapper;
    }
}
