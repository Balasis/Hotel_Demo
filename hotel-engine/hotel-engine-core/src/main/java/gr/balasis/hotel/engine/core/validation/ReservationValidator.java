package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Reservation;

import java.time.LocalDate;

public interface ReservationValidator extends BaseValidator<Reservation>{

    void validateReservationBelongsToGuest(Long reservationId, Long guestId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long reservationId);

    Feedback validateFeedback(Long reservationId, Long guestId, Feedback feedback);

    Feedback validateFeedbackForUpdate(Long reservationId, Long guestId, Feedback domain);

    void checkIfFeedbackCanBeDeleted(Long reservationId, Long guestId);
}
