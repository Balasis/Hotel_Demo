package gr.balasis.hotel.modules.feedback.service;

import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.web.exception.EntityNotFoundException;
import gr.balasis.hotel.context.web.exception.UnauthorizedAccessException;
import gr.balasis.hotel.data.entity.GuestEntity;
import gr.balasis.hotel.data.entity.ReservationEntity;
import gr.balasis.hotel.data.repository.GuestRepository;
import gr.balasis.hotel.data.repository.ReservationRepository;
import gr.balasis.hotel.modules.feedback.domain.Feedback;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import gr.balasis.hotel.modules.feedback.exception.web.DuplicateFeedbackException;
import gr.balasis.hotel.modules.feedback.exception.web.FeedBackMismatchException;
import gr.balasis.hotel.modules.feedback.mapper.FeedbackMapper;
import gr.balasis.hotel.modules.feedback.repository.FeedbackRepository;
import gr.balasis.hotel.modules.feedback.resource.FeedbackResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl extends BasicServiceImpl<Feedback, FeedbackResource, FeedbackEntity> implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final GuestRepository guestRepository;
    private final ReservationRepository reservationRepository;

    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(feedback, reservation);
        validateNoDuplicateFeedback(reservation);

        FeedbackEntity feedbackEntity = feedbackMapper.toEntity(feedback);
        FeedbackEntity savedEntity = feedbackRepository.save(feedbackEntity);
        return feedbackMapper.toDomainFromEntity(savedEntity);
    }

    public Feedback getFeedbackById(Long guestId, Long reservationId) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);

        FeedbackEntity feedback = validateFeedbackExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(feedback, reservation);
        validateFeedbackBelongsToGuest(feedback, guest);

        return feedbackMapper.toDomainFromEntity(feedback);
    }

    public void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);
        FeedbackEntity existingFeedback = validateFeedbackExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(existingFeedback, reservation);
        validateFeedbackBelongsToGuest(existingFeedback, guest);

        existingFeedback.setMessage(updatedFeedback.getMessage());

        feedbackRepository.save(existingFeedback);
    }

    public void deleteFeedback(Long guestId, Long reservationId) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);

        FeedbackEntity feedback = validateFeedbackExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(feedback, reservation);
        validateFeedbackBelongsToGuest(feedback, guest);

        feedbackRepository.delete(feedback);
    }

    public boolean feedbackExistsForReservationId(Long reservationId) {
        return feedbackRepository.existsByReservationId(reservationId);
    }

    private GuestEntity validateGuestExists(Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with ID: " + guestId));
    }

    private ReservationEntity validateReservationExists(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + reservationId));
    }

    private void validateReservationBelongsToGuest(GuestEntity guest, ReservationEntity reservation) {
        if (!reservation.getGuest().getId().equals(guest.getId())) {
            throw new UnauthorizedAccessException("Reservation does not belong to the given guest");
        }
    }

    private void validateFeedbackBelongsToReservation(FeedbackEntity feedback, ReservationEntity reservation) {
        if (!feedback.getReservationId().equals(reservation.getId())) {
            throw new FeedBackMismatchException("Feedback does not belong to the provided reservation");
        }
    }

    private void validateFeedbackBelongsToReservation(Feedback feedback, ReservationEntity reservation) {
        if (!feedback.getReservationId().equals(reservation.getId())) {
            throw new FeedBackMismatchException("Feedback does not belong to the provided reservation");
        }
    }

    private void validateNoDuplicateFeedback(ReservationEntity reservation) {
        boolean feedbackExists = feedbackRepository.existsByReservationId(reservation.getId());
        if (feedbackExists) {
            throw new DuplicateFeedbackException("Feedback already exists for this reservation");
        }
    }

    private FeedbackEntity validateFeedbackExists(Long reservationId) {
        return feedbackRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with reservationID: "
                        + reservationId));
    }

    private void validateFeedbackBelongsToGuest(FeedbackEntity feedback, GuestEntity guest) {
        if (!feedback.getGuest().getId().equals(guest.getId())) {
            throw new UnauthorizedAccessException("Feedback does not belong to the given guest");
        }
    }

    @Override
    public JpaRepository<FeedbackEntity, Long> getRepository() {
        return feedbackRepository;
    }

    @Override
    public BaseMapper<Feedback, FeedbackResource, FeedbackEntity> getMapper() {
        return feedbackMapper;
    }
}
