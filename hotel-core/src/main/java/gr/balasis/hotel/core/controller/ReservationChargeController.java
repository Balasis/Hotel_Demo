package gr.balasis.hotel.core.controller;


import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.domain.ReservationCharge;
import gr.balasis.hotel.context.web.resource.ReservationChargeResource;
import gr.balasis.hotel.core.mapper.ReservationChargeMapper;
import gr.balasis.hotel.core.service.ReservationChargeService;
import gr.balasis.hotel.core.service.ReservationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/{reservationId}/ReservationCharge")
public class ReservationChargeController {

    private final ReservationChargeService reservationChargeService;
    private final ReservationChargeMapper reservationChargeMapper;
    private final ReservationServiceImpl reservationService;

    @GetMapping
    public ResponseEntity<ReservationChargeResource> getReservationCharge(@PathVariable Long reservationId) {
        ReservationCharge reservationChargeDomain = reservationChargeService.getChargeByReservationId(reservationId);
        ReservationChargeResource reservationChargeResource = reservationChargeMapper.toResource(reservationChargeDomain);
        return ResponseEntity.ok(reservationChargeResource);
    }

    @PostMapping
    public ResponseEntity<ReservationChargeResource> createReservationCharge(@RequestBody ReservationChargeResource resource) {
        ReservationCharge reservationChargeDomain = reservationChargeMapper.toDomainFromResource(resource);

        Reservation reservation = reservationService.findById(resource.getReservationId());

        reservationChargeDomain = reservationChargeService.createCharge(reservation, reservationChargeDomain);

        ReservationChargeResource reservationChargeResource = reservationChargeMapper.toResource(reservationChargeDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationChargeResource);
    }
}