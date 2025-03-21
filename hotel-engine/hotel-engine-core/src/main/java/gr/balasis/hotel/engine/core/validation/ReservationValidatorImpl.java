package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.exception.HotelException;
import gr.balasis.hotel.context.base.exception.conflict.RoomAvailabilityConflictException;
import gr.balasis.hotel.context.base.exception.corrupted.CorruptedReservationModelException;
import gr.balasis.hotel.context.base.exception.notfound.FeedbackNotFoundException;
import gr.balasis.hotel.context.base.exception.unauthorized.UnauthorizedAccessException;
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
    public void validateReservationBelongsToGuest(Long reservationId, Long guestId) {
        if (!reservationRepository.reservationBelongsToGuest(reservationId, guestId)) {
            throw new UnauthorizedAccessException("Reservation does not exist or doesn't belong to the guest");
        }
    }

    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        if (!reservationRepository.isRoomAvailableOn(roomId, checkOutDate, checkInDate)) {
            throw new RoomAvailabilityConflictException("Room is already reserved during the specified dates");
        }
    }

    @Override
    public void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate,
                                                  LocalDate checkOutDate, Long reservationId) {
        if (!reservationRepository.isRoomAvailableExcludeReservationOn(roomId, checkOutDate, checkInDate, reservationId)) {
            throw new RoomAvailabilityConflictException("Room is already reserved during the specified dates");
        }
    }


    @Override
    public Reservation validate(Reservation reservation) {
        if (reservation.getPayment() != null) {
            throw new HotelException("Payment should not be sent since its been exclusively generated server side.");
        }
        validateRoomAvailability(
                reservation.getRoom().getId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        return reservation;
    }

    @Override
    public Reservation validateForUpdate(Long id, Reservation reservation) {
        validateReservationBelongsToGuest(reservation.getId(), reservation.getGuest().getId());
        if (reservationRepository.getReservationPaymentStatus(reservation.getId()).equals(PaymentStatus.PAID)) {
            throw new HotelException("Can not update paid reservation");
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
    public Feedback validateFeedback(Long reservationId, Long guestId, Feedback feedback) {
        return commonFeedbackValidationForUpdateAndCreate(reservationId, guestId,feedback);
    }

    @Override
    public Feedback validateFeedbackForUpdate(Long reservationId, Long guestId, Feedback feedback) {
        return commonFeedbackValidationForUpdateAndCreate(reservationId, guestId,feedback);
    }

    @Override
    public void checkIfFeedbackCanBeDeleted(Long reservationId, Long guestId) {
        validateReservationBelongsToGuest(reservationId, guestId);
        if (!reservationRepository.doesFeedbackExist(reservationId)) {
            throw new FeedbackNotFoundException("Theres no feedback to delete for reservation: " + reservationId);
        }
    }

    private Feedback commonFeedbackValidationForUpdateAndCreate(Long reservationId, Long guestId,Feedback feedback) {
        validateReservationBelongsToGuest(reservationId, guestId);
        var reservationStatus = reservationRepository.getReservationStatus(reservationId).orElseThrow(
                () -> new CorruptedReservationModelException("Reservation status could not be found (corrupted data)")
        );
        if (reservationStatus.equals(ReservationStatus.CANCELED)) {
            //TODO:Change the above to be allowed only at completed reservations. Leave it now for testing as "canceled")
            throw new HotelException("Feedback is not allowed to canceled reservations");
        }
        if (feedback.getId() != null &&
                !reservationRepository.doesFeedbackBelongsToReservation(feedback.getId(), reservationId) ) {
            throw new UnauthorizedAccessException("Feedback does not belong to the reservation");
        }
        return feedback;
    }

}
