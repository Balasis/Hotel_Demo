package gr.balasis.hotel.modules.feedback.service;

import gr.balasis.hotel.modules.feedback.domain.Feedback;

import java.util.List;

public interface FeedbackService extends BaseService<Feedback,Long>{
    Feedback createFeedback(Long id,Long reservationId, Feedback feedback);
    void updateFeedback(Long guestId, Long reservationId, Feedback updatedFeedback);
    void deleteFeedback(Long guestId, Long reservationId);
    Feedback getFeedbackById(Long guestId,Long reservationId);
}
