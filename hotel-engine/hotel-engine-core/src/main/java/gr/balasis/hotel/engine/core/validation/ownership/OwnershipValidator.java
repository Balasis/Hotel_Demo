package gr.balasis.hotel.engine.core.validation.ownership;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;

public interface OwnershipValidator {
    void validateGuestReservation(Long guestId, Reservation reservation);

    void validateGuestReservation(Long guestId, Long reservationId);

    void validateReservationFeedback(Long reservationId, Feedback feedback);

    void validateReservationFeedback(Long reservationId, Long feedbackId);

    void validateReservationPayment(Long reservationId, Payment payment);

    void validateReservationPayment(Long reservationId, Long paymentId);
}
