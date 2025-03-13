package gr.balasis.hotel.engine.core.controller;


import gr.balasis.hotel.context.base.model.Guest;

import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.*;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
import gr.balasis.hotel.context.web.resource.FeedbackResource;

import gr.balasis.hotel.context.web.validation.ResourceDataValidator;
import gr.balasis.hotel.engine.core.mapper.FeedbackMapper;
import gr.balasis.hotel.engine.core.mapper.GuestMapper;
import gr.balasis.hotel.engine.core.mapper.PaymentMapper;
import gr.balasis.hotel.engine.core.mapper.ReservationMapper;
import gr.balasis.hotel.engine.core.service.GuestService;
import gr.balasis.hotel.engine.core.service.ReservationService;

import gr.balasis.hotel.engine.core.validation.GuestValidator;
import gr.balasis.hotel.engine.core.validation.ReservationValidator;
import gr.balasis.hotel.engine.core.validation.ownership.OwnershipValidator;
import gr.balasis.hotel.engine.core.validation.ownership.OwnershipValidatorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest,GuestResource> {
    private final OwnershipValidator ownershipValidator;
    private final ResourceDataValidator resourceDataValidator;
    private final GuestValidator guestValidator;
    private final GuestService guestService;
    private final GuestMapper guestMapper;
    private final ReservationValidator reservationValidator;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;
    private final FeedbackMapper feedbackMapper;

    @Override
    @GetMapping("/{guestId}")
    public ResponseEntity<GuestResource> get(
            @PathVariable final Long guestId) {

        return ResponseEntity.ok(
                guestMapper.toResource(
                        guestService.get(guestId))
        );
    }

    @Override
    @PostMapping
    public ResponseEntity<GuestResource> create(
            @RequestBody final GuestResource guestResource) {

        resourceDataValidator.validateResourceData(guestResource);
        var guest = guestValidator.validate(getMapper().toDomain(guestResource));
        return ResponseEntity.ok(
                getMapper().toResource(
                        getBaseService().create(guest))
        );
    }

    @Override
    @PutMapping("/{guestId}")
    public ResponseEntity<Void> update(
            @PathVariable final Long guestId,
            @RequestBody final GuestResource guestResource) {

        resourceDataValidator.validateResourceData(guestResource);
        guestResource.setId(guestId);
        var guest = guestValidator.validate(getMapper().toDomain(guestResource));
        guestService.update(guest);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> delete(
            @PathVariable final Long guestId) {

        guestService.delete(guestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{guestId}/reservations")
    public ResponseEntity<List<ReservationResource>> findReservations(
            @PathVariable final Long guestId) {

        return ResponseEntity.ok(
                reservationMapper.toResources(
                        reservationService.findByGuestId(guestId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<ReservationResource> getReservation(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        ownershipValidator.validateReservationBelongsToGuest(reservationId,guestId);
        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.get(reservationId))
        );
    }

    @PostMapping("/{guestId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable final Long guestId,
            @RequestBody final ReservationResource reservationResource) {

        resourceDataValidator.validateResourceData(reservationResource);
        var reservation = reservationValidator.validate(reservationMapper.toDomain(reservationResource));
        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.create(reservation))
        );
    }

    @PutMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> updateReservation(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestBody final ReservationResource reservationResource) {

        resourceDataValidator.validateResourceData(reservationResource);
        reservationResource.getGuest().setId(guestId);
        ownershipValidator.validateReservationBelongsToGuest(reservationId,guestId);
        var reservation = reservationValidator.validate(reservationMapper.toDomain(reservationResource));
        reservationService.update(reservation);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> manageReservationAction(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestHeader(value = "a") final String action) {

        reservationService.manageReservationAction(guestId,reservationId,action);
        return ResponseEntity.noContent().build();
    }




    @GetMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getFeedback(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.getFeedback(guestId, reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> getPayment(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.getPayment(guestId, reservationId)) );
    }

    @PostMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> payReservation(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestBody final PaymentResource paymentResource) {

        resourceDataValidator.validateResourceData(paymentResource);
        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.finalizePayment(guestId, reservationId, paymentMapper.toDomain(paymentResource))
        ));
    }

    @PostMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> submitFeedback(
            @PathVariable final Long guestsId,
            @PathVariable final Long reservationId,
            @RequestBody final FeedbackResource feedbackResource) {

        resourceDataValidator.validateResourceData(feedbackResource);
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.createFeedback(guestsId, reservationId, feedbackMapper.toDomain(feedbackResource))
        ));
    }

    @PutMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> updateFeedback(
            @PathVariable final Long guestsId,
            @PathVariable final Long reservationId,
            @RequestBody final FeedbackResource feedbackResource) {

        resourceDataValidator.validateResourceData(feedbackResource);
        reservationService.updateFeedback(guestsId, reservationId, feedbackMapper.toDomain(feedbackResource));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestsId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable final Long guestsId,
            @PathVariable final Long reservationId) {
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
