package gr.balasis.hotel.modules.feedback.repository;

import gr.balasis.hotel.data.entity.ReservationEntity;
import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> findByGuestId(Long guestId);

    boolean existsByReservationId(Long reservationId);
}