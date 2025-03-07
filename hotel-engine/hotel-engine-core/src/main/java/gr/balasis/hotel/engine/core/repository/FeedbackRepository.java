package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.model.Feedback;
import gr.balasis.hotel.context.base.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByReservationId(Long reservationId);
    void deleteByReservation(Reservation reservation);
    Optional<Feedback> findByReservationId(Long reservationId);
}