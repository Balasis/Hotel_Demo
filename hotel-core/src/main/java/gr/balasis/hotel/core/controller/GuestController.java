package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.data.entity.GuestEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.GuestMapper;
import gr.balasis.hotel.context.base.mapper.ReservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest, GuestResource, GuestEntity> {
    private final GuestService guestService;
    private final GuestMapper guestMapper;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public ResponseEntity<List<GuestResource>> findAll() {
        List<GuestResource> resources = guestService.findAll()
                .stream()
                .map(guestMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationResource>> getGuestReservations(
            @PathVariable Long id) {
        List<ReservationResource> reservations = guestService.findReservationsByGuestId(id)
                .stream()
                .map(reservationMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable Long id,
            @RequestBody ReservationResource reservation) {
        Reservation newReservation =
                guestService.createReservationForGuest(id, reservationMapper.toDomainFromResource(reservation));
        return ResponseEntity.ok(reservationMapper.toResource(newReservation));
    }

    @DeleteMapping("/{id}/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @PathVariable Long reservationId) {
        guestService.cancelReservation(id, reservationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<GuestResource> updateEmail(@PathVariable Long id, @RequestBody String email) {
        Guest updatedGuest = guestService.updateEmail(id, email);
        return ResponseEntity.ok(guestMapper.toResource(updatedGuest));
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<GuestResource>> searchGuests(
//            @RequestParam(required = false) String firstName,
//            @RequestParam(required = false) String lastName) {
//
//        if (firstName == null && lastName == null) {
//            throw new IllegalArgumentException("At least one of 'firstName' or 'lastName' must be provided");
//        }
//
//        List<Guest> guests;
//        if (firstName != null && lastName != null) {
//            guests = guestService.findByFirstNameAndLastName(firstName, lastName);
//        } else if (firstName != null) {
//            guests = guestService.findByFirstName(firstName);
//        } else {
//            guests = guestService.findByLastName(lastName);
//        }
//
//        return ResponseEntity.ok(guests.stream()
//                .map(guestMapper::toResource)
//                .collect(Collectors.toList()));
//    }

//    @GetMapping("guests/{guestId}/reservations")
//    public ResponseEntity<List<ReservationResource>> findByGuestId(
//            @PathVariable final Long guestId) {
//        List<ReservationResource> resources = reservationService.findByGuestId(guestId)
//                .stream()
//                .map(getMapper()::toResource)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(resources);
//    }
//
//    @PostMapping("guests/{guestId}/reservations")
//    public ResponseEntity<ReservationResource> create(
//            @RequestBody ReservationResource reservationResource) {
//
//        if (!guestService.existsById(reservationResource.getGuest().getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found");
//        }
//
//        if (!roomService.existsById(reservationResource.getRoom().getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found");
//        }
//
//        Reservation reservation = reservationMapper.toDomainFromResource(reservationResource);
//        Reservation savedReservation = reservationService.create(reservation);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(reservationMapper.toResource(savedReservation));
//    }

    @Override
    public BaseService<Guest, Long> getBaseService() {
        return guestService;
    }

    @Override
    public BaseMapper<Guest, GuestResource, GuestEntity> getMapper() {
        return guestMapper;
    }
}
