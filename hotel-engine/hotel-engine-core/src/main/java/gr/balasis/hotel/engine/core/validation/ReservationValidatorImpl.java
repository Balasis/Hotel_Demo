package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.HotelException;
import gr.balasis.hotel.context.base.exception.conflict.ReservationConflictException;
import gr.balasis.hotel.context.base.exception.conflict.RoomAvailabilityConflictException;
import gr.balasis.hotel.context.base.exception.notfound.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.unauthorized.UnauthorizedAccessException;
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
    public void validateReservationBelongsToGuest(Long reservationId, Long guestId) {
        Reservation reservation = reservationRepository.findReservationByIdCompleteFetch(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }
    }

    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isRoomReserved = reservationRepository.existsReservationConflict(
                roomId, checkOutDate, checkInDate);
        if (isRoomReserved) {
            throw new RoomAvailabilityConflictException("Room is already reserved during the specified dates");
        }
    }

    @Override
    public void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate,
                                                  LocalDate checkOutDate, Long reservationId) {
        boolean isRoomReserved = reservationRepository.existsReservationConflictExcludeSelf(
                roomId, checkOutDate, checkInDate, reservationId);

        if (isRoomReserved) {
            throw new RoomAvailabilityConflictException("Room is already reserved during the specified dates");
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

    @Override
    public Reservation validateForUpdate(Reservation reservation) {
        Reservation savedReservation = reservationRepository.findReservationByIdCompleteFetch(reservation.getId())
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!savedReservation.getGuest().getId().equals(reservation.getGuest().getId())){
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }

        if (savedReservation.getPayment().getPaymentStatus() == PaymentStatus.PAID){
            throw new ReservationConflictException("Can not update an already paid reservation");
        }

        validateRoomAvailabilityForUpdate(
                reservation.getRoom().getId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getId()
        );

        return reservation;
    }

    @Override
    public void reservationFeedbackValidations(Long reservationId, Long guestId) {
        Reservation reservation = reservationRepository.findReservationByIdCompleteFetch(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
        if (!reservation.getGuest().getId().equals(guestId)){
            throw new UnauthorizedAccessException("The feedback target reservation('"+reservationId+
                    "')does not belong to guest('"+guestId+"')");
        }
        if(reservation.getStatus().equals(ReservationStatus.CANCELED)){
        //TODO:Change the above to be allowed only at completed reservations. Leave it now for testing as "canceled")
            throw new HotelException("Feedback is not allowed to canceled reservations");
        }

    }

}
