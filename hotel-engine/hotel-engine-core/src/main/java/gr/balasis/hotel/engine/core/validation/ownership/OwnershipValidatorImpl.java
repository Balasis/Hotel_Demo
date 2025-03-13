package gr.balasis.hotel.engine.core.validation.ownership;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnershipValidatorImpl implements OwnershipValidator{
    private final ReservationRepository reservationRepository;

    @Override
    public void validateGuestReservation(Long guestId, Reservation reservation) {
        validateGuestReservation(guestId, reservation.getId());
    }

    @Override
    public void validateGuestReservation(Long guestId, Long reservationId) {
        if (reservationRepository.existsByIdAndGuestId(reservationId, guestId)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }
    }

    @Override
    public void validateReservationFeedback(Long reservationId, Feedback feedback) {
        validateReservationFeedback(reservationId, feedback.getId());
    }

    @Override
    public void validateReservationFeedback(Long reservationId, Long feedbackId) {
        if (!reservationRepository.existsByFeedbackIdAndId(reservationId ,feedbackId)) {
            throw new IllegalArgumentException("Feedback does not belong to the reservation");
        }
    }

    @Override
    public void validateReservationPayment(Long reservationId, Payment payment) {
        validateReservationPayment(reservationId, payment.getId());
    }

    @Override
    public void validateReservationPayment(Long reservationId, Long paymentId) {
        if (!reservationRepository.existsByPaymentIdAndId(reservationId,paymentId)) {
            throw new IllegalArgumentException("Payment does not belong to the reservation");
        }
    }

}
