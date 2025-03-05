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
import gr.balasis.hotel.engine.core.mapper.base.PaymentMapper;
import gr.balasis.hotel.engine.core.mapper.base.ReservationMapper;
import gr.balasis.hotel.engine.core.mapper.base.FeedbackMapper;

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

    public Reservation getReservation(Long guestId, Long reservationId) {

        ReservationEntity reservationEntity = validateReservationExists(reservationId);
        validateReservationOwnership(guestId, reservationId);
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
        return reservationMapper.toDomains(reservationRepository.findByGuestId(guestId));
    }

    @Override
    public void cancelReservation(Long guestId, Long reservationId) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        reservationRepository.delete(reservation);
    }

    @Override
    @Transactional
    public Payment finalizePaymentForReservation(Long guestId, Long reservationId, Payment payment) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        PaymentEntity paymentEntity = validatePaymentExists(reservation);
        paymentEntity.setPaymentDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(paymentEntity);
        return paymentMapper.toDomain(paymentEntity);
    }

    public Payment getPaymentForReservation(Long guestId, Long reservationId) {
        return paymentMapper.toDomain(validatePaymentExists(validateReservationOwnership(guestId, reservationId)));
    }

    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        validateNoExistingFeedback(reservation);
        FeedbackEntity savedFeedback = feedbackRepository.save(feedbackMapper.toEntity(feedback));
        return feedbackMapper.toDomain(savedFeedback);
    }

    public Feedback getFeedbackByReservation(Long guestId, Long reservationId) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        FeedbackEntity feedback = validateFeedbackExists(reservation.getId());
        return feedbackMapper.toDomain(feedback);
    }

    public void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        FeedbackEntity feedback = validateFeedbackExists(reservation.getId());
        feedback.setMessage(updatedFeedback.getMessage());
        feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Long guestId, Long reservationId) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        FeedbackEntity feedback = validateFeedbackExists(reservation.getId());
        feedbackRepository.delete(feedback);
    }

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationEntity> getMapper() {
        return reservationMapper;
    }

    private ReservationEntity validateReservationOwnership(Long guestId, Long reservationId) {
        ReservationEntity reservation = validateReservationExists(reservationId);
        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }
        return reservation;
    }

    private ReservationEntity validateReservationExists(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: "
                        + reservationId));
    }

    private void validateGuestExists(Long guestId) {
        guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found: " + guestId));
    }

    private PaymentEntity validatePaymentExists(ReservationEntity reservation) {
        PaymentEntity payment = reservation.getPayment();
        if (payment == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return payment;
    }

    private FeedbackEntity validateFeedbackExists(Long reservationId) {
        return feedbackRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback not found for reservation: "
                        + reservationId));
    }

    private void validateNoExistingFeedback(ReservationEntity reservation) {
        if (feedbackRepository.existsByReservationId(reservation.getId())) {
            throw new DuplicateFeedbackException("Feedback already exists for this reservation");
        }
    }

    private void validateFeedbackBelongsToGuest(FeedbackEntity feedback, GuestEntity guest) {
        if (!feedback.getGuest().getId().equals(guest.getId())) {
            throw new UnauthorizedAccessException("Feedback does not belong to this guest");
        }
    }


    private Reservation buildReservationWithPayment(Reservation reservation) {
        Payment payment = initializePayment(reservation);
        reservation.setPayment(payment);
        reservation.setCreatedAt(LocalDateTime.now());
        return reservationMapper.toDomain(reservationRepository.save(reservationMapper.toEntity(reservation)));
    }

    private Payment initializePayment(Reservation reservation) {
        Payment payment = new Payment();
        if (reservation.getCheckOutDate() != null) {
            long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            payment.setAmount(reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(days)));
        }
        payment.setPaymentStatus(PaymentStatus.PENDING);
        return payment;
    }

}
