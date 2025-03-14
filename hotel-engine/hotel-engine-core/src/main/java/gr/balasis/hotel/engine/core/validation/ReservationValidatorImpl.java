package gr.balasis.hotel.engine.core.validation;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.exception.conflict.ReservationConflictException;
import gr.balasis.hotel.context.base.exception.conflict.RoomAvailabilityConflictException;
import gr.balasis.hotel.context.base.exception.notfound.ReservationNotFoundException;
import gr.balasis.hotel.context.base.exception.unauthorized.UnauthorizedAccessException;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.engine.core.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
@AllArgsConstructor
public class ReservationValidatorImpl implements ReservationValidator {

    private final ReservationRepository reservationRepository;

    @Override
    public void validateReservationBelongsToGuest(Long guestId, Reservation reservation) {
        validateReservationBelongsToGuest(guestId, reservation.getId());
    }

    @Override
    public void validateReservationBelongsToGuest(Long reservationId, Long guestId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getGuest().getId().equals(guestId)) {
            throw new UnauthorizedAccessException("Reservation does not belong to the guest");
        }
    }

    @Override
    public void validateFeedbackBelongsToReservation(Long reservationId, Feedback feedback) {
        validateFeedbackBelongsToReservation(reservationId, feedback.getId());
    }

    @Override
    public void validateFeedbackBelongsToReservation(Long reservationId, Long feedbackId) {
        if (!reservationRepository.existsByIdAndFeedbackId(reservationId, feedbackId)) {
            throw new UnauthorizedAccessException("Feedback does not belong to the reservation");
        }
    }

    @Override
    public void validatePaymentBelongsToReservation(Long reservationId, Payment payment) {
        validatePaymentBelongsToReservation(reservationId, payment.getId());
    }

    @Override
    public void validatePaymentBelongsToReservation(Long reservationId, Long paymentId) {
        if (!reservationRepository.existsByIdAndPaymentId(reservationId, paymentId)) {
            throw new UnauthorizedAccessException("Payment does not belong to the reservation");
        }
    }

    @Override
    public void validateRoomAvailability(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isRoomReserved = reservationRepository.existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(
                roomId, checkOutDate, checkInDate);
        if (isRoomReserved) {
            throw new RoomAvailabilityConflictException("Room is already reserved during the specified dates");
        }
    }

    @Override
    public void validateRoomAvailabilityForUpdate(Long roomId, LocalDate checkInDate,
                                                  LocalDate checkOutDate, Long reservationId) {
        boolean isRoomReserved = reservationRepository.existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfterAndIdNot(
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
        Reservation savedReservation = reservationRepository.findById(reservation.getId())
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!Objects.equals(savedReservation.getGuest().getId(), reservation.getGuest().getId())){
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

}
