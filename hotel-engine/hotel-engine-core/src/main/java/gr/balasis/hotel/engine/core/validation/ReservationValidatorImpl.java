package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
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
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new IllegalArgumentException("Reservation is already canceled");
        }
    }

    @Override
    public void validateReservationBelongsToGuest(Long reservationId, Long guestId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new IllegalArgumentException("Reservation does not belong to the guest");
        }
    }

    @Override
    public void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId) {
        if (!reservationRepository.existsByIdAndFeedbackId(reservationId, feedbackId)) {
            throw new IllegalArgumentException("Feedback does not belong to the reservation");
        }
    }

    @Override
    public void validatePaymentBelongsToReservation(Long reservationId, Long paymentId) {
        if (!reservationRepository.existsByIdAndPaymentId(reservationId, paymentId)) {
            throw new IllegalArgumentException("Payment does not belong to the reservation");
        }
    }

    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isRoomReserved = reservationRepository.existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(
                roomId, checkOutDate, checkInDate);
        if (isRoomReserved) {
            throw new IllegalArgumentException("Room is already reserved during the specified dates");
        }
    }


}
