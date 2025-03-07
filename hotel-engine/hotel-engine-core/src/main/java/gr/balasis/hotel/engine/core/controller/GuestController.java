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
    public ResponseEntity<GuestResource> getGuest(
            @PathVariable Long guestId) {
        return ResponseEntity.ok(
                guestMapper.toResource(
                        guestService.getGuest(guestId))
        );
    }

    @GetMapping("/{guestsId}/reservations")
    public ResponseEntity<List<ReservationResource>> findGuestReservations(
            @PathVariable Long guestsId) {
        return ResponseEntity.ok(
                reservationMapper.toResources(reservationService.findReservations(guestsId))
        );
    }

    @GetMapping("/{guestsId}/reservations/{reservationId}")
    public ResponseEntity<ReservationResource> getReservation(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationMapper.toResource(
                reservationService.getReservation(guestsId, reservationId)));
    }

    @GetMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        Feedback feedback = reservationService.getFeedback(guestsId, reservationId);
        return ResponseEntity.ok(feedbackMapper.toResource(feedback));
    }

    @GetMapping("/{guestsId}/reservations/{reservationId}/payment")
    public ResponseEntity<Payment> getPaymentForReservation(
            @PathVariable Long reservationId,
            @PathVariable Long guestsId) {

        Payment payment = reservationService.getPayment(guestsId, reservationId);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(
            @PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<Void> updateGuest(
            @PathVariable Long guestId,
            @RequestBody @Valid GuestResource guestResource) {
        guestService.updateGuest(guestId, guestMapper.toDomain(guestResource));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestsId}/email")
    public ResponseEntity<Void> updateEmail(
            @PathVariable Long guestsId,
            @RequestBody String email) {
        guestService.updateEmail(guestsId, email);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{guestsId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable Long guestsId,
            @RequestBody ReservationResource reservation) {
        Reservation newReservation =
                reservationService.createReservation(guestsId, reservationMapper.toDomain(reservation));
        return ResponseEntity.ok(reservationMapper.toResource(newReservation));
    }


    @DeleteMapping("/{guestsId}/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId) {
        reservationService.cancelReservation(guestsId, reservationId);
        return ResponseEntity.noContent().build();
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

    @Override
    protected BaseService<Guest> getBaseService() {
        return guestService;
    }

    @Override
    protected BaseWebMapper<Guest, GuestResource> getMapper() {
        return guestMapper;
    }





}
