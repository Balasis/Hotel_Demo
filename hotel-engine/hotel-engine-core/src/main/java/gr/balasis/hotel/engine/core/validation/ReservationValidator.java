package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Reservation;

import java.time.LocalDate;

public interface ReservationValidator {

    void validateReservationBelongsToGuest(Long reservationId, Long guestId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long reservationId);

    Reservation validate(Reservation domain);

    Reservation validateForUpdate(Reservation reservation);

    void reservationFeedbackValidations(Long reservationId, Long guestId);
}
