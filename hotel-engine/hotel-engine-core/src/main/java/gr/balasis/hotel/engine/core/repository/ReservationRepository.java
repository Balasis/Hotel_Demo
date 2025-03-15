package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {




    @Query("""
    select case when exists (
                            select 1
                            from Reservation r
                            where r.id= :reservationId and r.guest.id= :guestID
                            )
    then true
    else false
    end
    """)
    boolean reservationBelongsToGuest(Long reservationId,Long guestID);

    @Query("""
       select case when COUNT(r) > 0 then false else true end
       from Reservation r
       where r.room.id = :roomId
       and r.checkOutDate > :checkInDate
       and r.checkInDate < :checkOutDate
       """)
    boolean isRoomAvailableOn(Long roomId, LocalDate checkOutDate, LocalDate checkInDate);

    @Query("""
       select case when COUNT(r) > 0 then false else true end
       from Reservation r
       where r.room.id = :roomId
       and r.checkOutDate > :checkInDate
       and r.checkInDate < :checkOutDate
       and r.id != :reservationId
       """)
    boolean isRoomAvailableExcludeReservationOn(Long roomId, LocalDate checkOutDate, LocalDate checkInDate, Long reservationId);

    @Query("""
    select r
    from Reservation r
    join fetch r.guest join fetch r.room left join fetch r.feedback join fetch r.payment
    where r.id = :reservationId
    """)
    Optional<Reservation> findByIdCompleteFetch(Long reservationId);

    @Query("""
    select r
    from Reservation r
    join fetch r.guest g join fetch r.room join fetch r.feedback join fetch r.payment
    where g.id= :guestId
    """)
    List<Reservation> findByGuestIdCompleteFetch(Long guestId);


    @Query("""
    select r
    from Reservation r
    left join fetch r.feedback join fetch r.payment
    where r.id = :reservationId
    """)
    Optional<Reservation> findReservationByIdMinimalFetch(Long reservationId);

    @Query("""
    select r.status
    from Reservation r
    where r.id = :reservationId
    """)
    ReservationStatus getReservationStatus(Long reservationId);

    @Query("""
    select p.paymentStatus
    from Payment p
    where p.reservation.id = :reservationId
    """)
    PaymentStatus getReservationPaymentStatus(Long reservationId);


    @Query("""
    select p
    from Payment p
    where p.reservation.id= :reservationId
    """)
    Optional<Payment> getPaymentByReservationId(Long reservationId);

    @Query("""
    select f
    from Feedback f
    where f.reservation.id = :reservationId
    """)
    Optional<Feedback> getFeedbackByReservationId(Long reservationId);

    @Query("""
    select case when exists (
                        select 1
                        from Feedback f
                        where f.reservation.id= :reservationId)
                        then true
                        else false
                        end
    """)
    boolean doesFeedbackExist(Long reservationId);

    @Modifying
    @Query(""" 
    update Feedback f
    SET f.message = :message,
    f.createdAt = :createdAt
    WHERE f.reservation.id = :reservationId
    """)
    void updateFeedback(Long reservationId,String message,LocalDateTime createdAt);

    @Modifying
    @Query("""
    delete from Feedback f
    where f.reservation.id= :reservationId
    """)
    void deleteFeedbackByReservationId(Long reservationId);
}