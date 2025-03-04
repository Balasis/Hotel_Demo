package gr.balasis.hotel.engine.core.repository;

import gr.balasis.hotel.context.base.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> findByGuestId(Long guestId);

    boolean existsByReservationId(Long reservationId);

    Optional<FeedbackEntity> findByReservationId(Long reservationId);
}