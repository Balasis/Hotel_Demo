package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.domain.Feedback;
import gr.balasis.hotel.context.base.domain.Payment;
import gr.balasis.hotel.context.base.domain.Reservation;
import gr.balasis.hotel.context.base.entity.FeedbackEntity;
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

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        return buildReservationWithPayment(reservation);
    }

    public Reservation getReservation(Long guestId, Long reservationId) {
        ReservationEntity reservationEntity = validateReservationExists(reservationId);
        validateReservationOwnership(guestId, reservationId);
        return reservationMapper.toDomain(reservationEntity);
    }

    @Override
    @Transactional
    public Reservation createReservation(Long guestId, Reservation reservation) {
        return buildReservationWithPayment(reservation);
    }

    @Override
    public List<Reservation> findReservations(Long guestId) {
        validateGuestExists(guestId);
        return reservationMapper.toDomains(reservationRepository.findByGuestId(guestId));
    }

    @Override
    @Transactional
    public void cancelReservation(Long guestId, Long reservationId) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        paymentRepository.deleteByReservation(reservation);
        feedbackRepository.deleteByReservation(reservation);
        reservationRepository.deleteById(reservationId);

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

    @Override
    public Payment getPayment(Long guestId, Long reservationId) {
        return paymentMapper.toDomain(validatePaymentExists(validateReservationOwnership(guestId, reservationId)));
    }

    @Override
    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        ReservationEntity reservation = validateReservationOwnership(guestId, reservationId);
        validateNoExistingFeedback(reservation);
        FeedbackEntity savedFeedback = feedbackRepository.save(feedbackMapper.toEntity(feedback));
        return feedbackMapper.toDomain(savedFeedback);
    }


    public Feedback getFeedback(Long guestId, Long reservationId) {
        return feedbackMapper.toDomain(fetchFeedbackForReservation(guestId, reservationId));
    }

    @Override
    public void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback) {
        FeedbackEntity feedback = fetchFeedbackForReservation(guestId, reservationId);
        feedback.setMessage(updatedFeedback.getMessage());
        feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long guestId, Long reservationId) {
        feedbackRepository.delete(
                fetchFeedbackForReservation(guestId,reservationId)
        );
    }

    @Override
    public JpaRepository<ReservationEntity, Long> getRepository() {
        return reservationRepository;
    }

    @Override
    public BaseMapper<Reservation, ReservationEntity> getMapper() {
        return reservationMapper;
    }


    private FeedbackEntity fetchFeedbackForReservation(Long guestId, Long reservationId) {
        return validateFeedbackExists(
                validateReservationOwnership(guestId, reservationId).getId());
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
        PaymentEntity payment = paymentRepository.getByReservation(reservation);
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


    public Reservation buildReservationWithPayment(Reservation reservation){
        validateGuestExists(reservation.getGuest().getId());
        reservation.setCreatedAt(LocalDateTime.now());
        Reservation savedReservation =reservationMapper.toDomain(
                reservationRepository.save(
                        reservationMapper.toEntity(reservation)));

        initializePayment(savedReservation);

        return savedReservation;
    }

    private void initializePayment(Reservation reservation) {
        Payment payment = new Payment();
        if (reservation.getCheckOutDate() != null) {
            long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            payment.setAmount(reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(days)));
        }
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);
        paymentRepository.save(paymentMapper.toEntity(payment));
    }

}
