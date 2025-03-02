package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.domains.Guest;
import gr.balasis.hotel.context.base.domain.domains.Payment;
import gr.balasis.hotel.context.base.domain.domains.Reservation;
import gr.balasis.hotel.context.base.mapper.PaymentMapper;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.data.entity.GuestEntity;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.mapper.GuestMapper;
import gr.balasis.hotel.context.base.mapper.ReservationMapper;
import gr.balasis.hotel.core.service.BaseService;
import gr.balasis.hotel.core.service.GuestService;
import gr.balasis.hotel.core.service.ReservationService;
import gr.balasis.hotel.modules.feedback.base.BaseComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseComponent {
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final GuestMapper guestMapper;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;

    @GetMapping
    public ResponseEntity<List<GuestResource>> findAll() {
        List<GuestResource> resources = guestService.findAll()
                .stream()
                .map(guestMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{guestsId}/reservations")
    public ResponseEntity<List<ReservationResource>> getGuestReservations(
            @PathVariable Long guestsId) {
        List<ReservationResource> reservations = reservationService.findReservationsByGuestId(guestsId)
                .stream()
                .map(reservationMapper::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/{guestsId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable Long guestsId,
            @RequestBody ReservationResource reservation) {
        Reservation newReservation =
                reservationService.createReservationForGuest(guestsId, reservationMapper.toDomainFromResource(reservation));
        return ResponseEntity.ok(reservationMapper.toResource(newReservation));
    }



    @DeleteMapping("/{guestsId}/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        reservationService.cancelReservation(guestsId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{guestsId}/reservations/{reservationId}/payment")
    public ResponseEntity<Payment> getPaymentForReservation(
            @PathVariable Long reservationId,
            @PathVariable Long guestsId) {

        Payment payment = reservationService.getPaymentForReservation(guestsId, reservationId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{guestsId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> payForReservation(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody PaymentResource paymentResource) {

        Payment newPayment = reservationService.processPaymentForReservation(
                guestsId, reservationId, paymentMapper.toDomainFromResource(paymentResource));
        return ResponseEntity.ok(paymentMapper.toResource(newPayment));
    }

    @PutMapping("/{guestsId}/email")
    public ResponseEntity<GuestResource> updateEmail(@PathVariable Long guestsId, @RequestBody String email) {
        Guest updatedGuest = guestService.updateEmail(guestsId, email);
        return ResponseEntity.ok(guestMapper.toResource(updatedGuest));
    }
}
