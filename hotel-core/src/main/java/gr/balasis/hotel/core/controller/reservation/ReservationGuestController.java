package gr.balasis.hotel.core.controller.reservation;

import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.controller.BaseController;
import gr.balasis.hotel.core.entity.ReservationEntity;
import gr.balasis.hotel.core.mapper.BaseMapper;
import gr.balasis.hotel.core.mapper.ReservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.GuestService;
import gr.balasis.hotel.core.service.ReservationService;
import gr.balasis.hotel.core.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("guests/{guestId}/reservations")
public class ReservationGuestController extends BaseController<Reservation, ReservationResource, ReservationEntity> {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final GuestService guestService;
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<ReservationResource>> findByGuestId(
            @PathVariable final Long guestId) {
        List<ReservationResource> resources = reservationService.findByGuestId(guestId)
                .stream()
                .map(getMapper()::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<ReservationResource> create(
            @RequestBody ReservationResource reservationResource) {

        if (!guestService.existsById(reservationResource.getGuest().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");
        }

        if (!roomService.existsById(reservationResource.getRoom().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
        }

        Reservation reservation = reservationMapper.toDomainFromResource(reservationResource);
        Reservation savedReservation = reservationService.create(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toResource(savedReservation));
    }

    @Override
    protected BaseService<Reservation, Long> getBaseService() {
        return reservationService;
    }

    @Override
    protected BaseMapper<Reservation, ReservationResource, ReservationEntity> getMapper() {
        return reservationMapper;
    }

}
