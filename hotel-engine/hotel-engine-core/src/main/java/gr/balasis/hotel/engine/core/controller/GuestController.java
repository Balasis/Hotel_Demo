package gr.balasis.hotel.engine.core.controller;


import gr.balasis.hotel.context.base.model.Guest;

import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.*;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.context.web.resource.FeedbackResource;

import gr.balasis.hotel.engine.core.mapper.FeedbackMapper;
import gr.balasis.hotel.engine.core.mapper.GuestMapper;
import gr.balasis.hotel.engine.core.mapper.PaymentMapper;
import gr.balasis.hotel.engine.core.mapper.ReservationMapper;
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
    private final GuestMapper guestMapper;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;
    private final FeedbackMapper feedbackMapper;

    @Override
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResource> get(
            @PathVariable Long guestId) {

        return ResponseEntity.ok(
                guestMapper.toResource(
                        guestService.getGuest(guestId))
        );
    }

    @PutMapping("/{guestId}")
    public ResponseEntity<Void> updateGuest(
            @PathVariable Long guestId,
            @RequestBody @Valid GuestResource guestResource) {

        guestService.updateGuest(guestId, guestMapper.toDomain(guestResource));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(
            @PathVariable Long guestId) {

        guestService.deleteGuest(guestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{guestId}/reservations")
    public ResponseEntity<List<ReservationResource>> findReservations(
            @PathVariable Long guestId) {

        return ResponseEntity.ok(
                reservationMapper.toResources(
                        reservationService.findReservations(guestId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<ReservationResource> getReservation(
            @PathVariable Long guestId,
            @PathVariable Long reservationId) {

        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.getReservation(guestId, reservationId))
        );
    }

    @PostMapping("/{guestId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable Long guestId,
            @RequestBody ReservationResource reservation) {

        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.createReservation(guestId, reservationMapper.toDomain(reservation)))
        );
    }

    @PutMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> updateReservation(
            @PathVariable Long guestId,
            @PathVariable Long reservationId,
            @RequestBody ReservationResource reservationResource) {

        reservationService.updateReservation(guestId,reservationId, reservationResource);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> manageReservationAction(
            @PathVariable Long guestId,
            @PathVariable Long reservationId,
            @RequestHeader(value = "a", required = true) String action) {

        reservationService.manageReservationAction(guestId,reservationId,action);
        return ResponseEntity.noContent().build();
    }




    @GetMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getFeedback(
            @PathVariable Long guestId,
            @PathVariable Long reservationId) {
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.getFeedback(guestId, reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> getPayment(
            @PathVariable Long guestId,
            @PathVariable Long reservationId) {

        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.getPayment(guestId, reservationId)) );
    }



    @PutMapping("/{guestsId}/email")
    public ResponseEntity<Void> updateEmail(
            @PathVariable Long guestsId,
            @RequestBody String email) {
        guestService.updateEmail(guestsId, email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> payReservation(
            @PathVariable Long guestId,
            @PathVariable Long reservationId,
            @RequestBody @Valid PaymentResource paymentResource) {

        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.finalizePayment(guestId, reservationId, paymentMapper.toDomain(paymentResource))
        ));
    }

    @PostMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable Long guestsId,
            @PathVariable Long reservationId,
            @RequestBody @Valid FeedbackResource resource) {
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.createFeedback(guestsId, reservationId, feedbackMapper.toDomain(resource))
        ));
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
    protected BaseService<Guest,Long> getBaseService() {
        return guestService;
    }

    @Override
    protected BaseMapper<Guest, GuestResource> getMapper() {
        return guestMapper;
    }





}
