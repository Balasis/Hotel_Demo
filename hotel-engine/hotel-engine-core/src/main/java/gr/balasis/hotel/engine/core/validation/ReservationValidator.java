package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.model.Reservation;

import java.time.LocalDate;

public interface ReservationValidator {

    void validateReservationNotCanceled(Long reservationId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    Reservation validate(Reservation domain);
}
