package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.exception.*;

import gr.balasis.hotel.context.base.service.BasicServiceImpl;

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
public class ReservationServiceImpl extends BasicServiceImpl<Reservation> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final FeedbackRepository feedbackRepository;
    private final GuestRepository guestRepository;
    private final PaymentRepository paymentRepository;


    @Override
    @Transactional
    public Reservation create(final Reservation reservation) {
        return buildReservationWithPayment(reservation);
    }

    public Reservation getReservation(Long guestId, Long reservationId) {
        Reservation reservationEntity = validateReservationExists(reservationId);
        validateReservationOwnership(guestId, reservationId);
        return reservationEntity;
    }

    @Override
    @Transactional
    public Reservation createReservation(Long guestId, Reservation reservation) {
        return buildReservationWithPayment(reservation);
    }

    @Override
    public List<Reservation> findReservations(Long guestId) {
        validateGuestExists(guestId);
        return reservationRepository.findByGuestId(guestId);
    }

    @Override
    @Transactional
    public void cancelReservation(Long guestId, Long reservationId) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        paymentRepository.deleteByReservation(reservation);
        feedbackRepository.deleteByReservation(reservation);
        reservationRepository.deleteById(reservationId);

    }

    @Override
    @Transactional
    public Payment finalizePaymentForReservation(Long guestId, Long reservationId, Payment payment) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        Payment paymentEntity = validatePaymentExists(reservation);
        paymentEntity.setPaymentDate(LocalDateTime.now());
        paymentEntity.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(paymentEntity);
        return paymentEntity;
    }

    @Override
    public Payment getPayment(Long guestId, Long reservationId) {
        return validatePaymentExists(validateReservationOwnership(guestId, reservationId));
    }

    @Override
    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        validateNoExistingFeedback(reservation);
        return feedbackRepository.save(feedback);
    }


    public Feedback getFeedback(Long guestId, Long reservationId) {
        return fetchFeedbackForReservation(guestId, reservationId);
    }

    @Override
    public void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback) {
        Feedback feedback = fetchFeedbackForReservation(guestId, reservationId);
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
    public JpaRepository<Reservation, Long> getRepository() {
        return reservationRepository;
    }


    private Feedback fetchFeedbackForReservation(Long guestId, Long reservationId) {
        return validateFeedbackExists(
                validateReservationOwnership(guestId, reservationId).getId());
    }

    private Reservation validateReservationOwnership(Long guestId, Long reservationId) {
        Reservation reservation = validateReservationExists(reservationId);
        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }
        return reservation;
    }

    private Reservation validateReservationExists(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found: "
                        + reservationId));
    }

    private void validateGuestExists(Long guestId) {
        guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found: " + guestId));
    }

    private Payment validatePaymentExists(Reservation reservation) {
        Payment payment = paymentRepository.getByReservation(reservation);
        if (payment == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return payment;
    }

    private Feedback validateFeedbackExists(Long reservationId) {
        return feedbackRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback not found for reservation: "
                        + reservationId));
    }

    private void validateNoExistingFeedback(Reservation reservation) {
        if (feedbackRepository.existsByReservationId(reservation.getId())) {
            throw new DuplicateFeedbackException("Feedback already exists for this reservation");
        }
    }


    public Reservation buildReservationWithPayment(Reservation reservation){
        validateGuestExists(reservation.getGuest().getId());
        reservation.setCreatedAt(LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);
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
        paymentRepository.save(payment);
    }

}
