package gr.balasis.hotel.modules.feedback.repository;

import gr.balasis.hotel.modules.feedback.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
}