package gr.balasis.hotel.modules.feedback.service;

import gr.balasis.hotel.modules.feedback.domain.Feedback;

import java.util.List;

public interface FeedbackService extends BaseService<Feedback,Long>{
    Feedback createFeedback(Long id,Long reservationId, Feedback feedback);
    Feedback updateFeedback(Long guestId, Long reservationId, Long feedbackId, Feedback updatedFeedback);
    void deleteFeedback(Long guestId, Long reservationId, Long feedbackId);
//    List<Feedback> getAllFeedbackFromGuestId(Long guestId ,Long reservationId);
}
