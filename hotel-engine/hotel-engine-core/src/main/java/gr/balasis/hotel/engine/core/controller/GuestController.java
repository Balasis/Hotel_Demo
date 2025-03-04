package gr.balasis.hotel.engine.core.controller;


import gr.balasis.hotel.context.base.domain.Guest;
import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.domain.Feedback;

import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.*;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.context.web.resource.FeedbackResource;

import gr.balasis.hotel.engine.core.mapper.web.FeedbackWebMapper;
import gr.balasis.hotel.engine.core.mapper.web.GuestWebMapper;
import gr.balasis.hotel.engine.core.mapper.web.PaymentWebMapper;
import gr.balasis.hotel.engine.core.mapper.web.ReservationWebMapper;
import gr.balasis.hotel.engine.core.service.GuestService;
import gr.balasis.hotel.engine.core.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest,GuestResource> {
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final GuestWebMapper guestMapper;
    private final ReservationWebMapper reservationMapper;
    private final PaymentWebMapper paymentMapper;
    private final FeedbackWebMapper feedbackMapper;

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
        guestService.updateGuest(guestId, guestMapper.toDomain(guestResource));
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
                reservationService.createReservationForGuest(guestsId, reservationMapper.toDomain(reservation));
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
                guestsId, reservationId, paymentMapper.toDomain(paymentResource));
        return ResponseEntity.ok(paymentMapper.toResource(newPayment));
    }

    @PutMapping("/{guestsId}/email")
    public ResponseEntity<Void> updateEmail(
            @PathVariable Long guestsId,
            @RequestBody String email) {
        guestService.updateEmail(guestsId, email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody @Valid FeedbackResource resource) {
        Feedback feedback = reservationService.createFeedback(guestsId, reservationId, feedbackMapper.toDomain(resource));
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @PutMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> updateFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody FeedbackResource updatedResource) {
        reservationService.updateFeedback(guestsId, reservationId, feedbackMapper.toDomain(updatedResource));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        reservationService.deleteFeedback(guestsId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        Feedback feedback = reservationService.getFeedbackById(guestsId, reservationId);
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @Override
    protected BaseService<Guest> getBaseService() {
        return guestService;
    }

    @Override
    protected BaseWebMapper<Guest, GuestResource> getMapper() {
        return guestMapper;
    }
}
