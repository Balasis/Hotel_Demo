package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
    SELECT r
    FROM Reservation r
    JOIN FETCH r.guest
    JOIN FETCH r.room
    LEFT JOIN FETCH r.feedback
    JOIN FETCH r.payment
    WHERE r.id = :reservationId
    """)
    Optional<Reservation> findByIdCompleteFetch(Long reservationId);

    @Query("""
    SELECT r.feedback
    FROM Reservation r
    WHERE r.id = :reservationId
    """)
    Optional<Feedback> getFeedbackByReservationId(Long reservationId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.guest g JOIN FETCH r.room where g.id= :guestId")
    List<Reservation> findByGuestId(Long guestId);

    boolean existsByIdAndFeedbackId(Long reservationId, Long feedbackId);
    boolean existsByIdAndPaymentId(Long reservationId, Long paymentId);
    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(Long roomId, LocalDate checkOutDate, LocalDate checkInDate);


    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfterAndIdNot(Long roomId, LocalDate checkOutDate,
                                                                           LocalDate checkInDate, Long reservationId);
}
