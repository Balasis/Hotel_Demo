package gr.balasis.hotel.engine.core.validation.ownership;

import gr.balasis.hotel.context.base.exception.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.UnauthorizedAccessException;
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
    public void validateReservationBelongsToGuest(Long guestId, Reservation reservation) {
        validateReservationBelongsToGuest(guestId, reservation.getId());
    }

    @Override
    public void validateReservationBelongsToGuest(Long reservationId, Long guestId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }
    }

    @Override
    public void validateFeedbackBelongsToReservation(Long reservationId, Feedback feedback) {
        validateFeedbackBelongsToReservation(reservationId, feedback.getId());
    }

    @Override
    public void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId) {
        if (!reservationRepository.existsByIdAndFeedbackId(reservationId, feedbackId)) {
            throw new UnauthorizedAccessException("Feedback does not belong to the reservation");
        }
    }

    @Override
    public void validatePaymentBelongsToReservation(Long reservationId, Payment payment) {
        validatePaymentBelongsToReservation(reservationId, payment.getId());
    }

    @Override
    public void validatePaymentBelongsToReservation(Long reservationId, Long paymentId) {
        if (!reservationRepository.existsByIdAndPaymentId(reservationId, paymentId)) {
            throw new UnauthorizedAccessException("Payment does not belong to the reservation");
        }
    }

}
