package gr.balasis.hotel.engine.core.validation.ownership;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;

public interface OwnershipValidator {

    void validateReservationBelongsToGuest(Long guestId, Reservation reservation);

    void validateReservationBelongsToGuest(Long reservationId, Long guestId);

    void validateFeedbackBelongsToReservation(Long reservationId, Feedback feedback);

    void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId);

    void validatePaymentBelongsToReservation(Long reservationId, Payment payment);

    void validatePaymentBelongsToReservation(Long reservationId, Long paymentId);
}
