package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.entity.FeedbackEntity;
import gr.balasis.hotel.context.base.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

    boolean existsByReservationId(Long reservationId);
    void deleteByReservation(ReservationEntity reservation);
    Optional<FeedbackEntity> findByReservationId(Long reservationId);
}