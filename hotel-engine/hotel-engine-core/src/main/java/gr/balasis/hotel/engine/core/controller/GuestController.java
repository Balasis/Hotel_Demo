package gr.balasis.hotel.engine.core.controller;


import gr.balasis.hotel.context.base.model.Guest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest,GuestResource> {
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

    @GetMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<ReservationResource> getReservation(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId,guestId);
        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.get(reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getReservationFeedback(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId,guestId);
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.getFeedback(reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> getReservationPayment(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId,guestId);
        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.getPayment(reservationId)) );
    }

    @GetMapping("/{guestId}/reservations")
    public ResponseEntity<List<ReservationResource>> findReservations(
            @PathVariable final Long guestId) {
        return ResponseEntity.ok(reservationMapper.toResources(
                reservationService.findByGuestId(guestId)
        ));
    }

    @Override
    @PostMapping
    public ResponseEntity<GuestResource> create(
            @RequestBody final GuestResource guestResource) {

        guestResource.setId(null);
        resourceDataValidator.validateResourceData(guestResource);
        var guest = guestValidator.validate(guestMapper.toDomain(guestResource));
        return ResponseEntity.ok(
                guestMapper.toResource(
                        guestService.create(guest))
        );
    }

    @PostMapping("/{guestId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(
            @PathVariable final Long guestId,
            @RequestBody final ReservationResource reservationResource) {

        reservationResource.setId(null);
        reservationResource.getGuest().setId(guestId);
        resourceDataValidator.validateResourceData(reservationResource);
        var reservation = reservationValidator.validate(reservationMapper.toDomain(reservationResource));
        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.create(reservation))
        );
    }

    @PostMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> createReservationFeedback(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestBody final FeedbackResource feedbackResource) {

        feedbackResource.setId(null);
        resourceDataValidator.validateResourceData(feedbackResource);
        reservationValidator.reservationFeedbackValidations(reservationId,guestId);
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.createFeedback(reservationId, feedbackMapper.toDomain(feedbackResource))
        ));
    }


    @Override
    @PutMapping("/{guestId}")
    public ResponseEntity<Void> update(
            @PathVariable final Long guestId,
            @RequestBody final GuestResource guestResource) {

        guestResource.setId(guestId);
        resourceDataValidator.validateResourceData(guestResource);
        var guest = guestValidator.validate(getMapper().toDomain(guestResource));
        guestService.update(guest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> updateReservation(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestBody final ReservationResource reservationResource) {

        reservationResource.getGuest().setId(guestId);
        reservationResource.setId(reservationId);
        resourceDataValidator.validateResourceData(reservationResource);
        var reservation = reservationValidator.validateForUpdate(reservationMapper.toDomain(reservationResource));
        reservationService.update(reservation);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> updateReservationFeedback(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestBody final FeedbackResource feedbackResource) {

        resourceDataValidator.validateResourceData(feedbackResource);
        reservationValidator.validateReservationBelongsToGuest(reservationId,guestId);
        reservationService.updateFeedback(reservationId, feedbackMapper.toDomain(feedbackResource));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> manageReservationAction(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId,
            @RequestHeader(value = "a") final String action) {

        reservationValidator.validateReservationBelongsToGuest(reservationId,guestId);
        reservationService.manageReservationAction(reservationId,action);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> delete(
            @PathVariable final Long guestId) {

        guestService.delete(guestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<Void> deleteReservationFeedback(
            @PathVariable final Long guestId,
            @PathVariable final Long reservationId) {

        reservationValidator.checkIfFeedbackCanBeDeleted(reservationId,guestId);
        reservationService.deleteFeedback(reservationId);
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