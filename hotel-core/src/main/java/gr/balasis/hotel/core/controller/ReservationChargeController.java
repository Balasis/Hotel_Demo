package gr.balasis.hotel.core.controller;


import gr.balasis.hotel.context.base.domain.ReservationCharge;
import gr.balasis.hotel.context.web.resource.ReservationChargeResource;
import gr.balasis.hotel.core.mapper.ReservationChargeMapper;
import gr.balasis.hotel.core.service.ReservationChargeService;
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

    @GetMapping
    public ResponseEntity<ReservationChargeResource> getReservationCharge(@PathVariable Long reservationId) {
        ReservationCharge reservationChargeDomain = reservationChargeService.getChargeByReservationId(reservationId);
        ReservationChargeResource reservationChargeResource = reservationChargeMapper.toResource(reservationChargeDomain);
        return ResponseEntity.ok(reservationChargeResource);
    }

    @PostMapping
    public ResponseEntity<ReservationChargeResource> createReservationCharge(@PathVariable Long reservationId, @RequestBody ReservationChargeResource resource) {
        ReservationCharge reservationChargeDomain = reservationChargeMapper.toDomainFromResource(resource);
        reservationChargeDomain = reservationChargeService.createCharge(reservationId, reservationChargeDomain);
        ReservationChargeResource reservationChargeResource = reservationChargeMapper.toResource(reservationChargeDomain);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationChargeResource);
    }
}