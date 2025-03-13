package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;

import java.time.LocalDate;

public interface ReservationValidator {

    void validateReservationBelongsToGuest(Long guestId, Reservation reservation);

    void validateReservationBelongsToGuest(Long reservationId, Long guestId);

    void validateFeedbackBelongsToReservation(Long reservationId, Feedback feedback);

    void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId);

    void validatePaymentBelongsToReservation(Long reservationId, Payment payment);

    void validatePaymentBelongsToReservation(Long reservationId, Long paymentId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long reservationId);

    Reservation validate(Reservation domain);

    Reservation validateForUpdate(Reservation reservation);
}
