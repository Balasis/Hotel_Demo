package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.DataConflictException;
import gr.balasis.hotel.context.base.exception.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.RoomNotAvailableException;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ReservationValidatorImpl implements ReservationValidator {

    private final ReservationRepository reservationRepository;

    @Override
    public void validateReservationNotCanceled(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new DataConflictException("Reservation is already canceled");
        }
    }

    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isRoomReserved = reservationRepository.existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(
                roomId, checkOutDate, checkInDate);
        if (isRoomReserved) {
            throw new RoomNotAvailableException("Room is already reserved during the specified dates");
        }
    }

    @Override
    public Reservation validate(Reservation reservation) {
        validateRoomAvailability(
                reservation.getRoom().getId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        return reservation;
    }

}
