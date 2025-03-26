package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.enumeration.PaymentStatus;
import gr.balasis.hotel.context.base.enumeration.ReservationStatus;
import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Payment;
import gr.balasis.hotel.context.base.model.Reservation;
import gr.balasis.hotel.engine.core.transfer.KeyValue;
import gr.balasis.hotel.engine.core.transfer.ReservationGuestStatisticsDTO;
import gr.balasis.hotel.engine.core.transfer.ReservationRoomStatisticsDTO;
import org.springframework.data.jpa.repository.EntityGraph;
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
                select new gr.balasis.hotel.engine.core.transfer.KeyValue('averageFeedback', AVG(avgResult.totalCount))
                from (
                    select count(r.id) as totalCount
                    from Reservation r
                    where r.feedback is not null
                    group by r.id
                ) avgResult
            """)
    KeyValue<String, Float> getAvgPercentageRateOfFeedback();

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
    boolean reservationBelongsToGuest(Long reservationId, Long guestID);

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
            select case when exists (
                                select 1
                                from Feedback f
                                where f.reservation.id= :reservationId)
                                then true
                                else false
                                end
            """)
    boolean doesFeedbackExist(Long reservationId);

    @Query("""
            select case when exists (
                select 1
                from Feedback f
                where f.reservation.id= :reservationId and f.id=:feedbackId)
                then true
                else false
                end
            """)
    boolean doesFeedbackBelongsToReservation(Long feedbackId, Long reservationId);

    @EntityGraph(attributePaths = {"guest","room","feedback","payment"})
    @Query("""
            select r
            from Reservation r
            where r.guest.id= :guestId
            """)
    List<Reservation> findByGuestIdCompleteFetch(Long guestId);

    @EntityGraph(attributePaths = {"guest","room","feedback","payment"})
    @Query("""
            select r
            from Reservation r
            where r.id = :reservationId
            """)
    Optional<Reservation> findByIdCompleteFetch(Long reservationId);

    //postgre
    @Query(value = """
                select
                ro.id as roomId,
                ro.room_number as roomNumber,
                round(avg(r.CHECK_OUT_DATE - r.CHECK_IN_DATE)) AS averageDaysPerReservation,
                coalesce(
                    (
                        select sum(p1.amount)
                        from payments p1
                        inner join reservations r1 on r1.id = p1.reservation_id
                        where r1.room_id = ro.id and p1.payment_status = 'PAID'
                        group by r1.room_id
                    ),
                    0
                ) as incomeSoFar
            from reservations r
            inner join rooms ro on ro.id = r.room_id
            group by ro.id
            order by incomeSoFar desc;
            """, nativeQuery = true)
    List<ReservationRoomStatisticsDTO> findReservationRoomStatistics();

    //postgre
    @Query(value = """
                select
                g.id as guestId,
                count(r.id) as totalReservations,
                round(avg(r.check_out_date - r.check_in_date)) as avgStayDuration,
                coalesce(
                    (
                        select sum(p.amount)
                        from payments p
                        inner join reservations r1 on r1.id = p.reservation_id
                        where r1.guest_id = g.id and p.payment_status = 'PAID'
                        group by r1.guest_id
                    ),
                    0
                ) as totalIncome
            from guests g
            left join reservations r on r.guest_id = g.id
            group by g.id
            order by totalIncome desc;;
            """, nativeQuery = true)
    List<ReservationGuestStatisticsDTO> findReservationGuestStatistics();

    @EntityGraph(attributePaths = {"guest","room","feedback","payment"})
    @Query("""
            select r
            from Reservation r
            """)
    List<Reservation> findAllCompleteFetch();

    @Query("""
            select p.paymentStatus
            from Payment p
            where p.reservation.id = :reservationId
            """)
    PaymentStatus getReservationPaymentStatus(Long reservationId);

    @Query("""
            select r.status
            from Reservation r
            where r.id = :reservationId
            """)
    Optional<ReservationStatus> getReservationStatus(Long reservationId);

    @EntityGraph(attributePaths = {"feedback","payment"})
    @Query("""
            select r
            from Reservation r
            where r.id = :reservationId
            """)
    Optional<Reservation> findByIdMinimalFetch(Long reservationId);

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

    @Modifying
    @Query(""" 
            update Feedback f
            SET f.message = :message,
            f.createdAt = :createdAt
            WHERE f.reservation.id = :reservationId
            """)
    void updateFeedback(Long reservationId, String message, LocalDateTime createdAt);

    @Modifying
    @Query("""
            delete from Feedback f
            where f.reservation.id= :reservationId
            """)
    void deleteFeedbackByReservationId(Long reservationId);

    @Modifying
    @Query("""
            delete from Feedback f
            where f.id = :feedbackId
            """)
    void deleteFeedbackById(Long feedbackId);


    //H2
//    @Query(value = """
//    SELECT
//        ro.id AS roomId,
//        ro.room_number AS roomNumber,
//        ROUND(AVG(TIMESTAMPDIFF(DAY,r.CHECK_IN_DATE,r.CHECK_OUT_DATE))) AS averageDaysPerReservation,
//        COALESCE(
//                    (select SUM(p1.amount)
//                     from payments p1
//                     inner join reservations r1 on r1.id = p1.reservation_id
//                     where r1.room_id = ro.id and p1.payment_status = 'PAID'
//                     group by ro.id
//                     )
//                     ,0 ) AS incomeSoFar
//    FROM reservations r
//    INNER JOIN rooms ro ON ro.id = r.room_id
//    GROUP BY ro.id
//    """, nativeQuery = true)
//    List<ReservationAnalyticsDTO> getReservationAnalytics();

}