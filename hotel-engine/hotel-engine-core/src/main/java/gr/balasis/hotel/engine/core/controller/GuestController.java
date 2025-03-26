package gr.balasis.hotel.engine.core.controller;

import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.controller.BaseController;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.web.resource.FeedbackResource;
import gr.balasis.hotel.context.web.resource.GuestResource;
import gr.balasis.hotel.context.web.resource.PaymentResource;
import gr.balasis.hotel.context.web.resource.ReservationResource;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests")
public class GuestController extends BaseController<Guest, GuestResource> {
    private final ResourceDataValidator resourceDataValidator;
    private final GuestValidator guestValidator;
    private final GuestService guestService;
    private final GuestMapper guestMapper;
    private final ReservationValidator reservationValidator;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final PaymentMapper paymentMapper;
    private final FeedbackMapper feedbackMapper;


    @GetMapping
    public ResponseEntity<List<GuestResource>> findAll(@RequestParam(required = false) String email,
                                                       @RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
                                                       @RequestParam(required = false) LocalDate birthDate) {

        List<Guest> guests = guestService.searchBy(email, firstName, lastName, birthDate);
        return ResponseEntity.ok(guestMapper.toResources(guests));
    }

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
    public ResponseEntity<ReservationResource> getReservation(@PathVariable final Long guestId,
                                                              @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId, guestId);
        return ResponseEntity.ok(
                reservationMapper.toResource(
                        reservationService.get(reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> getReservationFeedback(@PathVariable final Long guestId,
                                                                   @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId, guestId);
        return ResponseEntity.ok(feedbackMapper.toResource(
                reservationService.getFeedback(reservationId))
        );
    }

    @GetMapping("/{guestId}/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentResource> getReservationPayment(@PathVariable final Long guestId,
                                                                 @PathVariable final Long reservationId) {

        reservationValidator.validateReservationBelongsToGuest(reservationId, guestId);
        return ResponseEntity.ok(paymentMapper.toResource(
                reservationService.getPayment(reservationId)));
    }

    @GetMapping("/{guestId}/reservations")
    public ResponseEntity<List<ReservationResource>> findReservations(@PathVariable final Long guestId) {

        return ResponseEntity.ok(reservationMapper.toResources(
                reservationService.findByGuestId(guestId)
        ));
    }

    @Override
    @PostMapping
    public ResponseEntity<GuestResource> create(@RequestBody final GuestResource guestResource) {

        resourceDataValidator.validateResourceData(guestResource);
        guestResource.setId(null);
        var guest = guestValidator.validate(guestMapper.toDomain(guestResource));
        return ResponseEntity.ok(
                guestMapper.toResource(
                        guestService.create(guest))
        );
    }

    @PostMapping("/{guestId}/reservations")
    public ResponseEntity<ReservationResource> createReservation(@PathVariable final Long guestId,
                                                                 @RequestBody final ReservationResource reservationResource) {

        resourceDataValidator.validateResourceData(reservationResource);
        reservationResource.setId(null);
        reservationResource.getGuest().setId(guestId);
        var reservation = reservationValidator.validate(reservationMapper.toDomain(reservationResource));
        return ResponseEntity.ok(reservationMapper.toResource(reservationService.create(reservation)));
    }

    @PostMapping("/{guestId}/reservations/{reservationId}/feedback")
    public ResponseEntity<FeedbackResource> createReservationFeedback(@PathVariable final Long guestId,
        @PathVariable final Long reservationId, @RequestBody final FeedbackResource feedbackResource) {

        resourceDataValidator.validateResourceData(feedbackResource);
        feedbackResource.setId(null);
        var feedback = reservationValidator.validateFeedback(reservationId, guestId, feedbackMapper.toDomain(feedbackResource));
        return ResponseEntity.ok(feedbackMapper.toResource(reservationService.createFeedback(reservationId, feedback)));
    }

    @Override
    @PutMapping("/{guestId}")
    public ResponseEntity<Void> update(@PathVariable final Long guestId, @RequestBody final GuestResource guestResource) {

        resourceDataValidator.validateResourceData(guestResource);
        guestResource.setId(guestId);
        var guest = guestValidator.validate(getMapper().toDomain(guestResource));
        guestService.update(guest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> updateReservation(@PathVariable final Long guestId,
                                                  @PathVariable final Long reservationId, @RequestBody final ReservationResource reservationResource) {

        resourceDataValidator.validateResourceData(reservationResource);
        reservationResource.getGuest().setId(guestId);
        var reservation = reservationValidator.validateForUpdate(reservationId, reservationMapper.toDomain(reservationResource));
        reservationService.update(reservation);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{guestId}/reservations/{reservationId}/{feedbackId}")
    public ResponseEntity<Void> updateReservationFeedback(@PathVariable final Long guestId, @PathVariable final Long reservationId,
                                                          @RequestBody final FeedbackResource feedbackResource, @PathVariable final Long feedbackId) {

        resourceDataValidator.validateResourceData(feedbackResource);
        feedbackResource.setId(feedbackId);
        var feedback = reservationValidator.validateFeedbackForUpdate(reservationId, guestId, feedbackMapper.toDomain(feedbackResource));
        reservationService.updateFeedback(reservationId, feedback);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{guestId}/reservations/{reservationId}")
    public ResponseEntity<Void> manageReservationAction(@PathVariable final Long guestId,
                                                        @PathVariable final Long reservationId, @RequestHeader(value = "a") final String action) {

        reservationValidator.validateReservationBelongsToGuest(reservationId, guestId);
        reservationService.manageReservationAction(reservationId, action);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> delete(@PathVariable final Long guestId) {

        guestService.delete(guestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestId}/reservations/{reservationId}/feedback/{feedbackId}")
    public ResponseEntity<Void> deleteReservationFeedback(@PathVariable final Long guestId,
                                                          @PathVariable final Long reservationId, @PathVariable Long feedbackId) {

        reservationValidator.checkIfFeedbackCanBeDeleted(reservationId, guestId);
        reservationService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }

    @Override
    protected BaseService<Guest, Long> getBaseService() {
        return guestService;
    }

    @Override
    protected BaseMapper<Guest, GuestResource> getMapper() {
        return guestMapper;
    }

    @Override
    protected ResourceDataValidator resourceDatavalidator() {
        return resourceDataValidator;
    }

}