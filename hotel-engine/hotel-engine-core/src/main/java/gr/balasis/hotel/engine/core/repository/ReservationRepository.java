package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Guest;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Long guestId);

    boolean existsByIdAndGuestId(Long reservationId, Long guestId);
    boolean existsByIdAndFeedbackId(Long reservationId, Long feedbackId);
    boolean existsByIdAndPaymentId(Long reservationId, Long paymentId);
    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(Long roomId, LocalDate checkOutDate, LocalDate checkInDate);


    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfterAndIdNot(Long roomId, LocalDate checkOutDate,
                                                                           LocalDate checkInDate, Long reservationId);
}
