package gr.balasis.hotel.engine.core.service;

import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.exception.*;

import gr.balasis.hotel.context.base.service.BasicServiceImpl;
import gr.balasis.hotel.engine.core.repository.GuestRepository;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends BasicServiceImpl<Reservation> implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;


    @Override
    @Transactional
    public Reservation create(final Reservation reservation) {
        return buildAndSaveReservation(reservation);
    }

    @Override
    public Reservation getReservation(Long guestId, Long reservationId) {
        Reservation reservationEntity = validateReservationExists(reservationId);
        validateReservationOwnership(guestId, reservationId);
        return reservationEntity;
    }

    @Override
    @Transactional
    public Reservation createReservation(Long guestId, Reservation reservation) {
        return buildAndSaveReservation(reservation);
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

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new DataConflictException("Reservation is already canceled");
        }

        reservation.setStatus(ReservationStatus.CANCELED);

        if (reservation.getPayment() != null) {
            if (reservation.getPayment().getPaymentStatus() == PaymentStatus.PENDING) {
                reservation.getPayment().setPaymentStatus(PaymentStatus.CANCELLED);
            } else if (reservation.getPayment().getPaymentStatus() == PaymentStatus.PAID) {
                reservation.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
            }
        }

        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Payment finalizePayment(Long guestId, Long reservationId, Payment payment) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        if (reservation.getPayment() == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }

        Payment existingPayment = reservation.getPayment();
        existingPayment.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate() : LocalDateTime.now());
        existingPayment.setPaymentStatus(payment.getPaymentStatus());

        return existingPayment;
    }

    @Override
    @Transactional
    public Payment getPayment(Long guestId, Long reservationId) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        if (reservation.getPayment() == null) {
            throw new PaymentNotFoundException("No payment associated with this reservation");
        }
        return reservation.getPayment();
    }

    @Override
    public Feedback createFeedback(Long guestId, Long reservationId, Feedback feedback) {
        return associateFeedbackWithReservation(guestId, reservationId, feedback).getFeedback();
    }

    @Override
    public void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback) {
        associateFeedbackWithReservation(guestId, reservationId, updatedFeedback);
    }

    @Override
    public Feedback getFeedback(Long guestId, Long reservationId) {
        return fetchFeedbackForReservation(guestId, reservationId);
    }

    @Override
    public void deleteFeedback(Long guestId, Long reservationId) {
        var reservation= validateReservationOwnership(guestId, reservationId);
        if (reservation.getFeedback() == null){
            throw new FeedbackNotFoundException("Feedback doesn't exist to be deleted");
        }
        reservation.setFeedback(null);
        reservationRepository.save(reservation);
    }

    @Override
    public JpaRepository<Reservation, Long> getRepository() {
        return reservationRepository;
    }



    private Reservation associateFeedbackWithReservation(Long guestId, Long reservationId, Feedback feedback) {
        Reservation reservation = validateReservationOwnership(guestId, reservationId);
        reservation.setFeedback(feedback);
        return reservationRepository.save(reservation);
    }

    private Feedback fetchFeedbackForReservation(Long guestId, Long reservationId) {
        return Optional.ofNullable(
                validateReservationOwnership(guestId, reservationId).getFeedback())
                .orElseThrow(
                        ()-> new FeedbackNotFoundException("Feedback not found")
        );
    }

    private Reservation validateReservationOwnership(Long guestId, Long reservationId) {
        return Optional.of(validateReservationExists(reservationId))
                .filter(r -> r.getGuest().getId().equals(guestId))
                .orElseThrow(
                        () -> new UnauthorizedAccessException("Reservation does not belong to the guest"));
    }

    private Reservation validateReservationExists(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(
                        () -> new ReservationNotFoundException("Reservation not found: "
                        + reservationId));
    }

    private void validateGuestExists(Long guestId) {
        guestRepository.findById(guestId)
                .orElseThrow(
                        () -> new GuestNotFoundException("Guest not found: " + guestId));
    }

    private Reservation buildAndSaveReservation(Reservation reservation){
        validateGuestExists(reservation.getGuest().getId());
        reservation.setCreatedAt(LocalDateTime.now());

        Payment payment = new Payment();
        if (reservation.getCheckOutDate() != null) {
            long days = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
            payment.setAmount(reservation.getRoom().getPricePerNight().multiply(BigDecimal.valueOf(days)));
        }
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);

        reservation.setPayment(payment);
        return reservationRepository.save(reservation);
    }

}
