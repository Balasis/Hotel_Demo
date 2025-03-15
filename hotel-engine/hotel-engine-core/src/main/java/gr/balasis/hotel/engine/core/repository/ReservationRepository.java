package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
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
    select r
    from Reservation r
    join fetch r.guest join fetch r.room LEFT join fetch r.feedback join fetch r.payment
    where r.id = :reservationId
    """)
    Optional<Reservation> findReservationByIdCompleteFetch(Long reservationId);

    @Query("""
    select f
    from Feedback f
    where f.reservation.id = :reservationId
    """)
    Optional<Feedback> getFeedbackByReservationId(Long reservationId);

    @Query("""
    select p
    from Payment p
    where p.reservation.id= :reservationId
    """)
    Optional<Payment> getPaymentByReservationId(Long reservationId);

    @Query("""
       select case when COUNT(r) > 0 then true else false end
       from Reservation r
       where r.room.id = :roomId
       and r.checkOutDate > :checkInDate
       and r.checkInDate < :checkOutDate
       """)
    boolean existsReservationConflict(Long roomId,LocalDate checkOutDate,LocalDate checkInDate);

    @Query("""
       select case when COUNT(r) > 0 then true else false end
       from Reservation r
       where r.room.id = :roomId
       and r.checkOutDate > :checkInDate
       and r.checkInDate < :checkOutDate
       and r.id != :reservationId
       """)
    boolean existsReservationConflictExcludeSelf(Long roomId,LocalDate checkOutDate,LocalDate checkInDate,Long reservationId);

    @Query("""
    select r
    from Reservation r
    join fetch r.guest g join fetch r.room join fetch r.feedback join fetch r.payment
    where g.id= :guestId
    """)
    List<Reservation> findReservationByGuestIdCompleteFetch(Long guestId);


    boolean existsByIdAndFeedbackId(Long reservationId, Long feedbackId);
    boolean existsByIdAndPaymentId(Long reservationId, Long paymentId);
    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfter(Long roomId, LocalDate checkOutDate, LocalDate checkInDate);


    boolean existsByRoomIdAndCheckInDateBeforeAndCheckOutDateAfterAndIdNot(Long roomId, LocalDate checkOutDate,
                                                                           LocalDate checkInDate, Long reservationId);


}
