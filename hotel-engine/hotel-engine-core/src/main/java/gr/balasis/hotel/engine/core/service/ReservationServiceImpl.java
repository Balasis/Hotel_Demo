package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.entity.FeedbackEntity;
import gr.balasis.hotel.context.base.entity.GuestEntity;
import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.exception.*;
import gr.balasis.hotel.context.base.mapper.BaseMapper;
import gr.balasis.hotel.context.base.service.BasicServiceImpl;



import gr.balasis.hotel.context.base.entity.PaymentEntity;
import gr.balasis.hotel.context.base.entity.ReservationEntity;
import gr.balasis.hotel.engine.core.mapper.PaymentMapper;
import gr.balasis.hotel.engine.core.mapper.ReservationMapper;
import gr.balasis.hotel.engine.core.mapper.FeedbackMapper;

import gr.balasis.hotel.engine.core.repository.FeedbackRepository;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import gr.balasis.hotel.engine.core.repository.PaymentRepository;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation,ReservationEntity> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final FeedbackRepository feedbackRepository;
    private final GuestRepository guestRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationMapper reservationMapper;
    private final FeedbackMapper feedbackMapper;
    private final PaymentMapper paymentMapper;


    @Override
    @Transactional
    public Reservation create(final Reservation reservation) {
        validateGuestExists(reservation.getGuest().getId());
        return buildReservationWithPayment(reservation);
    }

    public Reservation findReservationById(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        return reservationMapper.toDomain(reservationEntity);
    }

    @Override
    @Transactional
    public Reservation createReservationForGuest(Long guestId, Reservation reservation) {
        validateGuestIdMatch(guestId, reservation);
        validateGuestExists(reservation.getGuest().getId());
        return buildReservationWithPayment(reservation);
    }

    @Override
    public List<Reservation> findReservationsByGuestId(Long guestId) {
        validateGuestExists(guestId);
        return reservationRepository.findByGuestId(guestId).stream()
                .map(reservationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);

        if (!reservationEntity.getGuest().getId().equals(guestId)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }
        reservationRepository.delete(reservationEntity);
    }

    @Override
    @Transactional
    public Payment finalizePaymentForReservation(Long guestId, Long reservationId, Payment payment) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        PaymentEntity paymentEntity = getValidPayment(reservationEntity);
        paymentEntity.setPaymentDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(paymentEntity);

        return paymentMapper.toDomain(paymentEntity);
    }

    public Payment getPaymentForReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = getValidReservation(guestId, reservationId);
        PaymentEntity paymentEntity = getValidPayment(reservationEntity);

        return paymentMapper.toDomain(paymentEntity);
    }

    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(feedback, reservation);
        validateNoDuplicateFeedback(reservation);

        FeedbackEntity feedbackEntity = feedbackMapper.toEntity(feedback);
        FeedbackEntity savedEntity = feedbackRepository.save(feedbackEntity);
        return feedbackMapper.toDomain(savedEntity);
    }

    public Feedback getFeedbackById(Long guestId, Long reservationId) {
        GuestEntity guest = validateGuestExists(guestId);
        ReservationEntity reservation = validateReservationExists(reservationId);

        FeedbackEntity feedback = validateFeedbackExists(reservationId);

        validateReservationBelongsToGuest(guest, reservation);
        validateFeedbackBelongsToReservation(feedback, reservation);
        validateFeedbackBelongsToGuest(feedback, guest);

        return feedbackMapper.toDomain(feedback);
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

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation,ReservationEntity> getMapper() {
        return reservationMapper;
    }

    private GuestEntity validateGuestExists(Long guestId) {
        return guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with ID: " + guestId));
    }

    private void validateGuestIdMatch(Long guestId, Reservation reservation) {
        if (!guestId.equals(reservation.getGuest().getId())) {
            throw new IllegalArgumentException("Provided guest ID does not match the reservation guest ID");
        }
    }

    private void validateReservationOwnership(Long guestId, ReservationEntity reservationEntity) {
        if (!reservationEntity.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("This reservation does not belong to the guest");
        }
    }

    private Reservation buildReservationWithPayment(Reservation reservation) {
        Payment payment = new Payment();
        if (reservation.getCheckOutDate() != null) {
            long daysStayed = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            payment.setAmount(
                    reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(daysStayed))
            );
        }
        payment.setPaymentStatus(PaymentStatus.PENDING);
        reservation.setPayment(payment);
        reservation.setCreatedAt(LocalDateTime.now());
        return reservationMapper.toDomain(reservationRepository.save(reservationMapper.toEntity(reservation)));
    }

    private ReservationEntity getValidReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        validateReservationOwnership(guestId, reservationEntity);

        return reservationEntity;
    }

    private PaymentEntity getValidPayment(ReservationEntity reservationEntity) {
        PaymentEntity paymentEntity = reservationEntity.getPayment();
        if (paymentEntity == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return paymentEntity;
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

}
