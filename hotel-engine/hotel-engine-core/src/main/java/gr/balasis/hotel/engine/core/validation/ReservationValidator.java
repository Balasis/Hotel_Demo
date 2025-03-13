package gr.balasis.hotel.engine.core.validation;

import java.time.LocalDate;

public interface ReservationValidator {

    void validateReservationNotCanceled(Long reservationId);

    void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
