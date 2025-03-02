package gr.balasis.hotel.core.app.controller;


import gr.balasis.hotel.context.base.domain.domains.Guest;
import gr.balasis.hotel.context.base.domain.domains.Payment;
import gr.balasis.hotel.context.base.domain.domains.Reservation;
import gr.balasis.hotel.context.base.mapper.GuestMapper;
import gr.balasis.hotel.context.base.mapper.PaymentMapper;
import gr.balasis.hotel.context.base.mapper.ReservationMapper;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.core.app.service.GuestService;
import gr.balasis.hotel.core.app.service.ReservationService;
import gr.balasis.hotel.modules.feedback.base.BaseComponent;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<GuestResource> createGuest(
            @RequestBody @Valid GuestResource guestResource) {
        Guest newGuest = guestService.create(guestMapper.toDomainFromResource(guestResource));
        return ResponseEntity.ok(guestMapper.toResource(newGuest));
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResource> findGuestById(
            @PathVariable Long guestId) {
        Guest guest = guestService.findGuestById(guestId);
        return ResponseEntity.ok(guestMapper.toResource(guest));
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuestById(
            @PathVariable Long guestId) {
        guestService.deleteGuestById(guestId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<Void> updateGuest(
            @PathVariable Long guestId,
            @RequestBody @Valid GuestResource guestResource) {
        guestService.updateGuest(guestId, guestMapper.toDomainFromResource(guestResource));
        return ResponseEntity.noContent().build();
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

    @GetMapping("/{guestsId}/reservations/{reservationId}")
    public ResponseEntity<ReservationResource> findReservationById(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        Reservation reservation = reservationService.findReservationById(guestsId, reservationId);
        return ResponseEntity.ok(reservationMapper.toResource(reservation));
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
            @RequestBody @Valid PaymentResource paymentResource) {

        Payment newPayment = reservationService.finalizePaymentForReservation(
                guestsId, reservationId, paymentMapper.toDomainFromResource(paymentResource));
        return ResponseEntity.ok(paymentMapper.toResource(newPayment));
    }

    @PutMapping("/{guestsId}/email")
    public ResponseEntity<Void> updateEmail(
            @PathVariable Long guestsId,
            @RequestBody String email) {
        guestService.updateEmail(guestsId, email);
        return ResponseEntity.noContent().build();
    }
}
