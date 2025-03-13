package gr.balasis.hotel.engine.core.validation;

import java.time.LocalDate;

public interface ReservationValidator {
    void validateReservationNotCanceled(Long reservationId);

    void validateReservationBelongsToGuest(Long reservationId, Long guestId);

    void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId);

    void validatePaymentBelongsToReservation(Long reservationId, Long paymentId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
